CREATE OR ALTER VIEW ALL_OPERATIONS_HISTORY (TRANSFERDATE, ACCOUNT, AMOUNT, MONEY_SIGN, MONEYTYPE) AS
SELECT TRANSFERDATE, ACCOUNT, AMOUNT, MONEY_SIGN, MONEYTYPE FROM (
     SELECT inc.TRANSFERDATE AS TRANSFERDATE, inc.ACCOUNT AS ACCOUNT, inc.TOTAL AS AMOUNT, 1 AS MONEY_SIGN, inc.MONEYTYPE AS MONEYTYPE
     FROM INCOME AS inc
     UNION ALL
     SELECT ex.TRANSFERDATE AS TRANSFERDATE, ex.ACCOUNT AS ACCOUNT, ex.TOTAL AS AMOUNT, -1 AS MONEY_SIGN, ex.MONEYTYPE AS MONEYTYPE
     FROM EXPENSE AS ex
     UNION ALL
     SELECT tr.TRANSFERDATE AS TRANSFERDATE, tr.DEST AS ACCOUNT, tr.DESTAMOUNT AS AMOUNT, 1 AS MONEY_SIGN, tr.MONEYTYPE AS MONEYTYPE
     FROM MONEYTRANSFER AS tr
     UNION ALL
     SELECT tr2.TRANSFERDATE AS TRANSFERDATE, tr2."SOURCE" AS ACCOUNT, tr2.AMOUNT AS AMOUNT, -1 AS MONEY_SIGN, tr2.MONEYTYPE AS MONEYTYPE
     FROM MONEYTRANSFER AS tr2
     UNION ALL
     SELECT mex.EXCHANGEDATE AS TRANSFERDATE, mex.DEST AS ACCOUNT, mex.DESTAMOUNT AS AMOUNT, 1 AS MONEY_SIGN, mex.DESTMONEYTYPE AS MONEYTYPE
     FROM MONEYEXCHANGE AS mex
     UNION ALL
     SELECT mex2.EXCHANGEDATE AS TRANSFERDATE, mex2."SOURCE" AS ACCOUNT, mex2.SOURCEAMOUNT AS AMOUNT, -1 AS MONEY_SIGN, mex2.SOURCEMONEYTYPE AS MONEYTYPE
     FROM MONEYEXCHANGE AS mex2
     UNION ALL
     SELECT br.CREDITDATE AS TRANSFERDATE, br.ACCOUNT AS ACCOUNT, br.CREDITSUM AS AMOUNT, 1 AS MONEY_SIGN, br.MONEYTYPE AS MONEYTYPE
     FROM BORROWING AS br
     UNION ALL
     SELECT brp.PAYMENTDATE AS TRANSFERDATE, brp.ACCOUNT AS ACCOUNT, brp.TOTALPAID AS AMOUNT, -1 AS MONEY_SIGN, brparent.MONEYTYPE AS MONEYTYPE
     FROM BORROWING AS brparent, BORROWINGPAYMENT AS brp
     WHERE brp.STATUS = 1
       AND brp.PAYMENTTYPE = 0
       AND brparent.id = brp.CREDIT
     UNION ALL
     SELECT brp2.PAYMENTDATE AS TRANSFERDATE, brp2.ACCOUNT AS ACCOUNT, brp2.INCREASE AS AMOUNT, 1 AS MONEY_SIGN, br2parent.MONEYTYPE AS MONEYTYPE
     FROM BORROWING AS br2parent, BORROWINGPAYMENT AS brp2
     WHERE brp2.STATUS = 1
       AND brp2.PAYMENTTYPE = 1
       AND br2parent.id = brp2.CREDIT
     UNION ALL
     SELECT ld.CREDITDATE AS TRANSFERDATE, ld.ACCOUNT AS ACCOUNT, ld.CREDITSUM AS AMOUNT, -1 AS MONEY_SIGN, ld.MONEYTYPE AS MONEYTYPE
     FROM LENDING AS ld
     UNION ALL
     SELECT ldp.PAYMENTDATE AS TRANSFERDATE, ldp.ACCOUNT AS ACCOUNT, ldp.TOTALPAID AS AMOUNT, 1 AS MONEY_SIGN, ldparent.MONEYTYPE AS MONEYTYPE
     FROM LENDING AS ldparent, LENDINGPAYMENT AS ldp
     WHERE ldp.STATUS = 1
       AND ldp.PAYMENTTYPE = 0
       AND ldparent.ID = ldp.CREDIT
     UNION ALL
     SELECT ldp2.PAYMENTDATE AS TRANSFERDATE, ldp2.ACCOUNT AS ACCOUNT, ldp2.INCREASE AS AMOUNT, -1 AS MONEY_SIGN, ld2parent.MONEYTYPE AS MONEYTYPE
     FROM LENDING AS ld2parent, LENDINGPAYMENT AS ldp2
     WHERE ldp2.STATUS = 1
       AND ldp2.PAYMENTTYPE = 1
       AND ld2parent.ID = ldp2.CREDIT
    ) ^


