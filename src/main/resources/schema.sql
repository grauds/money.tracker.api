CREATE OR ALTER VIEW ALL_OPERATIONS_HISTORY (TRANSFERDATE, ACCOUNT, AMOUNT, MONEY_SIGN) AS
SELECT * FROM (
         SELECT inc.TRANSFERDATE AS TRANSFERDATE, inc.ACCOUNT AS ACCOUNT, inc.TOTAL AS AMOUNT, 1 AS MONEY_SIGN
         FROM INCOME AS inc
         UNION ALL
         SELECT ex.TRANSFERDATE AS TRANSFERDATE, ex.ACCOUNT AS ACCOUNT, ex.TOTAL AS AMOUNT, -1 AS MONEY_SIGN
         FROM EXPENSE AS ex
         UNION ALL
         SELECT tr.TRANSFERDATE AS TRANSFERDATE, tr.DEST AS ACCOUNT, tr.DESTAMOUNT AS AMOUNT, 1 AS MONEY_SIGN
         FROM MONEYTRANSFER AS tr
         UNION ALL
         SELECT tr2.TRANSFERDATE AS TRANSFERDATE, tr2."SOURCE" AS ACCOUNT, tr2.AMOUNT AS AMOUNT, -1 AS MONEY_SIGN
         FROM MONEYTRANSFER AS tr2
         UNION ALL
         SELECT mex.EXCHANGEDATE AS TRANSFERDATE, mex.DEST AS ACCOUNT, mex.DESTAMOUNT AS AMOUNT, 1 AS MONEY_SIGN
         FROM MONEYEXCHANGE AS mex
         UNION ALL
         SELECT mex2.EXCHANGEDATE AS TRANSFERDATE, mex2."SOURCE" AS ACCOUNT, mex2.SOURCEAMOUNT AS AMOUNT, -1 AS MONEY_SIGN
         FROM MONEYEXCHANGE AS mex2
         UNION ALL
         SELECT br.CREDITDATE AS TRANSFERDATE, br.ACCOUNT AS ACCOUNT, br.CREDITSUM AS AMOUNT, 1 AS MONEY_SIGN
         FROM BORROWING AS br
         UNION ALL
         SELECT brp.PAYMENTDATE AS TRANSFERDATE, brp.ACCOUNT AS ACCOUNT, brp.TOTALPAID AS AMOUNT, -1 AS MONEY_SIGN
         FROM BORROWINGPAYMENT AS brp
         WHERE brp.STATUS = 1
           AND brp.PAYMENTTYPE = 0
         UNION ALL
         SELECT brp2.PAYMENTDATE AS TRANSFERDATE, brp2.ACCOUNT AS ACCOUNT, brp2.INCREASE AS AMOUNT, 1 AS MONEY_SIGN
         FROM BORROWINGPAYMENT AS brp2
         WHERE brp2.STATUS = 1
           AND brp2.PAYMENTTYPE = 1
         UNION ALL
         SELECT ld.CREDITDATE AS TRANSFERDATE, ld.ACCOUNT AS ACCOUNT, ld.CREDITSUM AS AMOUNT, -1 AS MONEY_SIGN
         FROM LENDING AS ld
         UNION ALL
         SELECT ldp.PAYMENTDATE AS TRANSFERDATE, ldp.ACCOUNT AS ACCOUNT, ldp.TOTALPAID AS AMOUNT, 1 AS MONEY_SIGN
         FROM LENDINGPAYMENT AS ldp
         WHERE ldp.STATUS = 1
           AND ldp.PAYMENTTYPE = 0
         UNION ALL
         SELECT ldp2.PAYMENTDATE AS TRANSFERDATE, ldp2.ACCOUNT AS ACCOUNT, ldp2.INCREASE AS AMOUNT, -1 AS MONEY_SIGN
         FROM LENDINGPAYMENT AS ldp2
         WHERE ldp2.STATUS = 1
           AND ldp2.PAYMENTTYPE = 1
     )
ORDER BY TRANSFERDATE;

----------------------------------------------------------

CREATE OR ALTER VIEW ACCOUNTS_TOTAL_LAST (ACCOUNT, NAME, BALANCE, TOTAL, CODE) AS
SELECT ACCOUNT, acc.NAME, BALANCE, ROUND(TOTAL, 2), CODE
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
ORDER BY TOTAL;

----------------------------------------------------------

CREATE OR ALTER VIEW ACCOUNTS_OPERATIONS (NAME, TRANSFERDATE, AMOUNT, CURRENCY) AS
SELECT acc.NAME, TRANSFERDATE, ROUND(AMOUNT * MONEY_SIGN, 2), CODE
FROM ALL_OPERATIONS_HISTORY
         JOIN ACCOUNT AS acc ON acc.ID = ACCOUNT
         JOIN MONEYTYPE AS mt ON acc.MONEYTYPE = mt.ID
ORDER BY TRANSFERDATE;

----------------------------------------------------------

CREATE OR ALTER VIEW LAST_COMMODITIES_VIEW (ID, NAME, PRICE, CURRENCY, QTY, TRANSACTION_DATE, ORG, DAYS_AGO) AS
SELECT c.ID                                                 AS ID,
       c.NAME                                               AS NAME,
       item.PRICE                                           AS PRICE,
       m.CODE                                               AS CURRENCY,
       item.QTY                                             AS QTY,
       item.TRANSFERDATE                                    AS TRANSACTION_DATE,
       o.NAME                                               AS ORG,
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
    WHERE LATEST > DATEADD(YEAR, -15, CURRENT_DATE)
) e ON e.ID = item.COMM AND e.LATEST = item.TRANSFERDATE
         JOIN ORGANIZATION o ON o.ID = item.TRADEPLACE
         JOIN COMMODITY c ON c.ID = item.COMM
         JOIN EXPENSE ex ON ex.ID = item.EXPENSE
         JOIN MONEYTYPE m ON ex.MONEYTYPE = m.ID
ORDER BY DAYS_AGO DESC;

----------------------------------------------------------

CREATE OR ALTER VIEW COMMODITIES_BUCKET_PER_MONTH ("YEAR", "MONTH", COMMODITIES) AS
SELECT
    EXTRACT(YEAR FROM EXPENSEITEM.TRANSFERDATE) as "YEAR",
    EXTRACT(MONTH FROM EXPENSEITEM.TRANSFERDATE) as "MONTH",
    COUNT(DISTINCT(EXPENSEITEM.COMM)) AS COMMODITIES

FROM EXPENSEITEM
GROUP BY "YEAR", "MONTH";

----------------------------------------------------------

CREATE OR ALTER VIEW LAST_COMMODITIES_PER_MONTH ("YEAR", "MONTH", COMMODITIES)
AS
SELECT
    EXTRACT(YEAR FROM LAST_COMMODITIES_VIEW.TRANSACTION_DATE) as "YEAR",
    EXTRACT(MONTH FROM LAST_COMMODITIES_VIEW.TRANSACTION_DATE) as "MONTH",
    COUNT(*) AS COMMODITIES

FROM LAST_COMMODITIES_VIEW
GROUP BY "YEAR", "MONTH";