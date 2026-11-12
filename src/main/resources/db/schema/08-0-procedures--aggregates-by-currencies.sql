-- Get income totals grouped by currency with conversion to target currency
CREATE OR ALTER PROCEDURE GET_INCOME_BY_CURRENCY (
    DEST_CODE VARCHAR(16) -- Target presentation currency code (e.g., 'USD', 'EUR', 'RUB')
)
RETURNS (
    CODE VARCHAR(16),               -- Original transaction currency code
    TOTAL NUMERIC(18, 2),           -- Total amount in the original currency
    TOTALCONVERTED NUMERIC(18, 2)   -- Total amount converted into DEST_CODE
)
AS
DECLARE VARIABLE V_RUB_ID INTEGER;
DECLARE VARIABLE V_DEST_ID INTEGER;
DECLARE VARIABLE V_CURR_ID INTEGER;
DECLARE VARIABLE V_CURR_CODE VARCHAR(16);
DECLARE VARIABLE V_TX_DATE DATE;
DECLARE VARIABLE V_DAILY_TOTAL DOUBLE PRECISION;
DECLARE VARIABLE V_RATE_SRC DOUBLE PRECISION;
DECLARE VARIABLE V_RATE_DEST DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_TOTAL DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_CONV DOUBLE PRECISION;
BEGIN
    -- 1. Resolve base RUB ID and target currency ID once at procedure startup
    SELECT ID FROM MONEYTYPE WHERE CODE = 'RUB' INTO :V_RUB_ID;
    SELECT ID FROM MONEYTYPE WHERE CODE = :DEST_CODE INTO :V_DEST_ID;

    -- 2. Iterate over each distinct currency present in the Income table
    FOR
        SELECT DISTINCT m.ID, m.CODE
        FROM Income e
        JOIN MONEYTYPE m ON m.ID = e.moneyType
        INTO :V_CURR_ID, :V_CURR_CODE
    DO
    BEGIN
        -- Initialize running accumulators for the current currency
        V_SUM_TOTAL = 0;
        V_SUM_CONV = 0;

        -- 3. Fast path: if the currency already matches DEST_CODE, sum directly without rate lookups
        IF (:V_CURR_CODE = :DEST_CODE) THEN
        BEGIN
            SELECT SUM(e.total)
            FROM Income e
            WHERE e.moneyType = :V_CURR_ID
            INTO :V_SUM_TOTAL;

            V_SUM_CONV = COALESCE(:V_SUM_TOTAL, 0);
        END
        ELSE
        BEGIN
            -- 4. Group transactions by calendar day (casting TIMESTAMP to DATE)
            -- Collapses multiple same-day entries so each date requires only a single rate lookup
            FOR
                SELECT CAST(e.transferdate AS DATE) AS tx_date, SUM(e.total)
                FROM Income e
                WHERE e.moneyType = :V_CURR_ID
                GROUP BY 1
                ORDER BY 1 ASC
                INTO :V_TX_DATE, :V_DAILY_TOTAL
            DO
            BEGIN
                -- IMPORTANT: Reset rate variables on every iteration.
                -- In Firebird PSQL, a SELECT INTO returning 0 rows leaves the variable unchanged
                -- rather than setting it to NULL, which would otherwise reuse the previous day's rate.
                V_RATE_SRC = NULL;
                V_RATE_DEST = NULL;

                V_SUM_TOTAL = :V_SUM_TOTAL + :V_DAILY_TOTAL;

                -- 5A. Converting directly into RUB (SRC -> RUB)
                -- NOTE: All 3 columns in ORDER BY must be DESC so Firebird uses composite index IDX_MTR_PAIR_DATE_DESC
                IF (:DEST_CODE = 'RUB') THEN
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
                -- 5B. Converting out of RUB (RUB -> DEST)
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
                -- 5C. Cross-rate between two non-RUB currencies (SRC -> RUB -> DEST)
                ELSE
                BEGIN
                    -- Fetch latest rate for SRC -> RUB prior to transaction date
                    SELECT FIRST 1 mtr.RATE
                    FROM MONEYTYPERATE mtr
                    WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                      AND mtr.SELLMONEYTYPE = :V_CURR_ID
                      AND mtr.RATEDATE < :V_TX_DATE
                    ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                    INTO :V_RATE_SRC;

                    -- Fetch latest rate for DEST -> RUB prior to transaction date
                    SELECT FIRST 1 mtr.RATE
                    FROM MONEYTYPERATE mtr
                    WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
                      AND mtr.SELLMONEYTYPE = :V_DEST_ID
                      AND mtr.RATEDATE < :V_TX_DATE
                    ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
                    INTO :V_RATE_DEST;

                    -- Compute cross-rate via RUB while guarding against division by zero
                    V_SUM_CONV = :V_SUM_CONV + (
                        :V_DAILY_TOTAL / NULLIF(COALESCE(:V_RATE_SRC, 1) / NULLIF(COALESCE(:V_RATE_DEST, 1), 0), 0)
                    );
                END
            END
        END

        -- 6. Round final currency totals to 2 decimal places and emit result row
        CODE = :V_CURR_CODE;
        TOTAL = ROUND(COALESCE(:V_SUM_TOTAL, 0), 2);
        TOTALCONVERTED = ROUND(COALESCE(:V_SUM_CONV, 0), 2);
        SUSPEND;
    END
