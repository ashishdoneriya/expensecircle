module.exports = {
	devServer: {
		port: 3000,
		/*host: 'localhost', */
		proxy: {
			"/api": {
				target: "http://localhost:8080", // Your Spring Boot backend URL
				changeOrigin: true,
			},
		},
	},
//	productionSourceMap: true
};
