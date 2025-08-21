<template>
	<el-row class="flex-container">
		<div>
			<el-select
				v-model="intervalType"
				:disabled="disableAll"
				:loading="disableAll"
				placeholder="Select"
				:size="isMobile ? 'small' : 'default'"
				:style="isMobile ? 'width: 80px' : 'width:100px'"
				@change="refresh()">
				<el-option value="Monthly" />
				<el-option value="Yearly" />
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
			<el-icon style="cursor: pointer" @click="loadPrevious" v-if="!disableAll">
				<ArrowLeftBold />
			</el-icon>
			<el-icon class="is-loading" v-if="disableAll">
				<Loading />
			</el-icon>
			<label
				v-if="isMobile"
				@click="openDatePicker"
				style="
					cursor: pointer;
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
				:type="intervalType == 'Monthly' ? 'month' : 'year'"
				placeholder="Select Date"
				:format="intervalType == 'Monthly' ? 'MMM YYYY' : 'YYYY'"
				:clearable="false"
				:disabled="disableAll"
				:loading="disableAll"
				:style="
					isMobile ? '' : 'width: 130px;margin-right: 10px;margin-left: 10px;'
				"
				@change="refresh"
				:editable="false" />

			<el-icon style="cursor: pointer" @click="loadNext" v-if="!disableAll">
				<ArrowRightBold />
			</el-icon>
			<el-icon class="is-loading" v-if="disableAll">
				<Loading />
			</el-icon>
		</div>
		<div>&#8377;&nbsp; {{ totalAmountFormatted }}</div>
	</el-row>
	<el-row style="padding: 0px; margin: 0px">
		<el-col>
			<el-divider style="margin: 10px 0px"></el-divider>
		</el-col>
	</el-row>
	<el-row style="padding: 0px; margin: 0px" class="carRow">
		<el-col
			class="cardColumn"
			v-if="breakdownByCategory"
			:xs="24"
			:sm="24"
			:md="8"
			:lg="8"
			:xl="8"
			:style="isMobile ? 'padding-bottom:10px' : 'padding-right:10px'">
			<el-card shadown="never">
				<template #header>
					<div class="card-header">
						<el-text tag="b" size="large">Breakdown by Category</el-text>
					</div>
				</template>
				<el-table
					:data="breakdownByCategory"
					table-layout="auto"
					v-loading="disableAll"
					:default-sort="{ prop: 'amount', order: 'descending' }">
					<el-table-column label="Category" sortable align="left">
						<template #default="scope">
							<el-tag
								v-if="scope.row.categoryId != 0"
								type="primary"
								@click="openCategogyExpenses(scope.row.categoryId)"
								class="cursorPointer">
								{{ group.categoriesMap[scope.row.categoryId] }}
							</el-tag>
						</template>
					</el-table-column>
					<el-table-column prop="amount" label="Amount" align="right">
						<template #default="scope">
							<span>&#8377;&nbsp;{{ scope.row.amount }}</span>
						</template>
					</el-table-column>
				</el-table>
			</el-card>
		</el-col>

		<el-col
			class="cardColumn"
			v-if="breakdownByTag"
			:xs="24"
			:sm="24"
			:md="8"
			:lg="8"
			:xl="8"
			:style="isMobile ? 'padding-bottom:10px' : 'padding-right:10px'">
			<el-card shadown="never">
				<template #header>
					<div class="card-header">
						<el-text tag="b" size="large">Breakdown by Tag</el-text>
					</div>
				</template>
				<el-table
					:data="breakdownByTag"
					table-layout="auto"
					v-loading="disableAll"
					:default-sort="{ prop: 'amount', order: 'descending' }">
					<el-table-column label="Tag" sortable align="left">
						<template #default="scope">
							<el-tag type="primary">
								{{ group.tagsMap[scope.row.tagId] }}
							</el-tag>
						</template>
					</el-table-column>
					<el-table-column prop="amount" label="Amount" align="right">
						<template #default="scope">
							<span>&#8377;&nbsp;{{ scope.row.amount }}</span>
						</template>
					</el-table-column>
				</el-table>
			</el-card>
		</el-col>

		<el-col
			v-if="breakdownByMonth && intervalType == 'Yearly'"
			:xs="24"
			:sm="24"
			:md="8"
			:lg="8"
			:xl="8"
			:style="isMobile ? 'padding-bottom:10px' : 'padding-right:10px'">
			<el-card shadown="never">
				<template #header>
					<div class="card-header">
						<el-text tag="b" size="large">Breakdown by Month</el-text>
					</div>
				</template>
				<el-table
					:data="breakdownByMonth"
					table-layout="auto"
					v-loading="disableAll"
					:default-sort="{ prop: 'amount', order: 'descending' }">
					<el-table-column label="Month" sortable align="left">
						<template #default="scope">
							<el-tag v-if="scope.row.categoryId != 0" type="primary">
								{{ monthsName[scope.row.month - 1] }}
							</el-tag>
						</template>
					</el-table-column>
					<el-table-column prop="amount" label="Amount" align="right">
						<template #default="scope">
							<span>&#8377;&nbsp;{{ scope.row.amount }}</span>
						</template>
					</el-table-column>
				</el-table>
			</el-card>
		</el-col>
	</el-row>
</template>

