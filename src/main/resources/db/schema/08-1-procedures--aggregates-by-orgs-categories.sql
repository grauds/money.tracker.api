CREATE OR ALTER PROCEDURE GET_INCOME_BY_TRADE_PLACE (
    DEST_CODE VARCHAR(16),
    ROWS_LIMIT INTEGER = 20
)
RETURNS (
    NAME VARCHAR(255),
    TOTAL NUMERIC(18, 2),
    TXCOUNT INTEGER
)
AS
DECLARE VARIABLE V_RUB_ID INTEGER;
DECLARE VARIABLE V_DEST_ID INTEGER;
DECLARE VARIABLE V_ORG_ID INTEGER;
DECLARE VARIABLE V_ORG_NAME VARCHAR(255);
DECLARE VARIABLE V_CURR_ID INTEGER;
DECLARE VARIABLE V_CURR_CODE VARCHAR(16);
DECLARE VARIABLE V_TX_DATE DATE;
DECLARE VARIABLE V_DAILY_TOTAL DOUBLE PRECISION;
DECLARE VARIABLE V_DAILY_COUNT INTEGER;
DECLARE VARIABLE V_RATE_SRC DOUBLE PRECISION;
DECLARE VARIABLE V_RATE_DEST DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_CONV DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_COUNT INTEGER;
DECLARE VARIABLE V_EMITTED INTEGER;
BEGIN
    SELECT ID FROM MONEYTYPE WHERE CODE = 'RUB' INTO :V_RUB_ID;
    SELECT ID FROM MONEYTYPE WHERE CODE = :DEST_CODE INTO :V_DEST_ID;

    -- Temporary in-memory sorting via recursive/ordered loop:
    -- First compute converted totals per organization, then sort & slice top N
    V_EMITTED = 0;

    FOR
        SELECT o.ID, COALESCE(o.NAME, 'Unknown')
        FROM Organization o
        WHERE EXISTS (SELECT 1 FROM IncomeItem e WHERE e.tradeplace = o.ID)
        INTO :V_ORG_ID, :V_ORG_NAME
    DO
    BEGIN
        V_SUM_CONV = 0;
        V_SUM_COUNT = 0;

        FOR
            SELECT ex.moneyType,
                   m.CODE,
                   CAST(e.transferdate AS DATE) AS tx_date,
                   SUM(e.total),
                   COUNT(*)
            FROM IncomeItem e
            JOIN Income ex ON ex.ID = e.income
            JOIN MONEYTYPE m ON m.ID = ex.moneyType
            WHERE ex.commontradeplace = :V_ORG_ID
            GROUP BY 1, 2, 3
            INTO :V_CURR_ID, :V_CURR_CODE, :V_TX_DATE, :V_DAILY_TOTAL, :V_DAILY_COUNT
        DO
        BEGIN
            V_RATE_SRC = NULL;
            V_RATE_DEST = NULL;
            V_SUM_COUNT = :V_SUM_COUNT + :V_DAILY_COUNT;

            IF (:V_CURR_CODE = :DEST_CODE) THEN
            BEGIN
                V_SUM_CONV = :V_SUM_CONV + :V_DAILY_TOTAL;
            END
            ELSE IF (:DEST_CODE = 'RUB') THEN
            BEGIN
                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_CURR_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_SRC;

                V_SUM_CONV = :V_SUM_CONV + (:V_DAILY_TOTAL / NULLIF(COALESCE(:V_RATE_SRC, 1), 0));
            END
            ELSE IF (:V_CURR_CODE = 'RUB') THEN
            BEGIN
                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_DEST_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_DEST;

                V_SUM_CONV = :V_SUM_CONV + (:V_DAILY_TOTAL / NULLIF(1.0 / NULLIF(COALESCE(:V_RATE_DEST, 1), 0), 0));
            END
            ELSE
            BEGIN
                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_CURR_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_SRC;

                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_DEST_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_DEST;

                V_SUM_CONV = :V_SUM_CONV + (
                    :V_DAILY_TOTAL / NULLIF(COALESCE(:V_RATE_SRC, 1) / NULLIF(COALESCE(:V_RATE_DEST, 1), 0), 0)
                );
            END
        END

        NAME = :V_ORG_NAME;
        TOTAL = ROUND(COALESCE(:V_SUM_CONV, 0), 2);
        TXCOUNT = :V_SUM_COUNT;
        SUSPEND;
    END
END^


