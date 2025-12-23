CREATE TABLE IF NOT EXISTS `users` (
	`userId` varchar(255) PRIMARY KEY,
	`name` varchar(255),
	`pictureUrl` varchar(255)
);

CREATE TABLE IF NOT EXISTS `groups` (
	`groupId` bigint(20) PRIMARY KEY,
	`groupName` varchar(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS `groupUsers` (
	`userId` varchar(255) NOT NULL,
	`groupId` bigint(20) NOT NULL,
	`role` enum('ADMIN','MEMBER') DEFAULT NULL,
	PRIMARY KEY (`groupId`,`userId`)
);

CREATE TABLE IF NOT EXISTS `groupCategories` (
	`groupId` bigint(20) NOT NULL,
	`categoryId` bigint(20) NOT NULL,
	`categoryName` varchar(100) DEFAULT NULL,
	`orderNumber` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`categoryId`,`groupId`)
);

CREATE TABLE IF NOT EXISTS `groupTags` (
	`tagId` bigint(20) NOT NULL,
	`groupId` bigint(20) NOT NULL,
	`orderNumber` bigint(20) DEFAULT NULL,
	`tagName` varchar(255) DEFAULT NULL,
	PRIMARY KEY (`groupId`,`tagId`)
);

CREATE TABLE IF NOT EXISTS `expenses` (
	`groupId` bigint(20) NOT NULL,
	`expenseId` bigint(20) NOT NULL,
	`amount` bigint(20) NOT NULL,
	`categoryId` bigint(20) NOT NULL,
	`dayOfMonth` tinyint(4) NOT NULL,
	`description` varchar(200) DEFAULT NULL,
	`lastChangedByUserId` varchar(254) DEFAULT NULL,
	`month` tinyint(4) NOT NULL,
	`ownerUserId` varchar(254) DEFAULT NULL,
	`timestamp` bigint(20) NOT NULL,
	`year` smallint(6) NOT NULL,
	PRIMARY KEY (`expenseId`,`groupId`)
);

CREATE TABLE IF NOT EXISTS `expenseTags` (
	`groupId` bigint(20),
	`expenseId` bigint(20),
	`tagId` bigint(20),
	PRIMARY KEY (`groupId`,`expenseId`, `tagId`)
);

CREATE TABLE IF NOT EXISTS `groupInvites` (
	`groupId` bigint(20) NOT NULL,
	`inviteId` bigint(20) NOT NULL,
	`invitedUserId` varchar(255),
	`invitedEmail` varchar(255) NOT NULL,
	`inviterUserId` varchar(255),
	`status` VARCHAR(20) NOT NULL,
	createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	expiresAt TIMESTAMP NOT NULL,
	consumedAt TIMESTAMP,
	PRIMARY KEY (`groupId`,`inviteId`)
);