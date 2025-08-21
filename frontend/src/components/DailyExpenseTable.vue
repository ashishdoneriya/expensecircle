<template>
	<el-table
		:data="expenses"
		:header-cell-class-name="cellStyle"
		table-layout="auto"
		:cell-class-name="cellStyle"
		v-loading="disabled"
		row-class-name="cursorPointer"
		@row-click="openExpense">
		<el-table-column label="Category" align="center">
			<template #default="scope">
				<el-tag v-if="scope.row.categoryId != 0" type="primary">
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
	import { useGroupStore } from "@/stores/group";
	import { useRoute, useRouter } from "vue-router";
	const route = useRoute();
	const router = useRouter();

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
		cursor:pointer;
	}
</style>