<script setup>
	import { ref, onMounted } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import { useGroupStore } from "@/stores/group";
	import * as api from "@/api/api";
	import { getLastExpensesDate } from "@/utils/lastDateStorage";

	const isMobile = window.innerWidth <= 720;
	const disableAll = ref(false);
	const group = useGroupStore();
	const intervalType = ref("Monthly");
	const datePickerDate = ref(new Date());
	const year = computed(() => datePickerDate.value.getFullYear());
	const month = computed(() => datePickerDate.value.getMonth() + 1);
	const datePicker = ref(null);
	const totalAmount = ref(0);
	const router = useRouter();
	const totalAmountFormatted = computed(() =>
		totalAmount.value.toLocaleString("en-IN"),
	);
	const formattedDate = computed(() => {
		if (intervalType.value == "Monthly") {
			return datePickerDate.value.toLocaleDateString("en-GB", {
				month: "short",
				year: "numeric",
			});
		} else {
			return datePickerDate.value.toLocaleDateString("en-GB", {
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
	async function loadPrevious() {
		if (disableAll.value) {
			return;
		}
		if (intervalType.value == "Monthly") {
			const newDate = new Date(datePickerDate.value);
			newDate.setMonth(newDate.getMonth() - 1);
			datePickerDate.value = newDate;
			await refresh();
		} else {
			const newDate = new Date(datePickerDate.value);
			newDate.setFullYear(newDate.getFullYear() - 1);
			datePickerDate.value = newDate;
			await refresh();
		}
	}

	async function loadNext() {
		if (disableAll.value) {
			return;
		}
		if (intervalType.value == "Monthly") {
			const newDate = new Date(datePickerDate.value);
			newDate.setMonth(newDate.getMonth() + 1);
			datePickerDate.value = newDate;
			await refresh();
		} else {
			const newDate = new Date(datePickerDate.value);
			newDate.setFullYear(newDate.getFullYear() + 1);
			datePickerDate.value = newDate;
			await refresh();
		}
	}

	onMounted(async () => {
		const route = useRoute();
		group.initialize(route.params.groupId);
		await refresh();
	});

	async function refresh() {
		if (intervalType.value == "Monthly") {
			await fetchMonthlyStats();
		} else {
			await fetchYearlyStats();
		}
	}

	const breakdownByCategory = ref([]);
	const breakdownByTag = ref([]);
	const breakdownByMonth = ref([]);

	async function fetchMonthlyStats() {
		disableAll.value = true;

		const promise1 = api
			.getMonthlyTotal(group.groupId, year.value, month.value)
			.then((data) => {
				totalAmount.value = data.data;
			})
			.catch((error) => {
				console.error("Error fetching getMonthlyTotal:", error);
			});

		const promise2 = api
			.getMonthlyBreakdownByCategory(group.groupId, year.value, month.value)
			.then((data) => {
				breakdownByCategory.value = data.data;
			})
			.catch((error) => {
				console.error("Error fetching getMonthlyBreakdownByCategory:", error);
			});

		const promise3 = api
			.getMonthlyBreakdownByTag(group.groupId, year.value, month.value)
			.then((data) => {
				breakdownByTag.value = data.data;
			})
			.catch((error) => {
				console.error("Error fetching getMonthlyBreakdownByTag:", error);
			});

		Promise.allSettled([promise1, promise2, promise3]).then(() => {
			disableAll.value = false;
		});
	}

	async function fetchYearlyStats() {
		disableAll.value = true;

		const promise1 = api
			.getYearlyTotal(group.groupId, year.value)
			.then((data) => {
				totalAmount.value = data.data;
			})
			.catch((error) => {
				console.error("Error fetching getYearlyTotal:", error);
			});

		const promise2 = api
			.getYearlyBreakdownByCategory(group.groupId, year.value)
			.then((data) => {
				breakdownByCategory.value = data.data;
			})
			.catch((error) => {
				console.error("Error fetching getYearlyBreakdownByCategory:", error);
			});

		const promise3 = api
			.getYearlyBreakdownByTag(group.groupId, year.value)
			.then((data) => {
				breakdownByTag.value = data.data;
			})
			.catch((error) => {
				console.error("Error fetching getYearlyBreakdownByTag:", error);
			});

		const promise4 = api
			.getYearlyBreakdownByMonth(group.groupId, year.value)
			.then((data) => {
				breakdownByMonth.value = data.data;
			})
			.catch((error) => {
				console.error("Error fetching getYearlyBreakdownByMonth:", error);
			});

		Promise.allSettled([promise1, promise2, promise3, promise4]).then(() => {
			disableAll.value = false;
		});
	}

	function openCategogyExpenses(categoryId) {
		const basePath = `/groups/${group.groupId}/expenses`;

		// fallback = today's day in 2 digits
		const today = new Date();
		let day = String(today.getDate()).padStart(2, "0");

		// cache is always an object from your store
		const cache = getLastExpensesDate();
		if (
			cache.date &&
			cache.groupId &&
			typeof cache.date === "string" &&
			!isNaN(Number(cache.groupId)) &&
			/^\d{4}-\d{2}-\d{2}$/.test(cache.date)
		) {
			const d = cache.date.split("-")[2];
			const prevGroupId = Number(cache.groupId);

			const curGroupId = Number(group.groupId);

			if (prevGroupId === curGroupId) {
				day = d;
			}
		}

		const sDate = `${String(year.value)}-${String(month.value).padStart(2, "0")}-${day}`;

		router.push({
			path: basePath,
			query: {
				interval: "Monthly",
				date: sDate,
				category: categoryId,
			},
		});
	}
</script>

<style scoped>
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
	.el-table .cell {
		padding: 0px !important;
		padding-left: 0px;
		margin: 0px !important;
	}
</style>
