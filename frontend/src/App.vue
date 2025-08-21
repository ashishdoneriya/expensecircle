<template>
	<router-view />
</template>

<script setup>
	//
</script>

<style>
	html,
	body {
		font-family: "Roboto", sans-serif;
		margin: 0px;
	}
	html.dark,
	html.dark body {
		background-color: #1f1f1f;
		color: var(--el-text-color-primary);
	}
	.flex-container {
		display: flex;
		justify-content: space-between;
	}

</style>
