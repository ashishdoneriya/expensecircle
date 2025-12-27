<template>
	<el-row justify="center" style="margin-top: 30px; margin-bottom: 100px">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9" v-loading="isLoading">
			<el-table
				v-if="!isMobile"
				:data="recurringExpenses"
				:header-cell-class-name="cellStyle"
				table-layout="auto"
				:cell-class-name="cellStyle"
				v-loading="isLoading"
				row-class-name="cursorPointer"
				@row-click="openRecurringExpense">
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
				<el-table-column prop="frequency" label="Frequency" align="center">
					<template #default="scope">
						{{ formatSentenceCase(scope.row.frequency) }}
					</template>
				</el-table-column>
			</el-table>
			<el-row
				v-else
				v-for="obj in recurringExpenses"
				:key="obj.recurringId"
				@click="openRecurringExpense(obj)"
				justify="center"
				class="cursorPointer">
				<el-col :span="24">
					<el-link
						:underline="false"
						style="font-size: var(--el-font-size-large); font-weight: 400">
						{{ obj.description }} &nbsp;&#8212;&nbsp;&#8377;&nbsp;
						{{ obj.amount }}/{{ frequencyMap[obj.frequency] }}
					</el-link>
				</el-col>
				<el-col :span="24">
					<el-divider border-style="dashed" style="margin: 5px 0px" />
				</el-col>
			</el-row>
		</el-col>
	</el-row>

	<el-button
		type="primary"
		class="fab"
		circle
		icon="Plus"
		size="large"
		@click="addRecurringExpense()"></el-button>
</template>
<script setup>
	import { ref } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import { useGroupStore } from "@/stores/group";
	import * as api from "@/api/api";

	const isMobile = window.innerWidth <= 720;

	const route = useRoute();
	const router = useRouter();

	const recurringExpenses = ref([]);
	const group = useGroupStore();
	const isLoading = ref(true);
	const frequencyMap = {
		MONTHLY: "Month",
		WEEKLY: "Week",
		DAILY: "Day",
	};

	onMounted(async () => {
		group.initialize(route.params.groupId);
		try {
			recurringExpenses.value = (
				await api.getAllRecurringExpenses(group.groupId)
			).data;
		} catch (error) {
			console.log(error);
			ElMessage({
				message: "Unable to load data, please try after some time",
				type: "error",
				offset: window.innerHeight - 100,
			});
		}
		isLoading.value = false;
	});

	function openRecurringExpense(recurring) {
		router.push({
			path: `/groups/${group.groupId}/edit-recurring-expense/${recurring.recurringId}`,
		});
	}

	function addRecurringExpense() {
		router.push({
			path: `/groups/${group.groupId}/new-recurring-expense`,
		});
	}

	function cellStyle(test) {
		if (test.columnIndex == 0) {
			return "col1";
		}
		if (test.columnIndex == 1) {
			return "col2";
		}
		return "col3";
	}

	function formatSentenceCase(text) {
		if (!text) return text;
		return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
	}
</script>

<style>
	.col1 {
		text-align: center;
		white-space: nowrap;
	}
	.mobile-expense-card {
		cursor: pointer;
		margin-bottom: 12px;
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
