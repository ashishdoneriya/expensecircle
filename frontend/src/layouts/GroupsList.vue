<template>
	<Header></Header>
	<el-container>
		<el-main>
			<el-row justify="center" style="margin-bottom: 30px">
				<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
					<el-input
						v-model="newGroupName"
						placeholder="Group Name"
						@keydown.enter="createGroup"
						:disabled="newGroupInputDisabled"
						style="
							max-width: calc(100% - 145px);
							margin-right: 5px;
						"></el-input>
					<el-button
						type="primary"
						plain
						icon="plus"
						@click="createGroup"
						:loading="newGroupButtonDisabled"
						:disabled="!newGroupName || newGroupButtonDisabled">
						Create Group
					</el-button>
				</el-col>
			</el-row>
			<el-row justify="center">
				<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
					<el-row
						v-for="(group, index) in groups"
						:key="index"
						@click="goToGroup(group)"
						justify="center"
						class="cursorPointer">
						<el-col :span="24">
							<el-row class="flex-container">
								<div>
									<el-link
										style="
											font-size: var(--el-font-size-large);
											font-weight: 400;
										">
										{{ group.groupName }}
									</el-link>
								</div>
								<div>
									<el-button
										:icon="Edit"
										circle
										@click.stop="changeGroupName(group)" />
									<el-button
										type="danger"
										:icon="Delete"
										circle
										@click.stop="deleteGroup(group)" />
								</div>
							</el-row>
						</el-col>
						<el-col :span="24">
							<el-divider border-style="dashed" style="margin: 5px 0px" />
						</el-col>
					</el-row>
				</el-col>
			</el-row>
		</el-main>
	</el-container>
</template>
<script setup>
	import { useUserStore } from "@/stores/user";
	import { ref, onMounted } from "vue";
	import { useRouter } from "vue-router";
	import * as api from "@/api/api";
	import { ElMessage, ElMessageBox } from "element-plus";
	import { Delete, Edit } from "@element-plus/icons-vue";
	import { useGroupStore } from "@/stores/group";
	const userStore = useUserStore();
	const router = useRouter();
	const groupStore = useGroupStore();
	// Example list of groups (replace with actual data or API calls)
	const groups = ref([]);

	onMounted(() => {
		fetchGroups();
	});

	const newGroupName = ref("");
	const newGroupInputDisabled = ref(false);
	const newGroupButtonDisabled = ref(false);

	async function createGroup() {
		if (!newGroupName) {
			return;
		}
		newGroupInputDisabled.value = true;
		newGroupButtonDisabled.value = true;
		try {
			await api.addGroup(newGroupName.value);
			await fetchGroups();
			newGroupName.value = "";
			ElMessage({
				message: "Group Added",
				type: "success",
				duration: 1200,
				offset: window.innerHeight - 100,
			});
		} catch (error) {
			ElMessage({
				message: "Problem while adding group",
				type: "error",
				offset: window.innerHeight - 100,
			});
		}
		newGroupInputDisabled.value = false;
		newGroupButtonDisabled.value = false;
	}

	function logout() {
		userStore.logout();
		router.push({ name: "Login" });
	}

	async function fetchGroups() {
		groups.value = (await api.getGroups()).data;
	}

	function goToGroup(group) {
		groupStore.clearInfo();
		groupStore.initialize(group.groupId);
		router.push(`/groups/${group.groupId}/expenses`);
	}

	function changeGroupName(group) {
		ElMessageBox.prompt("New name of the group", {
			confirmButtonText: "OK",
			cancelButtonText: "Cancel",
			initalValue: group.groupName,
		})
			.then(({ value }) => {
				if (value == group.groupName) {
					return;
				}
				api.changeGroupName(group.groupId, value)
					.then(() => {
						group.groupName = value;
						ElMessage({
							type: "success",
							message: `Group name changed`,
						});
					})
					.catch((e) => {
						ElMessage({
							type: "error",
							message: e,
						});
					});
			})
			.catch(() => {
				ElMessage({
					type: "info",
					message: "Input canceled",
				});
			});
	}

	async function deleteGroup(group) {
		try {
			await ElMessageBox.confirm(
				`Are you sure want to permanently delete the group ${group.groupName}`,
				{
					confirmButtonText: "OK",
					cancelButtonText: "Cancel",
					type: "warning",
					icon: markRaw(Delete),
				},
			);
			await api.deleteGroup(group.groupId);
			let indexToRemove = groups.value.findIndex(item => item.groupId === group.groupId);
			groups.value.splice(indexToRemove, 1);
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
