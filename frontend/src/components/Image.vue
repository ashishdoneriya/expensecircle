<template>
	<img
		:src="source"
		class="icon handle cursorPointer"
		style="margin-right: 5px" />
</template>
<script setup>
   const props = defineProps({
      name: { type: String },
   });

   // Resolve the path relative to the current component file
   const source = new URL(`../assets/${props.name}.svg`, import.meta.url).href;
</script>
