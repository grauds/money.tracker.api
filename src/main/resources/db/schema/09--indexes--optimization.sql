-- Speeds up filtering by currency and chronological grouping in GET_INCOME_BY_CURRENCY
CREATE INDEX IDX_INCOME_TYPE_DATE ON Income (moneyType, transferdate) ^

-- Speeds up filtering by currency and chronological grouping in GET_EXPENSE_BY_CURRENCY
CREATE INDEX IDX_EXPENSE_TYPE_DATE ON Expense (moneyType, transferdate) ^

-- Speeds up both the EXISTS check and the WHERE e.commontradeplace = :V_ORG_ID grouping in GET_INCOME_BY_COMMON_TRADE_PLACE
CREATE INDEX IDX_INCOME_CTP_TYPE_DATE ON Income (commontradeplace, moneyType, transferdate) ^

-- Finds all commodities belonging to a COMMGROUP
CREATE INDEX IDX_COMMODITY_PARENT ON COMMODITY (PARENT, ID) ^

-- Finds all ExpenseItems for each commodity and covers moneyType + transferdate
CREATE INDEX IDX_EXPITEM_COMM_TYPE_DATE ON ExpenseItem (comm, moneyType, transferdate) ^
