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

CREATE TABLE `groupCategories` (
	`groupId` bigint(20) NOT NULL,
	`categoryId` bigint(20) NOT NULL,
	`categoryName` varchar(100) DEFAULT NULL,
	`orderNumber` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`categoryId`,`groupId`)
);

CREATE TABLE `groupTags` (
	`tagId` bigint(20) NOT NULL,
	`groupId` bigint(20) NOT NULL,
	`orderNumber` bigint(20) DEFAULT NULL,
	`tagName` varchar(255) DEFAULT NULL,
	PRIMARY KEY (`groupId`,`tagId`)
);

CREATE TABLE `expenses` (
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

CREATE TABLE `expenseTags` (
	`groupId` bigint(20),
	`expenseId` bigint(20),
	`tagId` bigint(20),
	PRIMARY KEY (`groupId`,`expenseId`, `tagId`)
);