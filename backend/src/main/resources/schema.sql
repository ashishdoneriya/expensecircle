CREATE TABLE IF NOT EXISTS `users` (
	`userId` char(11) PRIMARY KEY,
	`email` varchar(255),
	`name` varchar(255),
	`pictureUrl` varchar(255)
);

CREATE TABLE IF NOT EXISTS `groups` (
	`groupId` char(11) PRIMARY KEY,
	`groupName` varchar(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS `groupUsers` (
	`userId` char(11) NOT NULL,
	`groupId` char(11) NOT NULL,
	`role` enum('ADMIN','MEMBER') DEFAULT NULL,
	PRIMARY KEY (`groupId`,`userId`)
);

CREATE TABLE IF NOT EXISTS `groupCategories` (
	`groupId` char(11) NOT NULL,
	`categoryId` char(11) NOT NULL,
	`categoryName` varchar(100) DEFAULT NULL,
	`orderNumber` bigint(20) DEFAULT NULL,
	PRIMARY KEY (`categoryId`,`groupId`)
);

CREATE TABLE IF NOT EXISTS `groupTags` (
	 `tagId` char(11) NOT NULL,
	 `groupId` char(11) NOT NULL,
	 `orderNumber` bigint(20) DEFAULT NULL,
	 `tagName` varchar(255) DEFAULT NULL,
	 PRIMARY KEY (`groupId`,`tagId`)
);

CREATE TABLE IF NOT EXISTS `expenses` (
	`groupId` char(11) NOT NULL,
	`expenseId` char(11) NOT NULL,
	`amount` bigint(20) NOT NULL,
	`categoryId` char(11) NOT NULL,
	`dayOfMonth` tinyint(4) NOT NULL,
	`description` varchar(200),
	`lastChangedByUserId` varchar(11),
	`month` tinyint(4) NOT NULL,
	`createdBy` char(11),
	`updatedBy` char(11),
	`createdAt` bigint(20),
	`updatedAt` bigint(20),
	`timestamp` bigint(20) NOT NULL,
	`year` smallint(6) NOT NULL,
	PRIMARY KEY (`expenseId`,`groupId`)
);

CREATE TABLE IF NOT EXISTS `expenseTags` (
	`groupId` char(11),
	`expenseId` char(11),
	`tagId` char(11),
	PRIMARY KEY (`groupId`,`expenseId`, `tagId`)
);

CREATE TABLE IF NOT EXISTS `groupInvites` (
	`groupId` char(11) NOT NULL,
	`inviteId` char(11) NOT NULL,
	`invitedUserId` char(11),
	`invitedEmail` varchar(255),
	`inviterUserId` char(11),
	`status` VARCHAR(20) NOT NULL,
	`createdAt` bigint(20) NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`expiresAt` bigint(20) NOT NULL,
	`consumedAt` bigint(20),
	PRIMARY KEY (`groupId`,`inviteId`)
);

CREATE TABLE IF NOT EXISTS `recurringExpenses` (
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
	`modifiedBy` char(11),
	`createdAt` bigint(20),
	`updatedAt` bigint(20)
);

CREATE TABLE IF NOT EXISTS `recurringExpenseTags` (
	`groupId` char(11),
	`recurringId` char(11),
	`tagId` char(11),
	PRIMARY KEY (`groupId`,`recurringId`, `tagId`)
);
