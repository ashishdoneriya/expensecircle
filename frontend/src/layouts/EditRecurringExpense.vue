<template>
	<el-row justify="center">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9" v-loading="loading">
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
						Update
					</el-button>
					<el-button
						type="danger"
						@click="deleteExpense"
						:disabled="!form.amount">
						Delete
					</el-button>
					<el-button @click="goBack">Cancel</el-button>
				</el-form-item>

				<el-form-item
					:label="isMobile ? '' : ' '"
					style="margin-top: 30px"
					v-if="auditState != 'loaded'">
					<div @click="loadAuditDetails()" class="flex-container cursorPointer">
						<Image name="history" v-if="auditState == 'notRequested'" />
						<el-icon class="is-loading marginRight10" style v-else>
							<Loading />
						</el-icon>
						View Activity
					</div>
				</el-form-item>
			</el-form>
		</el-col>
	</el-row>
	<el-row justify="center" v-if="auditState == 'loaded'">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<el-descriptions
				title="Activity History"
				:column="1"
				:size="isMobile ? 'small' : 'default'"
				border>
				<el-descriptions-item label="Created by">
					{{ auditDetails.createdBy }}
				</el-descriptions-item>
				<el-descriptions-item label="Updated by" v-if="auditDetails.updatedBy">
					{{ auditDetails.updatedBy }}
				</el-descriptions-item>
			</el-descriptions>
		</el-col>
	</el-row>
</template>

<script setup>
	import { reactive, ref } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import { ElMessage, ElMessageBox } from "element-plus";
	import { useGroupStore } from "@/stores/group";
	import * as api from "@/api/api";
	import { Plus, Loading } from "@element-plus/icons-vue";
	import { useUserStore } from "@/stores/user";
	import { Delete } from "@element-plus/icons-vue";
	import Tags from "@/components/Tags.vue";
	import Image from "@/components/Image.vue";
	const userStore = useUserStore();
	const group = useGroupStore();
	const route = useRoute();
	const router = useRouter();
	const isMobile = window.innerWidth <= 720;
	const form = reactive({
		frequency: "MONTHLY",
		dayOfMonth: 1,
		dayOfWeek: "SUN",
		dayPeriod: "AM",
		executionTimeHour: "",
		executionTimeMinute: "",
		timestamp: new Date(),
		categoryId: "",
		amount: "",
		description: "",
		tags: [],
	});

	const loading = ref(true);

	const expense = ref({});

	const formRef = ref(null);

	// three states - notLoaded, loading, loaded
	const auditState = ref("notRequested");

	// Validation rules
	const rules = {
		amount: [
			{ required: true, message: "Amount is required", trigger: "blur" },
		],
	};

	const auditDetails = ref({
		createdBy: "",
		updatedBy: "",
	});

	const expenseId = route.params.expenseId

	onMounted(async () => {
		let groupId = route.params.groupId;
		group.initialize(groupId);
		if (expenseId) {
			try {
				expense.value = (
					await api.getRecurringExpense(group.groupId, expenseId)
				).data;
			} catch (error) {
				loading.value = false;
				console.log(error);
				ElMessage({
					message: "Unable to load data, please try after some time",
					type: "error",
					offset: window.innerHeight - 100,
				});
			}
			loading.value = false;
			form.timestamp = expense.value.timestamp;
			form.categoryId = expense.value.categoryId;
			form.amount = expense.value.amount;
			form.description = expense.value.description;
			form.tags = expense.value.tags.map((tagId) => ({
				tagId,
				tagName: group.tagsMap[tagId],
			}));
			form.frequency = expense.value.frequency;
			form.dayOfMonth = expense.value.dayOfMonth;
			form.dayOfWeek = expense.value.dayOfWeek;
			form.dayPeriod = expense.value.dayPeriod;
			form.executionTimeHour = expense.value.executionTimeHour;
			form.executionTimeMinute = expense.value.executionTimeMinute;
		}
	});

	async function deleteExpense() {
		try {
			await ElMessageBox.confirm(
				`Are you sure want to delete the recurring expense`,
				{
					confirmButtonText: "OK",
					cancelButtonText: "Cancel",
					type: "warning",
					icon: markRaw(Delete),
				},
			);
			await api.deleteRecurringExpense(group.groupId, expenseId);
			goBack();
		} catch (error) {}
	}

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
				};

				api
					.updateRecurringExpense(group.groupId, expenseId, obj)
					.then(() => {
						ElMessage({
							message: "Updated",
							type: "success",
							duration: 1200,
							offset: window.innerHeight - 100,
						});
						group.fetchTags();
						goBack();
					})
					.catch((error) => {
						ElMessage({
							message: "Unable to update",
							type: "error",
							offset: window.innerHeight - 100,
						});
					});
			} else {
				return false;
			}
		});
	}

	async function loadAuditDetails() {
		auditState.value = "loading";
		try {
			let data = (
				await api.getRecurringExpenseAuditDetails(group.groupId, route.params.expenseId)
			).data;
			if (!data.createdByName && !data.createdByName) {
				ElMessage({
					message: "Activity History not available",
					type: "error",
					offset: window.innerHeight - 100,
				});
				auditState.value = "";
				return;
			}
			auditDetails.value.createdBy = `${data.createdByName} (${data.createdByEmail}) on ${epochToString(data.createdAt)}`;
			if (data.updatedByName) {
				auditDetails.value.updatedBy = `${data.updatedByName} (${data.updatedByEmail}) on ${epochToString(data.updatedAt)}`;
			}

			auditState.value = "loaded";
		} catch (error) {
			console.log(error);
			ElMessage({
				message: "Activity History not available",
				type: "error",
				offset: window.innerHeight - 100,
			});
			auditState.value = "";
		}
	}

	function epochToString(epoch) {
		return new Intl.DateTimeFormat("en-IN", {
			day: "2-digit",
			month: "short",
			year: "numeric",
			hour: "2-digit",
			minute: "2-digit",
			hour12: true,
		}).format(new Date(Number(epoch)));
	}

	function goBack() {
		router.back();
	}

	function addCategory() {
		router.push(`/groups/${group.groupId}/settings/categories`);
	}
</script>
