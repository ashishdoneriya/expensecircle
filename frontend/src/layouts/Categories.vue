<template>
	<el-row justify="center" style="margin-bottom: 30px">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<el-input
				v-model="newCategoryName"
				placeholder="Category Name"
				@keydown.enter="createCategory"
				:disabled="newCategoryInputDisabled"
				style="max-width: calc(100% - 160px); margin-right: 5px"></el-input>
			<el-button
				type="primary"
				plain
				icon="plus"
				@click="createCategory"
				:loading="newCategoryButtonDisabled"
				:disabled="!newCategoryName || newCategoryButtonDisabled">
				New Category
			</el-button>
		</el-col>
	</el-row>
	<el-row justify="center">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<draggable
				:list="group.categories"
				@end="onDragEnd"
				handle=".handle"
				:item-key="'categoryId'">
				<template #item="{ element }">
					<el-row :key="element.categoryId" justify="center">
						<el-col :span="24">
							<el-row class="flex-container">
								<div class="flex-container">
									<img
										src="@/assets/justify.svg"
										alt="Icon"
										class="icon handle cursorPointer"
										style="margin-right: 5px" />
									<el-text
										style="
											font-size: var(--el-font-size-large);
											font-weight: 400;
										">
										{{ element.categoryName }}
									</el-text>
								</div>
								<div>
									<el-button
										:icon="Edit"
										circle
										@click.stop="renameCategory(element)"
										:disabled="disabledEdit[element.categoryId]"
										:loading="disabledEdit[element.categoryId]" />
									<el-button
										type="danger"
										:icon="Delete"
										circle
										@click.stop="deleteCategory(element)"
										:disabled="disabledDelete[element.categoryId]"
										:loading="disabledDelete[element.categoryId]" />
								</div>
							</el-row>
						</el-col>
						<el-col :span="24">
							<el-divider border-style="dashed" style="margin: 5px 0px" />
						</el-col>
					</el-row>
				</template>
			</draggable>
		</el-col>
	</el-row>
</template>
<script setup>
	import { ref } from "vue";
	import { ElMessage, ElMessageBox } from "element-plus";
	import { Delete, Edit } from "@element-plus/icons-vue";
	import { useGroupStore } from "@/stores/group";
	import draggable from "vuedraggable";
	import * as api from "@/api/api";

	const group = useGroupStore();

	const newCategoryName = ref("");
	const newCategoryInputDisabled = ref(false);
	const newCategoryButtonDisabled = ref(false);

	const disabledEdit = ref({});
	const disabledDelete = ref({});
	let timeoutId;
	function onDragEnd() {
		if (timeoutId) {
			clearTimeout(timeoutId);
		}
		timeoutId = setTimeout(() => {
			updateOrder();
		}, 1000);
		console.log("New order:", group.categories);
	}

	function updateOrder() {
		let newArr = [];
		for (let i = 0; i < group.categories.length; i++) {
			newArr.push({
				id: group.categories[i].categoryId,
				orderNumber: i,
			});
		}
		api.changeCategoriesOrder(group.groupId, newArr);
	}

	async function createCategory() {
		if (!newCategoryName) {
			return;
		}
		newCategoryInputDisabled.value = true;
		newCategoryButtonDisabled.value = true;
		try {
			await group.addCategory(newCategoryName.value);
			newCategoryName.value = "";
			ElMessage({
				message: "Category Added",
				type: "success",
				duration: 1200,
				offset: window.innerHeight - 100,
			});
		} catch (error) {
			console.log(error);
			ElMessage({
				message: "Problem while adding category",
				type: "error",
				offset: window.innerHeight - 100,
			});
		}
		newCategoryInputDisabled.value = false;
		newCategoryButtonDisabled.value = false;
	}

	async function renameCategory(category) {
		try {
			let value = await ElMessageBox.prompt("New name of the category", {
				confirmButtonText: "OK",
				cancelButtonText: "Cancel",
				inputValue: category.categoryName,
			});
			value = value.value;
			if (value == category.categoryName) {
				return;
			}
			try {
				disabledEdit.value[category.categoryId] = true;
				await group.renameCategory(category.categoryId, value);
				ElMessage({
					message: "Category name changed",
					type: "success",
					duration: 1200,
					offset: window.innerHeight - 100,
				});
				category.categoryName = value;
			} catch (error) {
				ElMessage({
					message: "Unable to change the name",
					type: "error",
					offset: window.innerHeight - 100,
				});
			}
			disabledEdit.value[category.categoryId] = false;
		} catch (error) {}
	}

	async function deleteCategory(category) {
		try {
			await ElMessageBox.confirm(
				`Are you sure want to permanently delete the category ${category.categoryName}`,
				{
					confirmButtonText: "OK",
					cancelButtonText: "Cancel",
					type: "warning",
					icon: markRaw(Delete),
				},
			);
			try {
				disabledDelete.value[category.categoryId] = true;
				await group.deleteCategory(category.categoryId);
				ElMessage({
					message: "Category deleted",
					type: "success",
					duration: 1200,
					offset: window.innerHeight - 100,
				});
			} catch (error) {
				ElMessage({
					message: "Unable to delete the category",
					type: "error",
					offset: window.innerHeight - 100,
				});
			}
			disabledDelete.value[category.categoryId] = false;
		} catch (error) {}
	}
</script>
