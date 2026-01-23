ALTER table `expenses` ADD COLUMN timezone VARCHAR(35);
ALTER table `recurringExpenses` ADD COLUMN timezone VARCHAR(35);
update expenses set timezone='Asia/Kolkata';
update recurringExpenses set timezone='Asia/Kolkata';
update recurringExpenses set nextTriggerEpoch=0;
update expenses set createdBy = '9cMc4kZZNQ' where createdBy IS NULL;
update expenses set createdAt = `timestamp` where createdAt is NULL;