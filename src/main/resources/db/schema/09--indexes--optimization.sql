-- Speeds up filtering by currency and chronological grouping in GET_INCOME_BY_CURRENCY
CREATE INDEX IDX_INCOME_TYPE_DATE ON Income (moneyType, transferdate) ^

-- Speeds up filtering by currency and chronological grouping in GET_EXPENSE_BY_CURRENCY
CREATE INDEX IDX_EXPENSE_TYPE_DATE ON Expense (moneyType, transferdate) ^
