<template>
	<v-app>
		<v-container
			class="fill-height d-flex flex-column align-center justify-center">
			<v-card class="pa-8 text-center" elevation="3">
				<v-card-title>
					<h2>Welcome to My App</h2>
				</v-card-title>
				<v-card-subtitle>
					<p>Please sign in to continue</p>
				</v-card-subtitle>
				<v-card-text>
					<GoogleLogin :callback="callback" prompt auto-login />
				</v-card-text>
			</v-card>
		</v-container>
	</v-app>
</template>

<script setup>
	import { ref, onMounted } from "vue";
	import { googleOneTap } from "vue3-google-login";
	import { decodeCredential } from "vue3-google-login";
	import { loginViaGoogleIdToken } from "@/api/api";
	import { useUserStore } from "@/stores/user";
	import { useRouter } from "vue-router";

	const userStore = useUserStore();
	const router = useRouter();

	// User data and authentication state
	const authenticated = ref(false);
	const user = ref({ name: "", email: "" });

	const callback = (response) => {
		console.log("Handle the response", response);
		const userData = decodeCredential(response.credential);
		console.log("Handle the userData", userData);
		loginViaGoogleIdToken(response.credential).then((response) => {
			response = response.data;
			userStore.setUserInfo(
				response.name,
				response.email,
				response.picture,
				response.serverAuthToken,
			);
			router.push({ name: "GroupsList" });
		});
	};
</script>

<style scoped>
	html,
	body,
	#app {
		height: 100%;
	}

	.fill-height {
		min-height: 100vh;
	}
</style>
