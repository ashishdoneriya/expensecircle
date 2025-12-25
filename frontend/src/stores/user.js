// src/stores/user.js
import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
	state: () => ({
		userId: "",
		name: "",
		email: "",
		picture: "",
		serverAuthToken: "",
		isAlreadyLoaded: false,
	}),
	actions: {
		// Set user information after login
		setUserInfo(userId, name, email, picture, serverAuthToken) {
			this.userId = userId;
			this.name = name;
			this.email = email;
			this.picture = picture;
			this.serverAuthToken = serverAuthToken;

			// Save to localStorage
			localStorage.setItem(
				"user",
				JSON.stringify({ userId, name, email, picture, serverAuthToken }),
			);
		},

		// Load user info from localStorage (for when the app is reloaded)
		loadUserInfoFromStorage() {
			if (this.isAlreadyLoaded) {
				return;
			}
			const storedUser = JSON.parse(localStorage.getItem("user"));
			if (storedUser) {
				this.userId = storedUser.userId;
				this.name = storedUser.name;
				this.email = storedUser.email;
				this.picture = storedUser.picture;
				this.serverAuthToken = storedUser.serverAuthToken;
			}
			this.isAlreadyLoaded = true;
		},

		// Clear user info on logout
		logout() {
			this.userId = "";
			this.name = "";
			this.email = "";
			this.picture = "";
			this.serverAuthToken = "";

			localStorage.removeItem("user");
		},
	},
	getters: {
		isAuthenticated: (state) => !!state.serverAuthToken, // Returns true if user is logged in
	},
});
