<template>
	<el-row class="flex-container sticky">
		<div>
			<el-select
				v-model="intervalType"
				:disabled="disableAll"
				:loading="disableAll"
				placeholder="Select"
				:size="isMobile ? 'small' : 'default'"
				:style="isMobile ? 'width: 80px' : 'width:100px'"
				@change="refresh()">
				<el-option value="Daily" />
				<el-option value="Monthly" />
			</el-select>
		</div>
		<div
			style="
				display: flex;
				justify-content: center;
				align-items: center;
				position: absolute;
				left: 50%;
				transform: translateX(-50%);
				top: 50%;
				transform: translate(-50%, -50%);
			">
			<el-icon
				class="cursorPointer"
				@click="loadPrevious"
				v-if="!disableAll">
				<ArrowLeftBold />
			</el-icon>
			<el-icon class="is-loading" v-if="disableAll">
				<Loading />
			</el-icon>
			<label
				v-if="isMobile"
				@click="openDatePicker"
				class="cursorPointer"
				style="
					font-weight: bold;
					margin-right: 10px;
					margin-left: 10px;
				">
				{{ formattedDate }}
			</label>
			<el-date-picker
				ref="datePicker"
				:class="isMobile ? 'hidden-date-picker' : ''"
				v-model="datePickerDate"
				:type="intervalType == 'Daily' ? 'date' : 'month'"
				placeholder="Select Date"
				:format="intervalType == 'Daily' ? 'DD MMM YYYY' : 'MMM YYYY'"
				:clearable="false"
				:disabled="disableAll"
				:loading="disableAll"
				:style="
					isMobile
						? 'cursor:pointer'
						: 'width: 130px;margin-right: 10px;margin-left: 10px;cursor:pointer;'
				"
				@change="refresh"
				:editable="false" />

			<el-icon class="cursorPointer" @click="loadNext" v-if="!disableAll">
				<ArrowRightBold />
			</el-icon>
			<el-icon class="is-loading" v-if="disableAll">
				<Loading />
			</el-icon>
		</div>
		<div>
			<el-select
				v-model="selectedCategoryId"
				@change="refresh"
				clearable
				:placeholder="!isMobile ? 'Filter Category' : 'Filter'"
				:disabled="disableAll"
				:loading="disableAll"
				:style="!isMobile ? 'width: 140px' : ''"
				:fallback-placements="['bottom-start']"
				suffix-icon="Filter">
				<template #header v-if="isMobile">
					<span v-if="selectedCategoryId" @click="selectedCategoryId = ''">
						Clear Filter
					</span>
					<span v-else>Select Category</span>
				</template>
				<el-option
					v-for="category in group.categories"
					:label="category.categoryName"
					:value="category.categoryId" />
			</el-select>
		</div>
	</el-row>
	<el-row style="padding: 0px; margin: 0px">
		<el-col>
			<el-divider style="margin: 10px 0px"></el-divider>
		</el-col>
	</el-row>
	<el-row justify="center" v-if="intervalType == 'Daily'">
		<el-col
			:xs="24"
			:sm="22"
			:md="15"
			:lg="12"
			:xl="9"
			style="text-align: center">
			<div
				style="
					display: flex;
					justify-content: space-around;
					align-items: center;
				">
				<el-statistic title="This Month's Expenses" :value="monthlyTotal">
					<template #prefix>&#8377;</template>
				</el-statistic>
				<el-statistic title="Today's Expenses" :value="dailyTotal">
					<template #prefix>&#8377;</template>
				</el-statistic>
			</div>
		</el-col>
	</el-row>
	<el-row justify="center" v-else>
		<el-col
			:xs="24"
			:sm="22"
			:md="15"
			:lg="12"
			:xl="9"
			style="text-align: center">
			<div
				style="
					display: flex;
					justify-content: space-around;
					align-items: center;
				">
				<el-statistic title="This Month's Expenses" :value="monthlyTotal">
					<template #prefix>&#8377;</template>
				</el-statistic>
				<el-statistic
					title="Daily Average Expenses"
					:value="dailyAverageExpenses">
					<template #prefix>&#8377;</template>
				</el-statistic>
			</div>
		</el-col>
	</el-row>
	<el-row
		v-if="intervalType == 'Daily'"
		justify="center"
		style="margin-top: 30px">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<DailyExpenseTable :expenses="dailyExpenses" :disabled="disableAll" />
		</el-col>
	</el-row>
	<el-row
		v-else
		justify="center"
		style="margin-top: 30px; margin-bottom: 100px">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<DailyExpenseTableCollapsable
				v-for="daily in monthlyBreakDown"
				:key="
					daily.dayOfMonth +
					'/' +
					month +
					'/' +
					year +
					'/' +
					selectedCategoryId
				"
				:year="year"
				:month="month"
				:day-of-month="daily.dayOfMonth"
				:amount="daily.amount"
				:category-id="selectedCategoryId"
				:disabled="disableAll" />
		</el-col>
	</el-row>
	<el-button
		type="primary"
		class="fab"
		circle
		icon="Plus"
		size="large"
		@click="addExpense()"></el-button>
