module.exports = {
	devServer: {
		port: 3000,
		/*host: 'localhost', */
		proxy: {
			"/api": {
				target: "https://expensecircle.com", // Your Spring Boot backend URL
				changeOrigin: true,
			},
		},
	},
//	productionSourceMap: true
};