----------------------------------------------------------

CREATE OR ALTER VIEW ACCOUNTS_TOTAL_LAST (ID, NAME, BALANCE, TOTAL, CODE) AS
SELECT ACCOUNT as ID, acc.NAME, BALANCE, ROUND(TOTAL, 2), CODE
FROM (SELECT ACCOUNT,
             SUM
                 (CASE
                      WHEN MONEY_SIGN = 1
                          THEN AMOUNT
                      ELSE AMOUNT * (-1) END
                 ) AS TOTAL
      FROM (
               SELECT *
               FROM ALL_OPERATIONS_HISTORY
           )
      GROUP BY ACCOUNT)
         JOIN ACCOUNT AS acc ON acc.ID = ACCOUNT
         JOIN MONEYTYPE AS mt ON acc.MONEYTYPE = mt.ID
ORDER BY TOTAL ^

----------------------------------------------------------

CREATE OR ALTER VIEW ACCOUNTS_OPERATIONS (NAME, TRANSFERDATE, AMOUNT, CURRENCY, EX_RATE, SINGLE_CUR_AMOUNT, CUR_CODE) AS
SELECT acc.NAME,
       TRANSFERDATE,
       ROUND(AMOUNT * MONEY_SIGN, 2) AS AMOUNT,
       mt.CODE as CODE,
       coalesce(mtr.RATE, 1) as EX_RATE,
       ROUND((AMOUNT * MONEY_SIGN) / coalesce(mtr.RATE, 1), 2) AS SINGLE_CUR_AMOUNT,
       coalesce(mt2.CODE, 'RUB') as CUR_CODE
FROM ALL_OPERATIONS_HISTORY
         JOIN ACCOUNT AS acc ON acc.ID = ACCOUNT
         JOIN MONEYTYPE AS mt ON acc.MONEYTYPE = mt.ID
         LEFT JOIN MONEYTYPERATE AS mtr ON
             (select first 1 RATEDATE from MONEYTYPERATE
                where RATEDATE <= TRANSFERDATE ORDER BY RATEDATE DESC) = mtr.RATEDATE
                 AND acc.MONEYTYPE = mtr.SELLMONEYTYPE AND mtr.BUYMONEYTYPE = 1
         LEFT JOIN MONEYTYPE AS mt2 ON mtr.BUYMONEYTYPE = mt2.ID
ORDER BY TRANSFERDATE ^

----------------------------------------------------------

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

----------------------------------------------------------

CREATE OR ALTER VIEW COMMODITIES_BUCKET_PER_MONTH ("AN", "MOIS", COMMODITIES) AS
SELECT
    EXTRACT(YEAR FROM EXPENSEITEM.TRANSFERDATE) as "AN",
    EXTRACT(MONTH FROM EXPENSEITEM.TRANSFERDATE) as "MOIS",
    COUNT(DISTINCT(EXPENSEITEM.COMM)) AS COMMODITIES

FROM EXPENSEITEM
GROUP BY "AN", "MOIS" ^

----------------------------------------------------------

CREATE OR ALTER VIEW LAST_EXPENSEITEMS_PER_MONTH ("AN", "MOIS", COMMODITIES)
AS
SELECT
    EXTRACT(YEAR FROM LAST_EXPENSEITEMS.TRANSACTION_DATE) as "AN",
    EXTRACT(MONTH FROM LAST_EXPENSEITEMS.TRANSACTION_DATE) as "MOIS",
    COUNT(*) AS COMMODITIES

FROM LAST_EXPENSEITEMS
GROUP BY "AN", "MOIS" ^

----------------------------------------------------------

CREATE OR ALTER VIEW DAILY_DELTA (TRANSFERDATE, DELTA, CODE) AS
SELECT TRANSFERDATE, ROUND(TOTAL, 2) AS DELTA, CODE
FROM (SELECT TRANSFERDATE,
             SUM
                 (CASE
                      WHEN MONEY_SIGN = 1
                          THEN AMOUNT
                      ELSE AMOUNT * (-1) END
                 ) AS TOTAL,
             MONEYTYPE
      FROM (
               SELECT *
               FROM ALL_OPERATIONS_HISTORY
           )
      GROUP BY TRANSFERDATE, MONEYTYPE)
         JOIN MONEYTYPE AS mt ON MONEYTYPE = mt.ID
ORDER BY TRANSFERDATE ^

----------------------------------------------------------