</template>

<script setup>
	import { ref, onMounted } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import { useGroupStore } from "@/stores/group";
	import * as api from "@/api/api";
	import DailyExpenseTable from "@/components/DailyExpenseTable.vue";
	import DailyExpenseTableCollapsable from "@/components/DailyExpenseTableCollapsable.vue";
	import { setLastExpensesDate } from "@/utils/lastDateStorage";

	const isMobile = window.innerWidth <= 720;
	const disableAll = ref(true);

	const group = useGroupStore();

	const intervalType = ref("Daily");
	const datePickerDate = ref(new Date());
	const year = computed(() => datePickerDate.value.getFullYear());
	const month = computed(() => datePickerDate.value.getMonth() + 1);
	const dayOfMonth = computed(() => datePickerDate.value.getDate());
	const selectedCategoryId = ref("");
	const dailyExpenses = ref([]);
	const route = useRoute();
	const router = useRouter();
	const datePicker = ref(null);

	const formattedDate = computed(() => {
		if (intervalType.value == "Daily") {
			return datePickerDate.value.toLocaleDateString("en-GB", {
				day: "2-digit",
				month: "short",
				year: "numeric",
			});
		} else {
			return datePickerDate.value.toLocaleDateString("en-GB", {
				month: "short",
				year: "numeric",
			});
		}
	});
	function openDatePicker() {
		if (disableAll.value) {
			return;
		}
		datePicker.value.handleOpen();
	}
	async function loadPrevious() {
		if (disableAll.value) {
			return;
		}
		if (intervalType.value == "Daily") {
			const newDate = new Date(datePickerDate.value);
			newDate.setDate(newDate.getDate() - 1);
			datePickerDate.value = newDate;
			await refresh();
		} else {
			const newDate = new Date(datePickerDate.value);
			newDate.setMonth(newDate.getMonth() - 1);
			datePickerDate.value = newDate;
			await refresh();
		}
	}
	async function loadNext() {
		if (disableAll.value) {
			return;
		}
		if (intervalType.value == "Daily") {
			const newDate = new Date(datePickerDate.value);
			newDate.setDate(newDate.getDate() + 1);
			datePickerDate.value = newDate;
			await refresh();
		} else {
			const newDate = new Date(datePickerDate.value);
			newDate.setMonth(newDate.getMonth() + 1);
			datePickerDate.value = newDate;
			await refresh();
		}
	}

	const dailyTotal = ref(0);
	const monthlyTotal = ref(0);
	onMounted(async () => {
		group.initialize(route.params.groupId);
		readQueryParams();
		await refresh();
	});

	async function readQueryParams() {
		if (
			route.query["interval"] &&
			(route.query["interval"] == "Daily" ||
				route.query["interval"] == "Monthly")
		) {
			intervalType.value = route.query["interval"];
		}
		if (route.query["date"] && isValidDateFormat(route.query["date"])) {
			datePickerDate.value = convertToDate(route.query["date"]);
		}
		if (route.query["category"]) {
			selectedCategoryId.value = Number(route.query["category"]);
		}
	}

	async function writeQueryParams() {
		if (selectedCategoryId.value) {
			router.push({
				query: {
					interval: intervalType.value,
					date: formatDate(datePickerDate.value),
					category: selectedCategoryId.value,
				},
			});
		} else {
			router.push({
				query: {
					interval: intervalType.value,
					date: formatDate(datePickerDate.value),
				},
			});
		}

		setLastExpensesDate(route.query.date || null, group.groupId);
	}

	function isValidDateFormat(dateStr) {
		// Regular expression for yyyy-MM-dd format
		const regex = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;
		if (!regex.test(dateStr)) return false; // Format invalid

		// Parse the date to verify it's real
		const [year, month, day] = dateStr.split("-").map(Number);
		const date = new Date(year, month - 1, day);

		// Check if the parsed date components match (avoiding invalid dates like 2024-02-30)
		return (
			date.getFullYear() === year &&
			date.getMonth() === month - 1 &&
			date.getDate() === day
		);
	}

	function convertToDate(dateStr) {
		// Ensure the format is valid before converting
		if (!/^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/.test(dateStr)) {
			throw new Error("Invalid date format. Use yyyy-MM-dd.");
		}

		const [year, month, day] = dateStr.split("-").map(Number);
		return new Date(year, month - 1, day); // Month is 0-based in JS
	}

	function formatDate(date) {
		const yyyy = date.getFullYear();
		const mm = String(date.getMonth() + 1).padStart(2, "0"); // Month is 0-based
		const dd = String(date.getDate()).padStart(2, "0");
		return `${yyyy}-${mm}-${dd}`;
	}

	async function refresh() {
		writeQueryParams();
		if (intervalType.value == "Daily") {
			await fetchDailyExpenses();
		} else {
			await fetchMonthExpenses();
		}
	}

	/* Monthly Functions */
	const monthlyBreakDown = ref({});
	const dailyAverageExpenses = computed(() => {
		let totalDaysInAMonth = new Date(
			year.value,
			month.value + 1,
			0,
		).getDate();
		return monthlyTotal.value / totalDaysInAMonth;
	});

	/* Daily Expenses */
	async function fetchDailyExpenses() {
		disableAll.value = true;

		const promise1 = api
			.getMonthlyTotal(
				group.groupId,
				year.value,
				month.value,
				selectedCategoryId.value,
			)
			.then((data) => {
				monthlyTotal.value = data.data;
			})
			.catch((error) => {
				console.log(error);
			});
		const promise2 = api
			.getExpenses(
				group.groupId,
				year.value,
				month.value,
				dayOfMonth.value,
				selectedCategoryId.value,
			)
			.then((data) => {
				dailyExpenses.value = data.data;
				let total = 0;
				for (let expense of dailyExpenses.value) {
					total += expense.amount;
				}
				dailyTotal.value = total;
			})
			.catch((error) => {
				console.log(error);
			});
		Promise.allSettled([promise1, promise2]).then(() => {
			disableAll.value = false;
		});
	}

	async function fetchMonthExpenses() {
		disableAll.value = true;
		try {
			let data = await api.getMonthlyBreakdownByDay(
				group.groupId,
				year.value,
				month.value,
				selectedCategoryId.value,
			);
			monthlyBreakDown.value = data.data;
			let total = 0;
			for (let breakdown of monthlyBreakDown.value) {
				total += breakdown.amount;
			}
			monthlyTotal.value = total;
		} catch (error) {
			console.log(error);
		}
		disableAll.value = false;
	}

	function addExpense() {
		router.push({
			path: `/groups/${group.groupId}/expense`,
			query: { timestamp: datePickerDate.value.getTime() },
		});
	}
</script>

<style>
	.fab {
		position: fixed;
		bottom: 90px;
		right: 30px;
		z-index: 999999;
		padding: 25px !important;
	}
	.fab i {
		font-size: 25px;
	}

	@media only screen and (max-width: 720px) {
		.fab {
			bottom: 70px;
			right: 10px;
		}
	}

	.flex-container {
		display: flex;
		align-items: center;
	}
	.hidden-date-picker {
		visibility: hidden;
		width: 1px;
		height: 10px;
		position: absolute;
	}
	.sticky {
		position: sticky;
		top: 0; /* Distance from the top of the viewport */
		z-index: 10; /* Optional: Ensures it stays above other content */
	}
</style>
