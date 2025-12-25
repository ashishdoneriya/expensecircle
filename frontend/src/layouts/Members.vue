<template>
	<el-row justify="center" style="margin-bottom: 30px">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<el-input
				v-model="newMemberId"
				placeholder="New member email id"
				@keydown.enter="addMember"
				:disabled="newMemberInputDisabled"
				style="max-width: calc(100% - 160px); margin-right: 5px"></el-input>
			<el-button
				type="primary"
				plain
				icon="plus"
				@click="addMember"
				:loading="newMemberButtonDisabled"
				:disabled="!newMemberId || newMemberButtonDisabled">
				Add Member
			</el-button>
		</el-col>
	</el-row>
	<el-row justify="center" style="margin-bottom: 30px">
		<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
			<el-row v-for="member in group.members">
				<el-col :span="24" class="flex-container">
					<div>
						<el-text>
							{{ member.userName ? member.userName : member.userId }}
						</el-text>
					</div>
					<div>
						<el-tag
							v-if="member.role == 'ADMIN'"
							type="success"
							size="small"
							:style="
								group.groupInfo.role == 'ADMIN' &&
								member.userId == userStore.userId &&
								group.members.length > 1
									? 'margin-right:30px'
									: ''
							">
							ADMIN
						</el-tag>
						<el-dropdown
							trigger="click"
							@command="handleRowActions"
							v-if="
								group.groupInfo.role == 'ADMIN' &&
								member.userId != userStore.userId
							">
							<img
								src="@/assets/more-vertical.svg"
								alt="Icon"
								class="icon handle cursorPointer"
								style="margin-right: 5px" />
							<template #dropdown>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item
										:command="{
											command: 'MAKE_ADMIN',
											row: member,
										}"
										v-if="member.role != 'ADMIN'"
										icon="el-icon-download">
										Make group admin
									</el-dropdown-item>
									<el-dropdown-item
										:command="{
											command: 'REMOVE_AS_ADMIN',
											row: member,
										}"
										v-if="member.role == 'ADMIN'"
										icon="el-icon-scissors">
										Dismiss as admin
									</el-dropdown-item>
									<el-dropdown-item
										:command="{ command: 'REMOVE', row: member }"
										icon="el-icon-document-copy">
										Remove
										{{
											member.userName
												? member.userName
												: member.userId
										}}
									</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>
				</el-col>
				<el-col :span="24">
					<el-divider border-style="dashed" style="margin: 5px 0px" />
				</el-col>
			</el-row>
		</el-col>
	</el-row>
	<el-row justify="center">
		<el-button type="danger" @click="leaveGroup">
			<img
				src="@/assets/logout.svg"
				alt="Icon"
				class="icon handle cursorPointer"
				style="margin-right: 5px" />
			Leave Group
		</el-button>
	</el-row>
</template>

<script setup>
	import { ref } from "vue";
	import { useGroupStore } from "@/stores/group";
	import * as api from "@/api/api";
	import { useUserStore } from "@/stores/user";
	import { useRouter } from "vue-router";
	import { ElMessage, ElMessageBox } from "element-plus";
	import { Delete } from "@element-plus/icons-vue";

	const group = useGroupStore();
	const userStore = useUserStore();
	const router = useRouter();

	const newMemberId = ref("");
	const newMemberInputDisabled = ref(false);
	const newMemberButtonDisabled = ref(false);

	async function addMember() {
		if (!newMemberId) {
			return;
		}
		newMemberInputDisabled.value = true;
		newMemberButtonDisabled.value = true;
		try {
			await group.addMember(newMemberId.value);
			newMemberId.value = "";
			ElMessage({
				message: "Invitation Sent",
				type: "success",
				duration: 1200,
				offset: window.innerHeight - 100,
			});
		} catch (error) {
			console.log(error);
			ElMessage({
				message: "Problem while adding member",
				type: "error",
				offset: window.innerHeight - 100,
			});
		}
		newMemberInputDisabled.value = false;
		newMemberButtonDisabled.value = false;
	}

	async function handleRowActions(obj) {
		let command = obj.command;
		let member = obj.row;
		if (command == "MAKE_ADMIN") {
			await group.changeUserPermission(member, "ADMIN");
		} else if (command == "REMOVE_AS_ADMIN") {
			await group.changeUserPermission(member, "MEMBER");
		} else if (command == "REMOVE") {
			await group.removeUser(member);
		}
	}

	async function leaveGroup() {
		if (group.groupInfo.role = 'ADMIN') {
			if (group.members.length == 1) {
				await askConfirmation(
					"If you leave then this groups will be permanently deleted. Are you sure want to leave this group?",
				);
			} else {
				if (getNumAdmins() == 1) {
					await askConfirmation(
						"Currently, you are the only ADMIN of this group. If you leave then all other members will automatically become ADMIN. Are you sure want to leave this group?",
					);
				} else {
					await askConfirmation(
						"Are you sure want to leave this group?",
					);
				}
			}
		} else {
			await askConfirmation(
				"Are you sure want to leave this group?",
			);
		}
	}

	function getNumAdmins() {
		let count = 0;
		for (let member of group.members) {
			if (member.role == 'ADMIN') {
				count++;
			}
		}
		return count;
	}

	async function askConfirmation(message) {
		try {
			await ElMessageBox.confirm(message, {
				confirmButtonText: "OK",
				cancelButtonText: "Cancel",
				type: "warning",
				icon: markRaw(Delete),
			});
			await api.exitFromGroup(group.groupId);
			group.clearInfo();
			router.push("/groups");
		} catch (error) {}
	}
</script>
