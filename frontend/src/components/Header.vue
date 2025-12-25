<template>
	<el-row style="margin: 5px 10px" class="flex-container">
		<div>
			<el-space spacer="|" v-if="isMobile">
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
			<el-breadcrumb separator="/" v-else>
				<el-breadcrumb-item
					v-for="(crumb, index) in breadcrumbs"
					:key="index"
					:to="crumb.path">
					<el-text size="large">
						{{ crumb.label }}
					</el-text>
				</el-breadcrumb-item>
			</el-breadcrumb>
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
	import { useRoute } from "vue-router";
	import { computed } from "vue";

	const isMobile = window.innerWidth <= 720;

	const route = useRoute();

	const userStore = useUserStore();
	const group = useGroupStore();

	const router = useRouter();

	const isDark = useDark({
		storageKey: "isDark",
	});

	const breadcrumbs = computed(() => {
		// Defensive check: if route.path is not available yet, return empty array
		if (!route || !route.path) return [];

		// Split URL path and remove empty segments to get an array of parts
		const pathArray = route.path.split("/").filter((p) => p !== "");

		// Find the entry point of 'groups' to start the breadcrumb trail from there
		const groupsIndex = pathArray.indexOf("groups");

		// If 'groups' isn't in the URL (e.g., on Login), return empty to hide breadcrumbs
		if (groupsIndex === -1) return [];

		// Slice the path array so we only process segments starting from 'groups'
		const activeSegments = pathArray.slice(groupsIndex);
		let accumulatedPath = "";

		return activeSegments
			.map((segment, index) => {
				// Reconstruct the actual URL path segment by segment using the original array
				const originalSegment = pathArray[index + groupsIndex];

				// Safety check for undefined segments during fast transitions
				if (!originalSegment) return null;

				accumulatedPath += `/${originalSegment}`;

				let label = segment;
				let finalPath = accumulatedPath;

				// Logic for the first segment ('groups')
				if (index === 0) {
					// Per requirement: rename the 'groups' segment to 'Home' in the UI
					label = "Home";
				}
				// Logic for the second segment (the dynamic :groupId)
				else if (index === 1) {
					// Replace the technical ID with the human-readable name from Pinia store
					label = group.groupName || originalSegment;

					// Force redirection to /expenses when the Group Name is clicked
					finalPath = `${accumulatedPath}/expenses`;
				}
				// Logic for subsequent segments (settings, categories, etc.)
				else {
					// Standard formatting: Capitalize the first letter for professional look
					label = label.charAt(0).toUpperCase() + label.slice(1);
				}

				return {
					label: label,
					path: finalPath,
				};
			})
			.filter((crumb) => crumb !== null); // Remove any null entries caused by safety checks
	});
	function goBackToDashboard() {
		group.clearInfo();
		router.push("/groups");
	}

	function handleCommand(command) {
		if (command == "logout") {
			logout();
			router.push("/");
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
