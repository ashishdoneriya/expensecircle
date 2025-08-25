// Plugins
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import Fonts from "unplugin-fonts/vite";
import Layouts from "vite-plugin-vue-layouts";
import Vue from "@vitejs/plugin-vue";
import VueRouter from "unplugin-vue-router/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import { VitePWA } from "vite-plugin-pwa";

// Utilities
import { defineConfig } from "vite";
import { fileURLToPath, URL } from "node:url";

// https://vitejs.dev/config/
export default defineConfig({
	build: {
		minify: process.env.NODE_ENV === "production",
		sourcemap: false,
		cssCodeSplit: true,
		// Use multi-threading in production builds
		terserOptions: {
			format: {
				comments: false,
			},
			compress: {
				drop_console: true,
			},
		},
	},
	plugins: [
		Layouts({
			importMode: (name) => {
				// Always use sync import mode
				return "sync";
			},
		}),
		VueRouter(),
		Layouts(),
		Vue(),
		Components({
			resolvers: [ElementPlusResolver()],
		}),
		Fonts({
			google: {
				families: [
					{
						name: "Roboto",
						styles: "wght@100;300;400;500;700;900",
					},
				],
			},
		}),
		AutoImport({
			imports: [
				"vue",
				"vue-router",
				{
					"element-plus": [
						"ElMessage", // Auto-import Element Plus functions like ElMessage
					],
				},
			],
			eslintrc: {
				enabled: true,
			},
			resolvers: [ElementPlusResolver()],

			vueTemplate: true,
		}),
		VitePWA({
			registerType: "autoUpdate",
			manifest: {
				name: "Expense Circle",
				short_name: "Expense Circle",
				start_url: "/",
				display_override: [
					"window-controls-overlay",
					"standalone",
					"browser",
				],
				background_color: "#ffffff",
				theme_color: "#000000",
				icons: [
					{
						src: "/wallet.png",
						sizes: "192x192",
						type: "image/png",
					},
					{
						src: "/wallet.png",
						sizes: "512x512",
						type: "image/png",
					},
				],
			},
		}),
	],
	define: { "process.env": {} },
	resolve: {
		alias: {
			"@": fileURLToPath(new URL("./src", import.meta.url)),
		},
		extensions: [".js", ".json", ".jsx", ".mjs", ".ts", ".tsx", ".vue"],
	},
	server: {
		port: 3000,
		proxy: {
			"/api": {
				target: "https://expensecircle.com", // Your Spring Boot backend URL
				changeOrigin: true,
				//       rewrite: (path) => path.replace(/^\/api/, ''),  // Optional: Rewrites '/api' if necessary
			},
		},
	},
});
