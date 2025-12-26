CREATE TABLE IF NOT EXISTS recurring_expenses (
	groupId bigint(20) NOT NULL,
	recurringId  bigint(20) NOT NULL,
	categoryId bigint(20) NOT NULL,
	amount  bigint(20) NOT NULL,
	description VARCHAR(200) DEFAULT NULL,
	frequency VARCHAR(20) NOT NULL,
	dayOfWeek CHAR(3),
	dayOfMonth TINYINT,
	executionTimeHour TINYINT NOT NULL,
	executionTimeMinute TINYINT NOT NULL,
	nextTriggerEpoch BIGINT NOT NULL DEFAULT 0
);