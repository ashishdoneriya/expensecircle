<template>
	<el-row justify="center">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<el-form
				:model="form"
				label-width="auto"
				:label-position="isMobile ? 'top' : 'left'"
				:rules="rules"
				ref="formRef"
				hide-required-asterisk>
				<el-form-item label="Amount" prop="amount">
					<el-input
						v-model.number="form.amount"
						placeholder="Enter amount"
						type="number"
						autocomplete="off" />
				</el-form-item>

				<el-form-item label="Description">
					<el-input
						v-model.number="form.description"
						placeholder="Description"
						type="text" />
				</el-form-item>
				<el-form-item label="Category">
					<el-select v-model="form.categoryId" placeholder="Select Category">
						<el-option
							v-for="category in group.categories"
							:label="category.categoryName"
							:value="category.categoryId" />
						<template #footer>
							<div style="display: flex; gap: 1rem">
								<el-button
									text
									bg
									:icon="Plus"
									@click="addCategory"
									size="small">
									Add Category
								</el-button>
							</div>
						</template>
					</el-select>
				</el-form-item>

				<el-form-item label="Tags">
					<Tags v-model="form.tags" />
				</el-form-item>

				<el-form-item label="Frequency">
					<el-select v-model="form.frequency">
						<el-option label="Monthly" value="MONTHLY" />
						<el-option label="Weekly" value="WEEKLY" />
						<el-option label="Daily" value="DAILY" />
					</el-select>
				</el-form-item>

				<el-form-item label="Day of Month" v-if="form.frequency == 'MONTHLY'">
					<el-select v-model="form.dayOfMonth">
						<el-option v-for="day in 31" :key="day" :value="day" />
						<el-option label="Last Day of Month" value="99" />
					</el-select>
				</el-form-item>

				<el-form-item label="Day of Week" v-if="form.frequency == 'WEEKLY'">
					<el-select v-model="form.dayOfWeek">
						<el-option label="Sunday" value="SUN" />
						<el-option label="Monday" value="MON" />
						<el-option label="Tuesday" value="TUE" />
						<el-option label="Wednesday" value="WED" />
						<el-option label="Thursday" value="THU" />
						<el-option label="Friday" value="FRI" />
						<el-option label="Saturday" value="SAT" />
					</el-select>
				</el-form-item>

				<el-form-item :label="isMobile ? '' : 'Time'">
					<el-col :span="7">
						<el-text>Hour</el-text>
						<el-select v-model="form.executionTimeHour">
							<el-option v-for="day in 12" :label="day" :value="day" />
						</el-select>
					</el-col>
					<el-col :span="1"></el-col>
					<el-col :span="7">
						<el-text>Minutes</el-text>
						<el-select v-model="form.executionTimeMinute">
							<el-option
								v-for="n in 60"
								:label="String(n - 1).padStart(2, '0')"
								:value="n - 1" />
						</el-select>
					</el-col>
					<el-col :span="1"></el-col>
					<el-col :span="8">
						<el-text>AM/PM</el-text>
						<el-select v-model="form.dayPeriod">
							<el-option label="AM" value="AM" />
							<el-option label="PM" value="PM" />
						</el-select>
					</el-col>
				</el-form-item>

				<el-form-item :label="isMobile ? '' : ' '">
					<el-button type="primary" @click="onSubmit" :disabled="!form.amount">
						Add
					</el-button>
					<el-button @click="goBack">Cancel</el-button>
				</el-form-item>
			</el-form>
		</el-col>
	</el-row>
</template>

<script setup>
	import { reactive, ref, onMounted } from "vue";
	import { useRouter, useRoute } from "vue-router";
	import { ElMessage } from "element-plus";
	import { useGroupStore } from "@/stores/group";
	import * as api from "@/api/api";
	import { Plus } from "@element-plus/icons-vue";
	const group = useGroupStore();
	const router = useRouter();
	const route = useRoute();
	const isMobile = window.innerWidth <= 720;
	const form = reactive({
		frequency: "MONTHLY",
		dayOfMonth: 1,
		dayOfWeek: "SUN",
		dayPeriod: "AM",
		executionTimeHour: getRandomNumber(1, 12, 1),
		executionTimeMinute: getRandomNumber(1, 30, 5),
		categoryId: "",
		amount: "",
		description: "",
		tags: [],
	});

	onMounted(async () => {
		if (route.query["timestamp"]) {
			let date = new Date(Number(route.query["timestamp"]));
			let now = new Date();
			date.setHours(
				now.getHours(),
				now.getMinutes(),
				now.getSeconds(),
				now.getMilliseconds(),
			);
			form.timestamp = date;
		}
	});

	const formRef = ref(null);

	// Validation rules
	const rules = {
		amount: [
			{ required: true, message: "Amount is required", trigger: "blur" },
		],
	};

	function onSubmit() {
		formRef.value.validate((valid) => {
			if (valid) {
				let obj = {
					frequency: form.frequency,
					dayOfMonth: Number(form.dayOfMonth),
					dayOfWeek: form.dayOfWeek,
					dayPeriod: form.dayPeriod,
					executionTimeHour: Number(form.executionTimeHour),
					executionTimeMinute: Number(form.executionTimeMinute),
					amount: form.amount,
					categoryId: form.categoryId,
					description: form.description,
					tags: form.tags,
					timezone: Intl.DateTimeFormat().resolvedOptions().timeZone
				};

				api
					.addRecurringExpense(group.groupId, obj)
					.then(() => {
						ElMessage({
							message: "Added",
							type: "success",
							duration: 1200,
							offset: window.innerHeight - 100,
						});
						form.amount = "";
						form.description = "";
						form.timestamp = new Date();
						form.categoryId = "";
						group.fetchTags();
						goBack();
					})
					.catch((error) => {
						ElMessage({
							message: "Unable to add",
							type: "error",
							offset: window.innerHeight - 100,
						});
					});
			} else {
				return false;
			}
		});
	}

	function getRandomNumber(min, max, factor) {
		// Find the first and last valid multiples within the range
		const firstMultiple = Math.ceil(min / factor) * factor;
		const lastMultiple = Math.floor(max / factor) * factor;

		if (firstMultiple > lastMultiple) {
			throw new Error("No valid multiple exists in the given range.");
		}

		// Calculate how many possible multiples exist
		const rangeCount = (lastMultiple - firstMultiple) / factor + 1;

		// Pick a random step and scale it back up
		return Math.floor(Math.random() * rangeCount) * factor + firstMultiple;
	}

	function goBack() {
		router.back();
	}

	function addCategory() {
		router.push(`/groups/${group.groupId}/categories`);
	}
</script>
