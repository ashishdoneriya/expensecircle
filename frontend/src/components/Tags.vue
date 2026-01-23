<template>
	<div class="flex-container">
		<div>
			<el-tag
				v-for="(tag, index) in tags"
				:key="tag.tagId || tag.tagName"
				closable
				:type="color[index % color.length]"
				@close="handleClose(index)"
				class="marginRight10"
				size="small">
				{{ tag.tagName }}
			</el-tag>

			<el-autocomplete
				v-if="inputVisible"
				ref="InputRef"
				v-model="inputValue"
				:fetch-suggestions="querySearch"
				placeholder="Enter tag name"
				@select="handleSelect"
				@keyup.enter="handleInputConfirm"
				@blur="handleInputConfirm"
				style="margin-top: 10px"
				:popper-options="{
					placement: 'top',
				}" />

			<el-button v-else class="button-new-tag" size="small" @click="showInput">
				+ New Tag
			</el-button>
		</div>
	</div>
</template>

<script setup>
	import { nextTick, ref } from "vue";
	import { useGroupStore } from "@/stores/group";

	const color = ["primary", "success", "info", "warning", "danger"];
	const group = useGroupStore();

	// defineModel links to the parent's v-model="form.tags"
	const tags = defineModel({ type: Array, default: () => [] });

	const inputValue = ref("");
	const inputVisible = ref(false);
	const InputRef = ref(null);

	// 1. Logic to filter suggestions from the store
	function querySearch(queryString, cb) {
		const allTags = group.tags || [];
		const results = queryString
			? allTags.filter((t) =>
					t.tagName.toLowerCase().includes(queryString.toLowerCase()),
				)
			: allTags;

		// Element Plus autocomplete requires a 'value' field for the display
		cb(results.map((t) => ({ value: t.tagName, tagId: t.tagId })));
	}

	// 2. Logic when a user clicks a suggestion from the dropdown
	function handleSelect(item) {
		addTag({ tagId: item.tagId, tagName: item.value });
	}

	// 3. Logic when user hits Enter or moves focus away
	function handleInputConfirm() {
		if (inputValue.value) {
			// Check if what they typed matches an existing tag in the store
			const existing = group.tags.find((t) => t.tagName === inputValue.value);
			if (existing) {
				addTag({ tagId: existing.tagId, tagName: existing.tagName });
			} else {
				addTag({ tagId: "", tagName: inputValue.value });
			}
		}
		closeInput();
	}

	function addTag(tagObject) {
		// Avoid duplicate tags in the same expense
		const exists = tags.value.some((t) => t.tagName === tagObject.tagName);
		if (!exists) {
			tags.value.push(tagObject);
		}
		closeInput();
	}

	function closeInput() {
		inputVisible.value = false;
		inputValue.value = "";
	}

	function handleClose(index) {
		tags.value.splice(index, 1);
	}

	function showInput() {
		inputVisible.value = true;
		nextTick(() => {
			// el-autocomplete has its own focus method
			if (InputRef.value) {
				InputRef.value.focus();
			}
		});
	}
</script>
