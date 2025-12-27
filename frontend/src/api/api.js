import axios from "axios";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";

const apiClient = axios.create({
	baseURL: "/api/",
	headers: {
		"Content-Type": "application/json",
	},
});

// Add request interceptor
apiClient.interceptors.request.use(
	(config) => {
		const userStore = useUserStore();
		if (userStore.isAuthenticated) {
			config.headers["Authorization"] =
				`Bearer ${userStore.serverAuthToken}`;
		}
		return config;
	},
	(error) => {
		return Promise.reject(error);
	},
);

// Add response interceptor to catch authentication errors
apiClient.interceptors.response.use(
	(response) => response, // Just return the response if everything is OK
	(error) => {
		if (error.response && error.response.status === 401) {
			// If 401 error is returned, logout and redirect to login page
			const userStore = useUserStore(); // Get the Pinia user store
			userStore.logout(); // Clear the user state and remove tokens
			const router = useRouter();
			// Redirect to login page
			router.push({ name: "Login" });
		}
		return Promise.reject(error); // Pass error for further handling if needed
	},
);

export function loginViaGoogleIdToken(googleIdToken) {
	return apiClient.post("/login/google-id-token", {
		googleIdToken: googleIdToken,
	});
}

export function addGroup(groupName) {
	return apiClient.post("/groups", {
		name: groupName,
	});
}

export function deleteGroup(groupId) {
	return apiClient.delete(`/groups/${groupId}`);
}

export function changeGroupName(groupId, groupName) {
	return apiClient.post(`/groups/${groupId}`, {
		name: groupName,
	});
}

export function getGroups() {
	return apiClient.get("/groups/me");
}

export function getGroupInfo(groupId) {
	return apiClient.get(`/groups/${groupId}`);
}

export function getCategories(groupId) {
	return apiClient.get(`/groups/${groupId}/categories`);
}

export function addCategory(groupId, categoryName) {
	return apiClient.post(`/groups/${groupId}/categories`, {
		name: categoryName,
	});
}

export function renameCategory(groupId, categoryId, newCategoryName) {
	return apiClient.put(`/groups/${groupId}/categories/${categoryId}`, {
		name: newCategoryName,
	});
}

export function deleteCategory(groupId, categoryId) {
	return apiClient.delete(`/groups/${groupId}/categories/${categoryId}`);
}

export function getTags(groupId) {
	return apiClient.get(`/groups/${groupId}/tags`);
}

export function addTag(groupId, tagName) {
	return apiClient.post(`/groups/${groupId}/tags`, {
		name: tagName,
	});
}

export function renameTag(groupId, tagId, newTagName) {
	return apiClient.put(`/groups/${groupId}/tags/${tagId}`, {
		name: newTagName,
	});
}

export function deleteTag(groupId, tagId) {
	return apiClient.delete(`/groups/${groupId}/tags/${tagId}`);
}

export function addExpense(groupId, obj) {
	return apiClient.post(`/groups/${groupId}/expenses`, obj);
}

export function updateExpense(groupId, expenseId, obj) {
	return apiClient.put(`/groups/${groupId}/expenses/${expenseId}`, obj);
}

export function deleteExpense(groupId, expenseId) {
	return apiClient.delete(`/groups/${groupId}/expenses/${expenseId}`);
}

export function getExpense(groupId, expenseId) {
	return apiClient.get(`/groups/${groupId}/expenses/${expenseId}`);
}

export function getExpenseAuditDetails(groupId, expenseId) {
	return apiClient.get(`/groups/${groupId}/expenses/${expenseId}/audit-details`);
}

export function getExpenses(groupId, year, month, dayOfMonth, categoryId) {
	if (!categoryId) {
		categoryId = '';
	}
	return apiClient.get(
		`/groups/${groupId}/expenses?year=${year}&month=${month}&dayOfMonth=${dayOfMonth}&categoryId=${categoryId}`,
	);
}

