-- Last expense items with latest transaction date
CREATE OR ALTER VIEW LAST_EXPENSEITEMS (ID, COMM_ID, PRICE, CURRENCY, QTY, UNIT, TRANSACTION_DATE, ORG_ID, DAYS_AGO) AS
SELECT item.ID                                              AS ID,
       c.ID                                                 AS COMM_ID,
       item.PRICE                                           AS PRICE,
       m.CODE                                               AS CURRENCY,
       item.QTY                                             AS QTY,
       u.SHORTNAME                                          AS UNIT,
       item.TRANSFERDATE                                    AS TRANSACTION_DATE,
       o.ID                                                 AS ORG_ID,
       datediff(DAY FROM item.TRANSFERDATE TO current_date) AS DAYS_AGO
FROM EXPENSEITEM AS item

         JOIN (

-- SELECT THE ONES WITH LATEST TRANSACTION TIME
    SELECT *
    FROM (SELECT c.ID, MAX(item.TRANSFERDATE) AS LATEST
          FROM COMMODITY c
                   JOIN EXPENSEITEM item ON item.COMM = c.ID
          GROUP BY c.ID
          ORDER BY LATEST DESC)
) e ON e.ID = item.COMM AND e.LATEST = item.TRANSFERDATE
         JOIN ORGANIZATION o ON o.ID = item.TRADEPLACE
         JOIN COMMODITY c ON c.ID = item.COMM
         JOIN EXPENSE ex ON ex.ID = item.EXPENSE
         JOIN MONEYTYPE m ON ex.MONEYTYPE = m.ID
         JOIN UNITTYPE u on u.ID = c.UNITTYPE
ORDER BY DAYS_AGO ^

-- Number of distinct commodities per month
CREATE OR ALTER VIEW COMMODITIES_BUCKET_PER_MONTH ("AN", "MOIS", COMMODITIES) AS
SELECT
    EXTRACT(YEAR FROM EXPENSEITEM.TRANSFERDATE) as "AN",
    EXTRACT(MONTH FROM EXPENSEITEM.TRANSFERDATE) as "MOIS",
    COUNT(DISTINCT(EXPENSEITEM.COMM)) AS COMMODITIES

FROM EXPENSEITEM
GROUP BY "AN", "MOIS" ^

-- Number of distinct commodities per month (from last expense items)
CREATE OR ALTER VIEW LAST_EXPENSEITEMS_PER_MONTH ("AN", "MOIS", COMMODITIES)
AS
SELECT
    EXTRACT(YEAR FROM LAST_EXPENSEITEMS.TRANSACTION_DATE) as "AN",
    EXTRACT(MONTH FROM LAST_EXPENSEITEMS.TRANSACTION_DATE) as "MOIS",
    COUNT(*) AS COMMODITIES

FROM LAST_EXPENSEITEMS
GROUP BY "AN", "MOIS" ^

