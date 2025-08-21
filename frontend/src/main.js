import App from "./App.vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import "element-plus/theme-chalk/dark/css-vars.css";
import { createApp } from "vue";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import pinia from "@/stores";
import router from "@/router";
import vue3GoogleLogin from "vue3-google-login";
import { useDark } from "@vueuse/core";

const app = createApp(App);

useDark({
	storageKey: "isDark",
});

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component);
}
//app.use(ElementPlus, { size: 'small', zIndex: 3000 })
app.use(ElementPlus).use(router).use(pinia).use(vue3GoogleLogin, {
	clientId:
		"378687249491-55qmh3gcrv72dbhqf67bh1o09rgpr9hh.apps.googleusercontent.com",
});

app.mount("#app");
