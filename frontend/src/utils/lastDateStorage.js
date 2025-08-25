// Keep last visited expenses page date + groupId in memory (not persistent)
let lastExpensesDate;
let lastExpensesGroupId;

export function setLastExpensesDate(date, groupId) {
	lastExpensesDate = date;
	lastExpensesGroupId = groupId;
}

export function getLastExpensesDate() {
	return { date: lastExpensesDate, groupId: lastExpensesGroupId };
}

export function clearLastExpensesDate() {
	lastExpensesDate = null;
	lastExpensesGroupId = null;
}
