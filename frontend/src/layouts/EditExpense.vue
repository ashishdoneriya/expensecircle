<template>
	<el-row justify="center">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9" is-loading="loading">
			<el-form
				:model="form"
				label-width="auto"
				:label-position="isMobile ? 'top' : 'left'"
				:rules="rules"
				ref="formRef"
				hide-required-asterisk>
				<el-form-item label="Date">
					<el-date-picker
						v-model="form.timestamp"
						type="datetime"
						placeholder="Pick a Date"
						format="DD MMM YYYY hh:mm A"
						value-format="x"
						style="width: 100%"
						:editable="false" />
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

				<el-form-item label="Tags">
					<Tags v-model="form.tags" />
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
	<el-row
		justify="center"
		v-if="auditState == 'loaded'"
		style="margin-top: 30px">
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
	const loading = ref(true);
	const group = useGroupStore();
	const route = useRoute();
	const router = useRouter();
	const isMobile = window.innerWidth <= 720;
	const form = reactive({
		timestamp: new Date(),
		categoryId: "",
		amount: "",
		description: "",
		tags: [],
	});

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

	onMounted(async () => {
		let expenseId = route.params.expenseId;
		let groupId = route.params.groupId;
		if (!group.isInitialized) {
			group.initialize(groupId);
		}
		if (route.params.expenseId) {
			try {
				expense.value = (await api.getExpense(group.groupId, expenseId)).data;
			} catch (error) {
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
		}
	});

	async function deleteExpense() {
		try {
			await ElMessageBox.confirm(`Are you sure want to delete the expense`, {
				confirmButtonText: "OK",
				cancelButtonText: "Cancel",
				type: "warning",
				icon: markRaw(Delete),
			});
			await api.deleteExpense(group.groupId, expense.value.expenseId);
			goBack();
		} catch (error) {}
	}

	function onSubmit() {
		formRef.value.validate((valid) => {
			if (valid) {
				let obj = {
					expenseId: expense.value.expenseId,
					timestamp: Number(form.timestamp),
					amount: form.amount,
					categoryId: form.categoryId,
					description: form.description,
					tags: form.tags,
				};

				api
					.updateExpense(group.groupId, expense.value.expenseId, obj)
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
		if (auditState.value == "loading") {
			return;
		}
		auditState.value = "loading";
		try {
			let data = (
				await api.getExpenseAuditDetails(group.groupId, expense.value.expenseId)
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
