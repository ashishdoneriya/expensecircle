<template>
	<div class="bottom-bar">
		<div
			class="bottom-bar-item"
			@click="navigateTo('expenses')"
			:class="{ active: activePage === 'expenses' }">
			<el-icon class="icon">
				<Wallet />
			</el-icon>
			<span class="label">Expenses</span>
		</div>

		<div
			class="bottom-bar-item"
			@click="navigateTo('stats')"
			:class="{ active: activePage === 'stats' }">
			<el-icon class="icon">
				<Histogram />
			</el-icon>
			<span class="label">Stats</span>
		</div>

		<div
			class="bottom-bar-item"
			@click="navigateTo('categories')"
			:class="{ active: activePage === 'categories' }">
			<el-icon class="icon">
				<Collection />
			</el-icon>
			<span class="label">Categories</span>
		</div>

		<div
			class="bottom-bar-item"
			@click="navigateTo('tags')"
			:class="{ active: activePage === 'tags' }">
			<el-icon class="icon">
				<PriceTag />
			</el-icon>
			<span class="label">Tags</span>
		</div>

		<div
			class="bottom-bar-item"
			@click="navigateTo('settings')"
			:class="{ active: activePage === 'settings' }">
			<el-icon class="icon">
				<Setting />
			</el-icon>
			<span class="label">Settings</span>
		</div>
	</div>
</template>

<script setup>
	import { ref, watchEffect } from "vue";
	import { useGroupStore } from "@/stores/group";
	import { useRouter, useRoute } from "vue-router";
	const group = useGroupStore();
	const router = useRouter();
	const route = useRoute();
	const activePage = ref("");

	function navigateTo(page) {
		router.push(`/groups/${group.groupId}/${page}`);
	}

	watchEffect(() => {
		activePage.value = route.path.split("/")[3];
	});
</script>

<style scoped>
	.bottom-bar {
		z-index: 1000;
		opacity: 1;
		position: fixed;
		bottom: 0;
		width: 100%;
		height: 60px;
		background-color: var(--el-bg-color-overlay);
		display: flex;
		justify-content: space-around;
		align-items: center;
		border-top: 1px solid var(--el-border-color);
		padding-top: 0px;
	}

	.bottom-bar-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		color: var(--el-text-color-secondary);
		cursor: pointer;
		transition: color 0.3s;
	}

	.bottom-bar-item .icon {
		font-size: 28px;
	}

	.bottom-bar-item .label {
		font-size: 12px;
		margin-top: 4px;
	}

	.bottom-bar-item.active {
		color: var(
			--el-color-primary
		); /* Element Plus primary color for active state */
	}

	.bottom-bar-item.active .icon {
		color: var(--el-color-primary);
		font-size: 30px;
	}
</style>
