<template>
	<el-row style="margin: 5px 10px" class="flex-container">
		<div>
			<el-space spacer="|">
				<el-text
					tag="b"
					size="large"
					@click="goBackToDashboard"
					style="cursor: pointer">
					Groups
				</el-text>
				<el-text size="large" v-if="group.isInitialized">
					{{ group.groupName }}
				</el-text>
			</el-space>
		</div>
		<div style="display: flex; align-items: center; gap: 10px">
			<el-switch
				v-model="isDark"
				:active-icon="Moon"
				:inactive-icon="Sunny"
				inline-prompt />
			<el-dropdown trigger="click" @command="handleCommand">
				<el-avatar
					:size="40"
					class="mr-3"
					:src="userStore.picture"
					style="cursor: pointer" />
				<template #dropdown>
					<el-dropdown-menu>
						<el-dropdown-item command="logout">Logout</el-dropdown-item>
					</el-dropdown-menu>
				</template>
			</el-dropdown>
		</div>
	</el-row>
	<el-divider style="margin: 0px 0px" />
</template>
<script setup>
	import { Sunny, Moon } from "@element-plus/icons-vue";
	import { useDark } from "@vueuse/core";
	import { useGroupStore } from "@/stores/group";
	import { useUserStore } from "@/stores/user";
	import { useRouter } from "vue-router";

	const userStore = useUserStore();
	const group = useGroupStore();

	const router = useRouter();

	const isDark = useDark({
		storageKey: "isDark",
	});

	function goBackToDashboard() {
		group.clearInfo();
		router.push("/groups");
	}

	function handleCommand(command) {
		if (command == 'logout') {
			logout();
			router.push('/');
		}
	}

	// Clear all cookies
	function logout() {
		const cookies = document.cookie.split(";");

		cookies.forEach((cookie) => {
			const cookieName = cookie.split("=")[0].trim();
			document.cookie =
				cookieName + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/";
		});
		sessionStorage.clear();
		group.clearInfo();
		userStore.logout();
		localStorage.clear();

	}
</script>
