-- Daily delta (depends on ALL_OPERATIONS_HISTORY)
CREATE OR ALTER VIEW DAILY_DELTA (TRANSFERDATE, DELTA, CODE) AS
SELECT TRANSFERDATE, ROUND(TOTAL, 2) AS DELTA, CODE
FROM (SELECT TRANSFERDATE,
             SUM(
                 CASE
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

-- Monthly delta (depends on ALL_OPERATIONS_HISTORY)
CREATE OR ALTER VIEW MONTHLY_DELTA ("MOIS", "AN", DELTA, CODE) AS
SELECT "MOIS", "AN", ROUND(TOTAL, 2) AS DELTA, CODE
FROM (SELECT EXTRACT(MONTH FROM TRANSFERDATE) AS "MOIS",
             EXTRACT(YEAR FROM TRANSFERDATE) AS "AN",
             SUM(
                 CASE
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
