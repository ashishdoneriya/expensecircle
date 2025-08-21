<template>
	<Header></Header>
	<el-container>
		<el-main style="padding: 10px">
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
		if (Number(route.params.groupId) != Number(group.groupId)) {
			group.clearInfo();
			group.initialize(route.params.groupId);
		}
	});

</script>


