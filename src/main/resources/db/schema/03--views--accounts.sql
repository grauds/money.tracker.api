-- Last balances for all accounts (depends on ALL_OPERATIONS_HISTORY)
CREATE OR ALTER VIEW ACCOUNTS_TOTAL_LAST (ID, NAME, BALANCE, TOTAL, CODE) AS
SELECT ACCOUNT as ID, acc.NAME, BALANCE, ROUND(TOTAL, 2), CODE
FROM (SELECT ACCOUNT,
             SUM(
                 CASE
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

-- Operations history with exchange rates (depends on ALL_OPERATIONS_HISTORY)
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