-- Commodity price volatility analysis
CREATE OR ALTER VIEW COMMODITY_PRICE_VOLATILITY AS
SELECT
    e.COMM AS commodity_id,
    c.NAME AS commodity_name,
    c.UNITTYPE AS unit_type,
    c.DEFAULTPRICE AS catalog_default_price,
    COUNT(*) AS total_transactions,
    AVG(e.PRICE) AS mean_unit_price,

    (SELECT MIN(m.TRANSFERDATE) FROM EXPENSEITEM m WHERE m.COMM = e.COMM AND m.PRICE IS NOT NULL) AS oldest_purchase_date,

    (SELECT FIRST 1 old_e.PRICE
     FROM EXPENSEITEM old_e
     WHERE old_e.COMM = e.COMM AND old_e.PRICE IS NOT NULL
     ORDER BY old_e.TRANSFERDATE ASC, old_e.ID ASC) AS oldest_unit_price,

    (SELECT MAX(mx.TRANSFERDATE) FROM EXPENSEITEM mx WHERE mx.COMM = e.COMM AND mx.PRICE IS NOT NULL) AS latest_purchase_date,

    (SELECT FIRST 1 new_e.PRICE
     FROM EXPENSEITEM new_e
     WHERE new_e.COMM = e.COMM AND new_e.PRICE IS NOT NULL
     ORDER BY new_e.TRANSFERDATE DESC, new_e.ID DESC) AS latest_unit_price,

    ROUND(
            (
                ((SELECT FIRST 1 n.PRICE FROM EXPENSEITEM n WHERE n.COMM = e.COMM AND n.PRICE IS NOT NULL ORDER BY n.TRANSFERDATE DESC, n.ID DESC)
                    -
                 (SELECT FIRST 1 o.PRICE FROM EXPENSEITEM o WHERE o.COMM = e.COMM AND o.PRICE IS NOT NULL ORDER BY o.TRANSFERDATE ASC, o.ID ASC))
                    /
                NULLIF((SELECT FIRST 1 o2.PRICE FROM EXPENSEITEM o2 WHERE o2.COMM = e.COMM AND o2.PRICE IS NOT NULL ORDER BY o2.TRANSFERDATE ASC, o2.ID ASC), 0)
                ) * 100,
            2
    ) AS inflation_rate_pct,

    SQRT(
            CASE
                WHEN (COUNT(*) * SUM(e.PRICE * e.PRICE) - SUM(e.PRICE) * SUM(e.PRICE)) < 0 THEN 0
                ELSE (COUNT(*) * SUM(e.PRICE * e.PRICE) - SUM(e.PRICE) * SUM(e.PRICE))
                END
                /
            (COUNT(*) * (COUNT(*) - 1))
    ) AS std_dev_unit_price,

    ROUND(
            (
                SQRT(
                        CASE
                            WHEN (COUNT(*) * SUM(e.PRICE * e.PRICE) - SUM(e.PRICE) * SUM(e.PRICE)) < 0 THEN 0
                            ELSE (COUNT(*) * SUM(e.PRICE * e.PRICE) - SUM(e.PRICE) * SUM(e.PRICE))
                            END
                            /
                        (COUNT(*) * (COUNT(*) - 1))
                )
                    /
                AVG(e.PRICE)
                ) * 100,
            2
    ) AS volatility_coefficient_pct

FROM EXPENSEITEM e
         LEFT JOIN COMMODITY c ON e.COMM = c.ID
WHERE e.PRICE IS NOT NULL
  AND e.COMM IS NOT NULL
GROUP BY e.COMM, c.NAME, c.UNITTYPE, c.DEFAULTPRICE
HAVING COUNT(*) > 1 ^

-- Income vs expense delta per commodity
CREATE OR ALTER VIEW IN_OUT_DELTA ("COMM_ID", "COMM_NAME", "ETOTAL", "ITOTAL", DELTA, MID, MCODE, "EXPENCES", "INCOMES") AS
select b.ID as COMM_ID,
       b.NAME as COMM_NAME,
       ROUND(b.etotal, 2),
       ROUND(b.itotal, 2),
       ROUND((b.itotal - b.etotal), 2) as delta,
       b.MID,
       b.ecode as MCODE,
       b.expences,
       b.incomes
from (
         select * from
             (select c.ID, c.name,
                     expense.etotal, me.CODE as ecode,
                     income.itotal, mi.CODE as icode,
                     mi.ID as MID,
                     expences,
                     incomes
              from
                  (select e.COMM as ecomm, ex.MONEYTYPE as emoney, SUM(e.TOTAL) as etotal, COUNT(e.ID) as expences
                   FROM EXPENSEITEM as e
                            LEFT JOIN EXPENSE as ex ON ex.ID = e.EXPENSE
                   group by e.COMM, ex.MONEYTYPE) as expense

                      LEFT JOIN

                  (select i.COMM as icomm, ix.MONEYTYPE as imoney, SUM(i.TOTAL) as itotal, COUNT(i.ID) as incomes
                   FROM INCOMEITEM as i
                            LEFT JOIN INCOME as ix ON ix.ID = i.INCOME
                   group by i.COMM, ix.MONEYTYPE) as income

                  ON expense.ecomm = income.icomm AND expense.emoney = income.imoney

                      LEFT JOIN COMMODITY as c ON income.icomm = c.ID
                      LEFT JOIN MONEYTYPE as mi ON income.imoney = mi.ID
                      LEFT JOIN MONEYTYPE as me ON expense.emoney = me.ID

              WHERE expense.ecomm is not null AND income.icomm is not null
             ) as a

     ) as b ORDER BY delta desc ^
