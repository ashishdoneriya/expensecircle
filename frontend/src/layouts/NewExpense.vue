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
							:value="category.categoryId"/>
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
					<el-button
						type="primary"
						@click="onSubmit"
						:disabled="!form.amount">
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
		timestamp: new Date(),
		categoryId: "",
		amount: "",
		description: "",
		tags: [],
	});

	onMounted(async () => {
		if (route.query['timestamp']) {
			let date = new Date(Number(route.query['timestamp']));
			let now = new Date();
			date.setHours(now.getHours(), now.getMinutes(), now.getSeconds(), now.getMilliseconds());
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
					timestamp: Number(form.timestamp),
					amount: form.amount,
					categoryId: form.categoryId,
					description: form.description,
					tags: form.tags
				};

				api.addExpense(group.groupId, obj)
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

	function goBack() {
		router.back();
	}

	function addCategory() {
		router.push(`/groups/${group.groupId}/settings/categories`);
	}
</script>