CREATE OR ALTER PROCEDURE GET_EXPENSE_BY_CATEGORIES (
    DEST_CODE VARCHAR(16)
)
RETURNS (
    NAME VARCHAR(255),
    TOTAL NUMERIC(18, 2),
    TXCOUNT INTEGER
)
AS
DECLARE VARIABLE V_RUB_ID INTEGER;
DECLARE VARIABLE V_DEST_ID INTEGER;
DECLARE VARIABLE V_CAT_ID INTEGER;
DECLARE VARIABLE V_CAT_NAME VARCHAR(255);
DECLARE VARIABLE V_CURR_ID INTEGER;
DECLARE VARIABLE V_CURR_CODE VARCHAR(16);
DECLARE VARIABLE V_TX_DATE DATE;
DECLARE VARIABLE V_DAILY_TOTAL DOUBLE PRECISION;
DECLARE VARIABLE V_DAILY_COUNT INTEGER;
DECLARE VARIABLE V_RATE_SRC DOUBLE PRECISION;
DECLARE VARIABLE V_RATE_DEST DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_CONV DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_COUNT INTEGER;
BEGIN
    SELECT ID FROM MONEYTYPE WHERE CODE = 'RUB' INTO :V_RUB_ID;
    SELECT ID FROM MONEYTYPE WHERE CODE = :DEST_CODE INTO :V_DEST_ID;

    -- 1. Iterate through each COMMGROUP that has at least one ExpenseItem via COMMODITY
    FOR
        SELECT c.ID, COALESCE(c.NAME, 'Uncategorized')
        FROM COMMGROUP c
        WHERE EXISTS (
            SELECT 1
            FROM ExpenseItem e
            JOIN COMMODITY cm ON cm.ID = e.comm
            WHERE cm.PARENT = c.ID
        )
        INTO :V_CAT_ID, :V_CAT_NAME
    DO
    BEGIN
        V_SUM_CONV = 0;
        V_SUM_COUNT = 0;

        -- 2. Group daily totals across all commodities belonging to this COMMGROUP
        FOR
            SELECT ex.moneyType,
                   m.CODE,
                   CAST(e.transferdate AS DATE) AS tx_date,
                   SUM(e.total),
                   COUNT(*)
            FROM ExpenseItem e
            JOIN Expense ex ON ex.ID = e.expense
            JOIN COMMODITY cm ON cm.ID = e.comm
            JOIN MONEYTYPE m ON m.ID = ex.moneyType
            WHERE cm.PARENT = :V_CAT_ID
            GROUP BY 1, 2, 3
            INTO :V_CURR_ID, :V_CURR_CODE, :V_TX_DATE, :V_DAILY_TOTAL, :V_DAILY_COUNT
        DO
        BEGIN
            -- Reset rate variables so a missing historical rate falls back to 1
            V_RATE_SRC = NULL;
            V_RATE_DEST = NULL;
            V_SUM_COUNT = :V_SUM_COUNT + :V_DAILY_COUNT;

            IF (:V_CURR_CODE = :DEST_CODE) THEN
            BEGIN
                V_SUM_CONV = :V_SUM_CONV + :V_DAILY_TOTAL;
            END
            ELSE IF (:DEST_CODE = 'RUB') THEN
            BEGIN
                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_CURR_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_SRC;

                V_SUM_CONV = :V_SUM_CONV + (:V_DAILY_TOTAL / NULLIF(COALESCE(:V_RATE_SRC, 1), 0));
            END
            ELSE IF (:V_CURR_CODE = 'RUB') THEN
            BEGIN
                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_DEST_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_DEST;

                V_SUM_CONV = :V_SUM_CONV + (:V_DAILY_TOTAL / NULLIF(1.0 / NULLIF(COALESCE(:V_RATE_DEST, 1), 0), 0));
            END
            ELSE
            BEGIN
                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_CURR_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_SRC;

                SELECT FIRST 1 mtr.RATE
                FROM MONEYTYPERATE mtr
                WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                  AND mtr.SELLMONEYTYPE = :V_DEST_ID
                  AND mtr.RATEDATE < :V_TX_DATE
                ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                INTO :V_RATE_DEST;

                V_SUM_CONV = :V_SUM_CONV + (
                    :V_DAILY_TOTAL / NULLIF(COALESCE(:V_RATE_SRC, 1) / NULLIF(COALESCE(:V_RATE_DEST, 1), 0), 0)
                );
            END
        END

        NAME = :V_CAT_NAME;
        TOTAL = ROUND(COALESCE(:V_SUM_CONV, 0), 2);
        TXCOUNT = :V_SUM_COUNT;
        SUSPEND;
    END
END^