export function getDailyTotal(groupId, year, month, dayOfMonth, categoryId) {
	if (!categoryId) {
		categoryId = '';
	}
	return apiClient.get(
		`/groups/${groupId}/stats/daily-total?year=${year}&month=${month}&dayOfMonth=${dayOfMonth}&categoryId=${categoryId}`,
	);
}

export function getMonthlyTotal(groupId, year, month, categoryId) {
	if (!categoryId) {
		categoryId = '';
	}
	return apiClient.get(
		`/groups/${groupId}/stats/monthly-total?year=${year}&month=${month}&categoryId=${categoryId}`,
	);
}

export function getMonthlyBreakdownByDay(groupId, year, month, categoryId) {
	if (!categoryId) {
		categoryId = '';
	}
	return apiClient.get(
		`/groups/${groupId}/stats/monthly-breakdown-by-day?year=${year}&month=${month}&categoryId=${categoryId}`,
	);
}

export function getMonthlyBreakdownByCategory(groupId, year, month) {
	return apiClient.get(
		`/groups/${groupId}/stats/monthly-breakdown-by-category?year=${year}&month=${month}`
	)
}

export function getMonthlyBreakdownByTag(groupId, year, month) {
	return apiClient.get(
		`/groups/${groupId}/stats/monthly-breakdown-by-tag?year=${year}&month=${month}`
	)
}

export function getYearlyTotal(groupId, year) {
	return apiClient.get(
		`/groups/${groupId}/stats/yearly-total?year=${year}`,
	);
}

export function getYearlyBreakdownByMonth(groupId, year) {
	return apiClient.get(
		`/groups/${groupId}/stats/yearly-breakdown-by-month?year=${year}`
	)
}

export function getYearlyBreakdownByCategory(groupId, year) {
	return apiClient.get(
		`/groups/${groupId}/stats/yearly-breakdown-by-category?year=${year}`
	)
}

export function getYearlyBreakdownByTag(groupId, year) {
	return apiClient.get(
		`/groups/${groupId}/stats/yearly-breakdown-by-tag?year=${year}`
	)
}

export function changeCategoriesOrder(groupId, categories) {
	return apiClient.post(`/groups/${groupId}/categories/change-categories-order`, categories);
}

export function changeTagsOrder(groupId, tags) {
	return apiClient.post(`/groups/${groupId}/tags/change-tags-order`, tags);
}

export function getGroupMembers(groupId) {
	return apiClient.get(`/groups/${groupId}/members`);
}

export function removeUser(groupId, userId) {
	return apiClient.post(`/groups/${groupId}/remove-user`, {
		userId: userId
	});
}

export function addGroupMember(groupId, email) {
	return apiClient.post(`/groups/${groupId}/add-user`, {
		email: email
	});
}

export function changeUserPermission(groupId, userId, role) {
	return apiClient.post(`/groups/${groupId}/change-user-permissions`, {
		userId: userId,
		role: role
	});
}

export function exitFromGroup(groupId) {
	return apiClient.post(`/groups/${groupId}/exit`);
}

export function addRecurringExpense(groupId, obj) {
	return apiClient.post(`/groups/${groupId}/recurring-expenses`, obj);
}

export function updateRecurringExpense(groupId, recurringId, obj) {
	return apiClient.put(`/groups/${groupId}/recurring-expenses/${recurringId}`, obj);
}

export function getRecurringExpense(groupId, recurringId) {
	return apiClient.get(`/groups/${groupId}/recurring-expenses/${recurringId}`);
}

export function deleteRecurringExpense(groupId, recurringId) {
	return apiClient.delete(`/groups/${groupId}/recurring-expenses/${recurringId}`);
}

export function listRecurringExpense(groupId, recurringId) {
	return apiClient.delete(`/groups/${groupId}/recurring-expenses/${recurringId}`);
}
export function getAllRecurringExpenses(groupId) {
	return apiClient.get(`/groups/${groupId}/recurring-expenses`);
}

export function getRecurringExpenseAuditDetails(groupId, recurringId) {
	return apiClient.get(`/groups/${groupId}/recurring-expenses/${recurringId}/audit-details`);
}