CREATE OR ALTER VIEW MONTHLY_DELTA ("MOIS", "AN", DELTA, CODE) AS
SELECT "MOIS", "AN", ROUND(TOTAL, 2) AS DELTA, CODE
FROM (SELECT EXTRACT(MONTH FROM TRANSFERDATE) AS "MOIS",
             EXTRACT(YEAR FROM TRANSFERDATE) AS "AN",
             SUM
                 (CASE
                      WHEN MONEY_SIGN = 1
                          THEN AMOUNT
                      ELSE AMOUNT * (-1) END
                 ) AS TOTAL,
             MONEYTYPE
      FROM (
               SELECT *
               FROM ALL_OPERATIONS_HISTORY
           )
      GROUP BY EXTRACT(MONTH FROM TRANSFERDATE), EXTRACT(YEAR FROM TRANSFERDATE), MONEYTYPE)
         JOIN MONEYTYPE AS mt ON MONEYTYPE = mt.ID
ORDER BY "AN", "MOIS" ^

----------------------------------------------------------

CREATE OR ALTER PROCEDURE CROSS_RATE (SRC VARCHAR(16), DEST VARCHAR(16), RATE_DATE DATE = CURRENT_DATE)
    RETURNS (RATE DOUBLE PRECISION)
AS
BEGIN
    RATE = 1; -- default for roubles and roubles

    IF (:DEST = 'RUB' AND :SRC <> 'RUB') THEN
    BEGIN
        FOR
            SELECT FIRST 1 * FROM(
                SELECT ROUND(mtr.RATE, 15) as RATE FROM MONEYTYPERATE as mtr
                WHERE (mtr.BUYMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:DEST)
                AND mtr.SELLMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:SRC))
                AND mtr.RATEDATE < :RATE_DATE
                ORDER BY mtr.RATEDATE DESC
                )
            INTO :RATE
            DO
                SUSPEND;
    END
    ELSE IF (:SRC = 'RUB' AND :DEST <> 'RUB') THEN
    BEGIN
        FOR
            SELECT FIRST 1 * FROM(
                SELECT ROUND(1 / mtr.RATE, 15) as RATE FROM MONEYTYPERATE as mtr
                WHERE (mtr.BUYMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:SRC)
                AND mtr.SELLMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:DEST))
                AND mtr.RATEDATE < :RATE_DATE
                ORDER BY mtr.RATEDATE DESC
                )
            INTO :RATE
            DO
                SUSPEND;
        END
    ELSE IF (:SRC <> 'RUB' AND :DEST <> 'RUB') THEN
    BEGIN
        FOR
            SELECT FIRST 1 * FROM (SELECT ROUND(mtr1.RATE/mtr2.RATE, 15) as RATE FROM MONEYTYPERATE as mtr1
                LEFT JOIN MONEYTYPERATE as mtr2
                ON mtr1.RATEDATE = mtr2.RATEDATE
                AND mtr1.SELLMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:SRC)
                AND mtr2.SELLMONEYTYPE = (SELECT ID FROM MONEYTYPE WHERE CODE=:DEST)
                AND mtr1.RATEDATE < :RATE_DATE
                AND mtr2.RATEDATE < :RATE_DATE
                WHERE (mtr1.RATE IS NOT NULL AND mtr2.RATE IS NOT NULL)
                ORDER BY mtr1.RATEDATE DESC
                )
            INTO :RATE
            DO
                SUSPEND;
    END
    ELSE
    BEGIN
        FOR
            SELECT FIRST 1 * FROM (SELECT 1 as RATE FROM MONEYTYPERATE)
            INTO :RATE
            DO
                SUSPEND;
    END
END ^

----------------------------------------------------------
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
----------------------------------------------------------

CREATE OR ALTER VIEW MONTHLY_INCOME ("MOIS", "AN", TOTAL, COMM_ID, COMMODITY, CODE) AS
SELECT "MOIS", "AN", ROUND(TOTAL, 2) AS TOTAL, COMM_ID, COMMODITY, CODE
FROM (SELECT EXTRACT(MONTH FROM INCOMEITEM.TRANSFERDATE) AS "MOIS",
             EXTRACT(YEAR FROM INCOMEITEM.TRANSFERDATE) AS "AN",
             SUM(INCOMEITEM.TOTAL) as TOTAL,
             C.ID AS COMM_ID,
             C.NAME AS COMMODITY,
             M.CODE AS CODE
      FROM INCOMEITEM
               LEFT JOIN INCOME I on INCOMEITEM.INCOME = I.ID
               LEFT JOIN COMMODITY C on INCOMEITEM.COMM = C.ID
               LEFT JOIN ACCOUNT A on I.ACCOUNT = A.ID
               LEFT JOIN MONEYTYPE M on I.MONEYTYPE = M.ID
      GROUP BY EXTRACT(MONTH FROM INCOMEITEM.TRANSFERDATE),
               EXTRACT(YEAR FROM INCOMEITEM.TRANSFERDATE),
               COMM_ID,
               COMMODITY,
               CODE
      )
ORDER BY "AN", "MOIS" ^

----------------------------------------------------------

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
HAVING COUNT(*) > 1;