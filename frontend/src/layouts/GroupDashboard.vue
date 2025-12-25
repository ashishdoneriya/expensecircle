<template>
	<Header></Header>
	<el-container>
		<el-main style="padding: 10px; margin-bottom: 100px">
			<router-view />
		</el-main>
	</el-container>
	<AppFooter />
</template>

<script setup>
	import { onMounted } from "vue";
	import { useGroupStore } from "@/stores/group";
	import { useRoute } from "vue-router";
	import AppFooter from "@/components/GroupFooter.vue";

	onMounted(() => {
		const route = useRoute();
		const group = useGroupStore();
		if (route.params.groupId != group.groupId) {
			group.clearInfo();
			group.initialize(route.params.groupId);
		}
	});
</script>
