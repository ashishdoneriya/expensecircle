<template>
	<el-row justify="center">
		<el-col :xs="24" :sm="20" :md="16" :lg="12">
			<el-space direction="vertical" fill class="full-width" :size="20">
				<section>
					<el-text type="info" size="small" class="section-label">
						IDENTITY & PEOPLE
					</el-text>
					<el-card
						shadow="never"
						:body-style="{ padding: '0px' }"
						class="menu-card">
						<el-row
							align="middle"
							class="menu-row flex-container"
							@click="navigateTo('members')">
							<div class="flex-container">
								<el-icon class="marginRight10"><User /></el-icon>
								<el-text>Members</el-text>
							</div>
							<div>
								<el-icon><ArrowRight /></el-icon>
							</div>
						</el-row>
					</el-card>
				</section>

				<section>
					<el-text type="info" size="small" class="section-label">
						ORGANIZATION
					</el-text>
					<el-card
						shadow="never"
						:body-style="{ padding: '0px' }"
						class="menu-card">
						<el-row
							align="middle"
							class="menu-row separator flex-container"
							@click="navigateTo('categories')">
							<div class="flex-container">
								<el-icon class="marginRight10"><Collection /></el-icon>
								<el-text>Categories</el-text>
							</div>
							<div>
								<el-icon><ArrowRight /></el-icon>
							</div>
						</el-row>

						<el-row
							align="middle"
							class="menu-row separator flex-container"
							@click="navigateTo('tags')">
							<div class="flex-container">
								<el-icon class="marginRight10"><PriceTag /></el-icon>
								<el-text>Tags</el-text>
							</div>
							<div>
								<el-icon><ArrowRight /></el-icon>
							</div>
						</el-row>
					</el-card>
				</section>
			</el-space>
		</el-col>
	</el-row>
</template>

<script setup>
	import { useRouter } from "vue-router";
	import {
		User,
		Collection,
		PriceTag,
		ArrowRight,
		SwitchButton,
	} from "@element-plus/icons-vue";
	import { useGroupStore } from "@/stores/group";

	const router = useRouter();
	const group = useGroupStore();

	function navigateTo(page) {
		router.push(`/groups/${group.groupId}/settings/${page}`);
	}
</script>

<style scoped>
	.full-width {
		width: 100%;
	}

	.section-label {
		margin-left: 5px;
	}

	.menu-card {
		margin-top: 10px;
	}

	.menu-row {
		padding: 15px;
		cursor: pointer;
	}

	.separator {
		border-bottom: 1px solid var(--el-border-color-lighter);
	}

	.text-right {
		text-align: right;
	}

	.leave-group-btn {
		width: 100%;
		height: 45px;
		margin-top: 30px;
	}

	.icon-margin {
		margin-right: 8px;
	}

	.header-divider {
		margin: 20px 0;
	}

	.list-item-row {
		padding: 12px 0;
	}

	.no-margin-divider {
		margin: 0;
	}

	.add-category-input {
		margin-top: 20px;
	}
</style>
