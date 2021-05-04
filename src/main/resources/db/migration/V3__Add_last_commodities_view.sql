CREATE
OR ALTER
VIEW LAST_COMMODITIES_VIEW (ID, NAME, PRICE, CURRENCY, QTY, TRANSACTION_DATE, ORG, DAYS_AGO)
AS
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
