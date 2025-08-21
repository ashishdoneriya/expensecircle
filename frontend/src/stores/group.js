import { defineStore } from "pinia";
import * as api from "@/api/api";

export const useGroupStore = defineStore("groupStore", {
	state: () => ({
		groupId: 0,
		groupName: "",
		categories: [],
		categoriesMap: {},
		tags: [],
		tagsMap: {},
		groupInfo: null,
		isInitialized: false,
		members: []
	}),

	actions: {
		async initialize(groupId) {
			if (this.isInitialized) {
				return;
			}
			this.isInitialized = true;
			this.groupId = groupId;
			this.fetchCategories();
			this.fetchTags();
			await this.fetchGroupInfo();
			await this.fetchMembers();
		},

		async fetchGroupInfo() {
			this.groupInfo = (await api.getUserGroupInfo(this.groupId)).data;
			this.groupName = this.groupInfo.groupName;
		},

		async changeGroupName(groupName) {
			await this.changeGroupName(this.groupId, groupName);
			this.groupName = groupName;
		},

		async fetchMembers() {
			this.members = (await api.getGroupMembers(this.groupId)).data;
			let member;
			for (let i = 0; i < this.members.length; i++) {
				if (this.members[i].userId == this.groupInfo.userId) {
					member = this.members[i];
					this.members.splice(i, 1);
					break;
				}
			}
			this.members.unshift(member);
		},

		async changeUserPermission(member, role) {
			await api.changeUserPermission(this.groupId, member.userId, role);
			member.role = role;
		},

		async removeUser(member) {
			await api.removeUser(this.groupId, member.userId);
			for (let i = 0; i < this.members.length; i++) {
				if (this.members[i].userId == member.userId) {
					this.members.splice(i, 1);
				}
			}
		},

		async addMember(email) {
			await api.addGroupMember(this.groupId, email);
			this.members.push({
				"userId": email,
				"role": 'USER'
			})
		},

		getCategories() {
			return this.categories;
		},

		async fetchCategories() {
			try {
				this.categories = (await api.getCategories(this.groupId)).data;
				for (let category of this.categories) {
					this.categoriesMap[category.categoryId] = category.categoryName;
				}
			} catch (error) {
				console.error("Error fetching categories:", error);
			}
		},

		async addCategory(categoryName) {
			try {
				let response = await api.addCategory(this.groupId, categoryName);
				this.categories.push({
					categoryId: Number(response.data),
					categoryName: categoryName,
				});
				this.categoriesMap[response.data] = categoryName;
				return Number(response.data);
			} catch (error) {
				console.error("Error fetching categories:", error);
			}
		},

		async renameCategory(categoryId, newCategoryName) {
			try {
				await api.renameCategory(this.groupId, categoryId, newCategoryName);
				for (let category of this.categories) {
					if (category["categoryId"] == categoryId) {
						category["categoryName"] = newCategoryName;
					}
					this.categoriesMap[categoryId] = newCategoryName;
					return;
				}
			} catch (error) {
				console.error("Error updating category name:", error);
			}
		},

		async deleteCategory(categoryId) {
			try {
				await api.deleteCategory(this.groupId, categoryId);
				this.categoriesMap.delete(categoryId);
				for (let i = 0; i < this.categories.length; i++) {
					if (this.categories[i]["categoryId"] == categoryId) {
						this.categories.splice(i, 1);
						return;
					}
				}
			} catch (error) {
				console.error("Error updating category name:", error);
			}
		},

		getTags() {
			return this.tags;
		},

		async fetchTags() {
			try {
				this.tags = (await api.getTags(this.groupId)).data;
				for (let tag of this.tags) {
					this.tagsMap[tag.tagId] = tag.tagName;
				}
			} catch (error) {
				console.error("Error fetching tags:", error);
			}
		},

		async addTag(tagName) {
			try {
				let response = await api.addTag(this.groupId, tagName);
				this.tags.push({
					tagId: Number(response.data),
					tagName: tagName,
				});
				this.tagsMap[response.data] = tagName;
				return Number(response.data);
			} catch (error) {
				console.error("Error fetching tags:", error);
			}
		},

		async renameTag(tagId, newTagName) {
			try {
				await api.renameTag(this.groupId, tagId, newTagName);
				for (let tag of this.tags) {
					if (tag["tagId"] == tagId) {
						tag["tagName"] = newTagName;
					}
					this.tagsMap[tagId] = newTagName;
					return;
				}
			} catch (error) {
				console.error("Error updating tag name:", error);
			}
		},

		async deleteTag(tagId) {
			try {
				await api.deleteTag(this.groupId, tagId);
				this.tagsMap.delete(tagId);
				for (let i = 0; i < this.tags.length; i++) {
					if (this.tags[i]["tagId"] == tagId) {
						this.tags.splice(i, 1);
						return;
					}
				}
			} catch (error) {
				console.error("Error updating tag name:", error);
			}
		},

		clearInfo() {
			this.isInitialized = false;
			this.groupId = 0;
			this.groupInfo = null;
			this.categories = [];
			this.categoriesMap = {};
			this.tags = [];
			this.tagsMap = {};
		},
	},
});
