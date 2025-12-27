/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from "vue-router/auto";
import { setupLayouts } from "virtual:generated-layouts";
import Login from "@/layouts/Login.vue";
import { useUserStore } from "@/stores/user";
import GroupDashboard from "@/layouts/GroupDashboard.vue";
import Expenses from "@/layouts/Expenses.vue";
import GroupsList from "@/layouts/GroupsList.vue";
import Categories from "@/layouts/Categories.vue";
import Tags from "@/layouts/Tags.vue";
import NewExpense from "@/layouts/NewExpense.vue";
import Stats from "@/layouts/Stats.vue";
import EditExpense from "@/layouts/EditExpense.vue";
import GroupSettingsLayout from "@/layouts/GroupSettingsLayout.vue";
import GroupSettingsMenu from "@/layouts/GroupSettingsMenu.vue";
import Members from "@/layouts/Members.vue";
import RecurringExpenses from "@/layouts/RecurringExpenses.vue";
import NewRecurringExpense from "@/layouts/NewRecurringExpense.vue";
import EditRecurringExpense from "@/layouts/EditRecurringExpense.vue";

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: setupLayouts([
		{
			path: "/",
			name: "Login",
			component: Login,
		},
		{
			path: "/groups",
			name: "GroupsList",
			component: GroupsList,
			requiresAuth: true,
		},
		{
			path: "/groups/:groupId",
			name: "GroupDashboard",
			component: GroupDashboard,
			requiresAuth: true,
			children: [
				{
					path: "expenses",
					alias: [""],
					name: "Expenses",
					component: Expenses,
				},
				{
					path: "new-expense",
					name: "NewExpense",
					component: NewExpense,
				},
				{
					path: "edit-expense/:expenseId",
					name: "EditExpense",
					component: EditExpense,
				},
				{
					path: "recurring-expenses",
					name: "RecurringExpenses",
					component: RecurringExpenses,
				},
				{
					path: "new-recurring-expense",
					name: "NewRecurringExpense",
					component: NewRecurringExpense,
				},
				{
					path: "edit-recurring-expense/:expenseId",
					name: "EditRecurringExpense",
					component: EditRecurringExpense,
				},
				{
					path: "stats",
					name: "Stats",
					component: Stats,
				},
				{
					path: "settings",
					name: "GroupSettings",
					component: GroupSettingsLayout,
					children: [
						{
							path: "",
							name: "GroupSettingsMenu",
							component: GroupSettingsMenu,
						},
						{
							path: "members",
							name: "Members",
							component: Members,
						},
						{
							path: "categories",
							name: "Categories",
							component: Categories,
						},
						{
							path: "tags",
							name: "Tags",
							component: Tags,
						},
					],
				},
			],
		},
	]),
});

router.beforeEach((to, from, next) => {
	const userStore = useUserStore();
	userStore.loadUserInfoFromStorage();
	// If trying to access login page but already logged in, redirect to dashboard
	if (to.name === "Login" && userStore.isAuthenticated) {
		next({ name: "GroupsList" });
	} else if (to.requiresAuth && !userStore.isAuthenticated) {
		// If trying to access a protected page and not authenticated, go to login
		next({ name: "Login" });
	} else {
		// Allow the navigation
		next();
	}
});

// Workaround for https://github.com/vitejs/vite/issues/11804
router.onError((err, to) => {
	if (err?.message?.includes?.("Failed to fetch dynamically imported module")) {
		if (!localStorage.getItem("vuetify:dynamic-reload")) {
			console.log("Reloading page to fix dynamic import error");
			localStorage.setItem("vuetify:dynamic-reload", "true");
			location.assign(to.fullPath);
		} else {
			console.error("Dynamic import error, reloading page did not fix it", err);
		}
	} else {
		console.error(err);
	}
});

router.isReady().then(() => {
	localStorage.removeItem("vuetify:dynamic-reload");
});

export default router;
