<template>
	<el-row justify="center" style="margin-bottom: 30px">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<el-input
				v-model="newTagName"
				placeholder="Tag Name"
				@keydown.enter="createTag"
				:disabled="newTagInputDisabled"
				style="max-width: calc(100% - 115px); margin-right: 5px"></el-input>
			<el-button
				type="primary"
				plain
				icon="plus"
				@click="createTag"
				:loading="newTagButtonDisabled"
				:disabled="!newTagName || newTagButtonDisabled">
				New Tag
			</el-button>
		</el-col>
	</el-row>
	<el-row justify="center">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<draggable
				:list="group.tags"
				@end="onDragEnd"
				handle=".handle"
				:item-key="'tagId'">
				<template #item="{ element }">
					<el-row
						v-if="!element.deleted"
						:key="element.tagId"
						justify="center">
						<el-col :span="24">
							<el-row class="flex-container">
								<div class="flex-container">
									<img
										src="@/assets/justify.svg"
										alt="Icon"
										class="icon handle cursorPointer"
										style="margin-right: 5px" />
									<el-link
										style="
											font-size: var(--el-font-size-large);
											font-weight: 400;
										">
										{{ element.tagName }}
									</el-link>
								</div>
								<div>
									<el-button
										:icon="Edit"
										circle
										@click.stop="renameTag(element)"
										:disabled="disabledEdit[element.tagId]"
										:loading="disabledEdit[element.tagId]" />
									<el-button
										type="danger"
										:icon="Delete"
										circle
										@click.stop="deleteTag(element)"
										:disabled="disabledDelete[element.tagId]"
										:loading="disabledDelete[element.tagId]" />
								</div>
							</el-row>
						</el-col>
						<el-col :span="24">
							<el-divider
								border-style="dashed"
								style="margin: 5px 0px" />
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

	const newTagName = ref("");
	const newTagInputDisabled = ref(false);
	const newTagButtonDisabled = ref(false);

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
	}

	function updateOrder() {
		let newArr = [];
		for (let i = 0; i < group.tags.length; i++) {
			newArr.push({
				id: group.tags[i].tagId,
				orderNumber: i,
			});
		}
		api.changeTagsOrder(group.groupId, newArr);
	}

	async function createTag() {
		if (!newTagName) {
			return;
		}
		newTagInputDisabled.value = true;
		newTagButtonDisabled.value = true;
		try {
			await group.addTag(newTagName.value);
			newTagName.value = "";
			ElMessage({
				message: "Tag Added",
				type: "success",
				duration: 1200,
				offset: window.innerHeight - 100,
			});
		} catch (error) {
			console.log(error);
			ElMessage({
				message: "Problem while adding tag",
				type: "error",
				offset: window.innerHeight - 100,
			});
		}
		newTagInputDisabled.value = false;
		newTagButtonDisabled.value = false;
	}

	async function renameTag(tag) {
		try {
			let value = await ElMessageBox.prompt("New name of the tag", {
				confirmButtonText: "OK",
				cancelButtonText: "Cancel",
				inputValue: tag.tagName,
			});
			value = value.value;
			if (value == tag.tagName) {
				return;
			}
			try {
				disabledEdit.value[tag.tagId] = true;
				await group.renameTag(tag.tagId, value);
				ElMessage({
					message: "Tag name changed",
					type: "success",
					duration: 1200,
					offset: window.innerHeight - 100,
				});
			} catch (error) {
				ElMessage({
					message: "Unable to change the name",
					type: "error",
					offset: window.innerHeight - 100,
				});
			}
			disabledEdit.value[tag.tagId] = false;
		} catch (error) {}
	}

	async function deleteTag(tag) {
		try {
			await ElMessageBox.confirm(
				`Are you sure want to permanently delete the tag ${tag.tagName}`,
				{
					confirmButtonText: "OK",
					cancelButtonText: "Cancel",
					type: "warning",
					icon: markRaw(Delete),
				},
			);
			try {
				disabledDelete.value[tag.tagId] = true;
				await group.deleteTag(tag.tagId);
				ElMessage({
					message: "Tag deleted",
					type: "success",
					duration: 1200,
					offset: window.innerHeight - 100,
				});
			} catch (error) {
				ElMessage({
					message: "Unable to delete the tag",
					type: "error",
					offset: window.innerHeight - 100,
				});
			}
			disabledDelete.value[tag.tagId] = false;
		} catch (error) {}
	}
</script>

<style>
	.group-row {
		padding: 10px 0;
		cursor: pointer;
		border-bottom: 1px solid #ebeef5;
		transition: background-color 0.3s;
	}

	.group-row:hover {
		background-color: #f0f2f5;
		/* Change background color on hover */
	}

	.group-item {
		padding-left: 20px;
		font-size: 16px;
	}
</style>
