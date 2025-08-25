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
					<el-select
						v-model="form.categoryId"
						placeholder="Select Category">
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
					<el-select
						v-model="form.tags"
						placeholder="Select Tags"
						multiple
						filterable
						allow-create
						default-first-option
						:reserve-keyword="false">
						<el-option
							v-for="tag in group.tags"
							:label="tag.tagName"
							:value="tag.tagId"/>
					</el-select>
				</el-form-item>

				<el-form-item
					label="Created"
					v-if="
						!(
							expense.ownerUserId == expense.lastChangedByUserId &&
							expense.ownerUserId == userStore.email
						)
					">
					<el-text>{{ expense.ownerUserId }}</el-text>
				</el-form-item>

				<el-form-item
					label="Last Edit"
					v-if="
						!(
							expense.ownerUserId == expense.lastChangedByUserId &&
							expense.ownerUserId == userStore.email
						)
					">
					<el-text>{{ expense.lastChangedByUserId }}</el-text>
				</el-form-item>

				<el-form-item :label="isMobile ? '' : ' '">
					<el-button
						type="primary"
						@click="onSubmit"
						:disabled="!form.amount">
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
			</el-form>
		</el-col>
	</el-row>
</template>

<script setup>
	import { reactive, ref } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import { ElMessage, ElMessageBox } from "element-plus";
	import { useGroupStore } from "@/stores/group";
	import * as api from "@/api/api";
	import { Plus } from "@element-plus/icons-vue";
	import { useUserStore } from "@/stores/user";
	import { Delete } from "@element-plus/icons-vue";

	const userStore = useUserStore();

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

	// Validation rules
	const rules = {
		amount: [
			{ required: true, message: "Amount is required", trigger: "blur" },
		],
	};

	onMounted(async () => {
		let expenseId = route.params.expenseId;
		let groupId = route.params.groupId;
		if (!group.isInitialized) {
			group.initialize(Number(groupId));
		}
		if (Number(route.params.expenseId)) {
			expense.value = (await api.getExpense(group.groupId, expenseId)).data;
			form.timestamp = expense.value.timestamp;
			form.categoryId = expense.value.categoryId;
			form.amount = expense.value.amount;
			form.description = expense.value.description;
			form.tags = expense.value.tags;
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
		} catch (error) {

		}
	}

	function onSubmit() {
		formRef.value.validate((valid) => {
			if (valid) {
				let obj = {
					expenseId: expense.value.expenseId,
					newTimestamp: Number(form.timestamp),
					amount: form.amount,
					categoryId: form.categoryId,
					description: form.description,
					tags: form.tags.filter((item) => typeof item === "number"),
					newTags: form.tags.filter((item) => typeof item === "string"),
				};

				api.updateExpense(group.groupId, expense.value.expenseId, obj)
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

	function goBack() {
		router.back();
	}

	function addCategory() {
		router.push(`/groups/${group.groupId}/categories`);
	}
</script>