END ^

-- Get expense totals grouped by currency with conversion to target currency
CREATE OR ALTER PROCEDURE GET_EXPENSE_BY_CURRENCY (
    DEST_CODE VARCHAR(16)
)
RETURNS (
    CODE VARCHAR(16),
    TOTAL NUMERIC(18, 2),
    TOTALCONVERTED NUMERIC(18, 2)
)
AS
DECLARE VARIABLE V_RUB_ID INTEGER;
DECLARE VARIABLE V_DEST_ID INTEGER;
DECLARE VARIABLE V_CURR_ID INTEGER;
DECLARE VARIABLE V_CURR_CODE VARCHAR(16);
DECLARE VARIABLE V_TX_DATE DATE;
DECLARE VARIABLE V_DAILY_TOTAL DOUBLE PRECISION;
DECLARE VARIABLE V_RATE_SRC DOUBLE PRECISION;
DECLARE VARIABLE V_RATE_DEST DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_TOTAL DOUBLE PRECISION;
DECLARE VARIABLE V_SUM_CONV DOUBLE PRECISION;
BEGIN
    SELECT ID FROM MONEYTYPE WHERE CODE = 'RUB' INTO :V_RUB_ID;
    SELECT ID FROM MONEYTYPE WHERE CODE = :DEST_CODE INTO :V_DEST_ID;

    -- Loop through each currency present in Expense
    FOR
        SELECT DISTINCT m.ID, m.CODE
        FROM Expense e
        JOIN MONEYTYPE m ON m.ID = e.moneyType
        INTO :V_CURR_ID, :V_CURR_CODE
    DO
    BEGIN
        V_SUM_TOTAL = 0;
        V_SUM_CONV = 0;

        -- Fast path: same currency as target
        IF (:V_CURR_CODE = :DEST_CODE) THEN
        BEGIN
            SELECT SUM(e.total)
            FROM Expense e
            WHERE e.moneyType = :V_CURR_ID
            INTO :V_SUM_TOTAL;

            V_SUM_CONV = COALESCE(:V_SUM_TOTAL, 0);
        END
        ELSE
        BEGIN
            -- Walk daily totals in chronological order
            FOR
                SELECT CAST(ex.transferdate AS DATE) AS tx_date, SUM(e.total)
                FROM ExpenseItem e
                JOIN Expense ex ON ex.ID = e.expense
                WHERE ex.moneyType = :V_CURR_ID
                GROUP BY 1
                ORDER BY 1 ASC
                INTO :V_TX_DATE, :V_DAILY_TOTAL
            DO
            BEGIN
               -- Reset rate variables so a missing historical rate falls back to 1 instead of reusing the previous day's rate
                V_RATE_SRC = NULL;
                V_RATE_DEST = NULL;

                V_SUM_TOTAL = :V_SUM_TOTAL + :V_DAILY_TOTAL;

                IF (:DEST_CODE = 'RUB') THEN
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
        END

        CODE = :V_CURR_CODE;
        TOTAL = ROUND(COALESCE(:V_SUM_TOTAL, 0), 2);
        TOTALCONVERTED = ROUND(COALESCE(:V_SUM_CONV, 0), 2);
        SUSPEND;
    END
END ^
