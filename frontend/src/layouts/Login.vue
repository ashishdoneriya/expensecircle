<template>
	<el-container>
		<el-header>
			<el-row style="margin: 10px 10px" class="flex-container">
				<div>
					<el-link>Expense Circle</el-link>
				</div>
				<div>
					<el-space spacer="|">
						<el-link>Contact</el-link>
						<el-link>Screenshots</el-link>
						<el-link>FAQ</el-link>
					</el-space>
				</div>
			</el-row>
			<el-divider style="margin: 0px 0px" />
		</el-header>
		<el-main>
			<el-row justify="center">
				<el-col :xs="24" :sm="22" :md="15" :lg="12" :xl="9">
					<el-row justify="center">
						<h1>Expense Circle</h1>
					</el-row>
					<el-row justify="center">
						<p>
							Easily manage expenses with your family and friends. Stay
							organized, track spending effortlessly, and keep your
							shared finances simple. Designed to bring clarity and ease
							to your family's and group's financial life.
						</p>
					</el-row>
					<el-row justify="center">
						<GoogleLogin :callback="callback" prompt auto-login />
					</el-row>
				</el-col>
			</el-row>
		</el-main>
	</el-container>
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
		const userData = decodeCredential(response.credential);
		loginViaGoogleIdToken(response.credential).then((response) => {
			response = response.data;
			userStore.setUserInfo(
				response.userId,
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
	.el-link {
		font-size: 1em;
	}
	.el-text {
		color: var(--el-text-color-primary);
		font-size: 2em;
		font-weight: bold;
	}
	h1 {
		color: var(--el-color-primary);
		font-size: 3em;
		margin-top: 2em;
	}
	p {
		font-size: 1em;
		line-height: 1.5em !important;
		word-spacing: 1px;
		color: #333;
		margin-bottom: 3em;
		text-align: justify;
	}
</style>
