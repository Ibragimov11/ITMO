package httpgin

import (
	"github.com/gin-gonic/gin"
	"homework9/internal/app"
)

func AppRouter(r *gin.RouterGroup, a app.App) {
	r.POST("/ads", createAd(a))
	r.GET("/ads", listAds(a))
	r.GET("/ads/:ad_id", getAd(a))
	r.GET("/ads/by_title/:title", getAdsByTitle(a))
	r.PUT("/ads/:ad_id", updateAd(a))
	r.PUT("/ads/:ad_id/status", changeAdStatus(a))
	r.DELETE("/ads/:ad_id", deleteAd(a))

	r.POST("/users", createUser(a))
	r.GET("/users", listUsers(a))
	r.GET("/users/:user_id", getUser(a))
	r.PUT("/users/:user_id", updateUser(a))
	r.DELETE("/users/:user_id", deleteUser(a))
}
