package httpgin

import (
	"github.com/gin-gonic/gin"
	"homework8/internal/app"
)

func AppRouter(r *gin.RouterGroup, a app.App) {
	r.GET("/ads", listAds(a))
	r.GET("/ads/:ad_id", getAdByID(a))
	r.GET("/ads/from_title/:title", getAdsByTitle(a))
	r.POST("/ads", createAd(a))
	r.PUT("/ads/:ad_id/status", changeAdStatus(a))
	r.PUT("/ads/:ad_id", updateAd(a))
	r.GET("/users", listUsers(a))
	r.GET("/users/:user_id", getUserByID(a))
	r.POST("/users", createUser(a))
	r.PUT("/users/:user_id", updateUser(a))
}
