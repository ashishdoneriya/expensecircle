ALTER TABLE `expenses` RENAME COLUMN ownerUserId TO createdBy;
ALTER TABLE `expenses` RENAME COLUMN lastChangedByUserId TO updatedBy;
ALTER TABLE `expenses` ADD COLUMN createdAt bigint(20);
ALTER TABLE `expenses` ADD COLUMN updatedAt bigint(20);
UPDATE `expenses` set createdAt = timestamp;
UPDATE `expenses` set updatedAt = timestamp where updatedBy is not null;

CREATE TABLE IF NOT EXISTS recurringExpenses (
	`groupId` char(11) NOT NULL,
	`recurringId` char(11) NOT NULL,
	`categoryId` char(11) NOT NULL,
	`amount`  bigint(20) NOT NULL,
	`description` VARCHAR(200) DEFAULT NULL,
	`frequency` VARCHAR(20) NOT NULL,
	`dayOfWeek` CHAR(3),
	`dayOfMonth` TINYINT,
	`executionTimeHour` TINYINT NOT NULL,
	`executionTimeMinute` TINYINT NOT NULL,
	`dayPeriod` CHAR(2) NOT NULL,
	`nextTriggerEpoch` BIGINT NOT NULL DEFAULT 0,
	`createdBy` char(11),
	`updatedBy` char(11),
	`createdAt` bigint(20),
	`updatedAt` bigint(20)
);

CREATE TABLE IF NOT EXISTS `recurringExpenseTags` (
	`groupId` char(11),
	`recurringId` char(11),
	`tagId` char(11),
	PRIMARY KEY (`groupId`,`recurringId`, `tagId`)
);


