<template>
	<el-table
		:data="sortedExpenses"
		:header-cell-class-name="cellStyle"
		table-layout="auto"
		:cell-class-name="cellStyle"
		v-loading="disabled"
		row-class-name="cursorPointer"
		@row-click="openExpense">
		<el-table-column label="Category" align="center">
			<template #default="scope">
				<el-tag v-if="scope.row.categoryId" type="primary">
					{{ group.categoriesMap[scope.row.categoryId] }}
				</el-tag>
			</template>
		</el-table-column>
		<el-table-column prop="description" label="Description" />
		<el-table-column prop="amount" label="Amount" align="right">
			<template #default="scope">
				<span>&#8377;&nbsp;{{ scope.row.amount }}</span>
			</template>
		</el-table-column>
	</el-table>
</template>

<script setup>
	import { useRoute, useRouter } from "vue-router";
	import { useGroupStore } from "@/stores/group";
	import { usePreferenceStore } from "@/stores/preferenceStore";

	const route = useRoute();
	const router = useRouter();
	const preferenceStore = usePreferenceStore();
	const group = useGroupStore();
	const props = defineProps({
		expenses: {
			default: () => [],
		},
		disabled: {
			default: () => false,
		},
	});

	const expenses = toRef(props, "expenses");
	const disabled = toRef(props, "disabled");

	function cellStyle(test) {
		if (test.columnIndex == 0) {
			return "col1";
		}
		if (test.columnIndex == 1) {
			return "col2";
		}
		return "col3";
	}

	function openExpense(expense) {
		router.push(`/groups/${group.groupId}/expense/${expense.expenseId}`);
	}

	// This computed property will watch both 'expenses' and 'preferenceStore.sortOrder'
	const sortedExpenses = computed(() => {
		const order = preferenceStore.sortOrder;

		// Create a shallow copy using the spread operator [...] to avoid mutating the original prop
		return expenses.value.sort((obj1, obj2) => {
			if (order === "DateNewestToOldest") {
				return obj2.timestamp - obj1.timestamp;
			} else if (order === "AmountLowToHigh") {
				if (obj1.amount === obj2.amount) {
					return obj1.timestamp - obj2.timestamp;
				}
				return obj1.amount - obj2.amount;
			} else if (order === "AmountHighToLow") {
				if (obj1.amount === obj2.amount) {
					return obj2.timestamp - obj1.timestamp;
				}
				return obj2.amount - obj1.amount;
			} else {
				// Default: Date Oldest to Newest (or ID based)
				return obj1.timestamp - obj2.timestamp;
			}
		});
	});
</script>

<style>
	.col1 {
		text-align: center;
		white-space: nowrap;
	}

	.col2 {
		text-align: left;
		width: 100%;
	}

	.col3 {
		text-align: right;
		white-space: nowrap;
	}
	.cursorPointer {
		cursor: pointer;
	}
</style>
