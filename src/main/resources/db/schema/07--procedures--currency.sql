-- Cross-rate calculation (legacy version)
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

-- Optimized cross-rate calculation using composite index
CREATE OR ALTER PROCEDURE CROSS_RATE_2 (
    SRC VARCHAR(16),
    DEST VARCHAR(16),
    RATE_DATE DATE = CURRENT_DATE
)
RETURNS (
    RATE DOUBLE PRECISION
)
AS
DECLARE VARIABLE V_RUB_ID INTEGER;
DECLARE VARIABLE V_SRC_ID INTEGER;
DECLARE VARIABLE V_DEST_ID INTEGER;
DECLARE VARIABLE V_RATE_SRC DOUBLE PRECISION;
DECLARE VARIABLE V_RATE_DEST DOUBLE PRECISION;
BEGIN
    RATE = 1;

    -- 1. Fast-path exit: identical currencies or NULL inputs require no table lookups
    IF (:SRC = :DEST OR :SRC IS NULL OR :DEST IS NULL) THEN
    BEGIN
        SUSPEND;
        EXIT;
    END

    -- 2. Resolve all required currency IDs in a single table scan instead of 3 separate queries
    SELECT
        MAX(CASE WHEN CODE = 'RUB' THEN ID END),
        MAX(CASE WHEN CODE = :SRC  THEN ID END),
        MAX(CASE WHEN CODE = :DEST THEN ID END)
    FROM MONEYTYPE
    WHERE CODE IN ('RUB', :SRC, :DEST)
    INTO :V_RUB_ID, :V_SRC_ID, :V_DEST_ID;

    -- 3. Direct conversion to RUB (SRC -> RUB)
    -- NOTE: All 3 columns in ORDER BY must be DESC so Firebird uses composite index IDX_MTR_PAIR_DATE_DESC
    IF (:DEST = 'RUB') THEN
    BEGIN
        SELECT FIRST 1 mtr.RATE
        FROM MONEYTYPERATE mtr
        WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
          AND mtr.SELLMONEYTYPE = :V_SRC_ID
          AND mtr.RATEDATE < :RATE_DATE
        ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
        INTO :V_RATE_SRC;

        IF (:V_RATE_SRC IS NOT NULL) THEN
            RATE = ROUND(:V_RATE_SRC, 15);
    END
    -- 4. Inverse conversion from RUB (RUB -> DEST)
    ELSE IF (:SRC = 'RUB') THEN
    BEGIN
        SELECT FIRST 1 mtr.RATE
        FROM MONEYTYPERATE mtr
        WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
          AND mtr.SELLMONEYTYPE = :V_DEST_ID
          AND mtr.RATEDATE < :RATE_DATE
        ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
        INTO :V_RATE_DEST;

        IF (:V_RATE_DEST IS NOT NULL AND :V_RATE_DEST <> 0) THEN
            RATE = ROUND(1.0 / :V_RATE_DEST, 15);
    END
    -- 5. Cross-rate via RUB (SRC -> RUB -> DEST) using two O(1) index seeks instead of a self-JOIN
    ELSE
    BEGIN
        SELECT FIRST 1 mtr.RATE
        FROM MONEYTYPERATE mtr
        WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
          AND mtr.SELLMONEYTYPE = :V_SRC_ID
          AND mtr.RATEDATE < :RATE_DATE
        ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
        INTO :V_RATE_SRC;

        SELECT FIRST 1 mtr.RATE
        FROM MONEYTYPERATE mtr
        WHERE mtr.BUYMONEYTYPE = :V_RUB_ID
          AND mtr.SELLMONEYTYPE = :V_DEST_ID
          AND mtr.RATEDATE < :RATE_DATE
        ORDER BY mtr.BUYMONEYTYPE DESC, mtr.SELLMONEYTYPE DESC, mtr.RATEDATE DESC
        INTO :V_RATE_DEST;

        IF (:V_RATE_SRC IS NOT NULL AND :V_RATE_DEST IS NOT NULL AND :V_RATE_DEST <> 0) THEN
            RATE = ROUND(:V_RATE_SRC / :V_RATE_DEST, 15);
    END

    SUSPEND;
END ^
