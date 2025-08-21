<template>
	<div class="custom-collapse">
		<div class="header" @click="toggle">
			<div class="left">
				{{ props.dayOfMonth }}&nbsp;{{
					monthsName[props.month - 1]
				}}&nbsp;{{ props.year }}
			</div>
			<div class="right">
				&#8377;&nbsp;{{ props.amount }}
				<el-button
					:loading="isLoading"
					:icon="isOpen ? Minus : Plus"
					type="primary"
					plain
					style="margin-left: 10px"
					:disabled="isLoading || disabled"
					size="small" />
			</div>
		</div>
		<transition name="collapse">
			<div class="content" v-show="isOpen && !isLoading">
				<DailyExpenseTable :expenses="expenses" />
			</div>
		</transition>
	</div>
</template>

<script setup>
	import { ref, toRef } from "vue";
	import { Plus, Minus } from "@element-plus/icons-vue";
	import DailyExpenseTable from "@/components/DailyExpenseTable.vue";
	import * as api from "@/api/api";
	import { useGroupStore } from "@/stores/group";

	const group = useGroupStore();

	const props = defineProps({
		year: { type: Number },
		month: { type: Number },
		dayOfMonth: { type: Number },
		amount: { type: Number },
		disabled: { type: Boolean },
		categoryId: [String, Number]
	});

	const monthsName = [
		"Jan",
		"Feb",
		"Mar",
		"Apr",
		"May",
		"Jun",
		"Jul",
		"Aug",
		"Sep",
		"Oct",
		"Nov",
		"Dec",
	];

	const expenses = ref([]);
	const isOpen = ref(false);
	const isLoading = ref(false);
	const isFirstTime = ref(true);
	const disabled = toRef(props, "disabled");
	async function toggle() {
		if (isLoading.value) {
			return;
		}
		if (isFirstTime.value) {
			isFirstTime.value = false;
			isLoading.value = true;
			await loadData();
			isLoading.value = false;
		}
		isOpen.value = !isOpen.value;
	}

	async function loadData() {
		try {
			expenses.value = (
				await api.getExpenses(
					group.groupId,
					props.year,
					props.month,
					props.dayOfMonth,
					props.categoryId
				)
			).data;
		} catch (error) {
			console.log(error);
		}
	}
</script>

<style scoped>
	.custom-collapse .header {
		display: flex;
		align-items: center;
		cursor: pointer;
		background-color: var(--el-bg-color);
		padding: 10px;
		color: var(--el-text-color-primary);
		border-bottom: 1px solid var(--el-border-color);
	}

	.custom-collapse .left,
	.custom-collapse .center,
	.custom-collapse .right {
		display: flex;
		align-items: center;
	}

	.custom-collapse .center {
		flex: 1;
	}

	.custom-collapse .right {
		margin-left: auto;
	}

	.custom-collapse .content {
		overflow: hidden;
		border: 10px solid var(--el-bg-color-page);
		background-color: var(--el-bg-color-page);
		color: var(--el-text-color-regular);
	}

	.collapse-enter-active,
	.collapse-leave-active {
		transition:
			max-height 0.3s ease,
			opacity 0.3s ease;
	}

	.collapse-enter-from,
	.collapse-leave-to {
		max-height: 0;
		opacity: 0;
	}
</style>
