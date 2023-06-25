package httpgin

import (
	"errors"
	"github.com/gin-gonic/gin"
	"homework9/internal/app"
	"homework9/internal/model"
	"net/http"
	"strconv"
	"time"
)

func createAd(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		var reqBody createAdRequest
		if err := c.ShouldBindJSON(&reqBody); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		if err := reqBody.validate(); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		ad, err := a.CreateAd(c, reqBody.Title, reqBody.Text, reqBody.UserID)
		if err != nil {
			c.JSON(http.StatusInternalServerError, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, AdSuccessResponse(ad))
	}
}

func listAds(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		adFilter, err := parseAdQueryKeys(c)
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		ads, err := a.GetAllAds(c, adFilter)
		if err != nil {
			c.JSON(http.StatusInternalServerError, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, AdsListSuccessResponse(ads))
	}
}

func getAd(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		adID, err := strconv.Atoi(c.Param("ad_id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		ad, err := a.GetAd(c, int64(adID))
		if err != nil {
			c.JSON(http.StatusNotFound, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, AdSuccessResponse(ad))
	}
}

func getAdsByTitle(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		title := c.Param("title")
		if len(title) == 0 {
			c.JSON(http.StatusBadRequest, ErrorResponse(errors.New("searched title must not be empty")))
			return
		}

		ads, err := a.GetAdsByTitle(c, title)
		if err != nil {
			c.JSON(http.StatusInternalServerError, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, AdsListSuccessResponse(ads))
	}
}

func updateAd(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		var reqBody updateAdRequest
		if err := c.ShouldBindJSON(&reqBody); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
		}

		if err := reqBody.validate(); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
		}

		adID, err := strconv.Atoi(c.Param("ad_id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		ad, err := a.UpdateAd(c, int64(adID), reqBody.UserID, reqBody.Title, reqBody.Text)
		if errors.Is(err, app.ErrNotAuthor) {
			c.JSON(http.StatusForbidden, ErrorResponse(err))
			return
		} else if err != nil {
			c.JSON(http.StatusNotFound, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, AdSuccessResponse(ad))
	}
}

func changeAdStatus(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		var reqBody changeAdStatusRequest
		if err := c.ShouldBindJSON(&reqBody); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		adID, err := strconv.Atoi(c.Param("ad_id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		ad, err := a.ChangeAdStatus(c, int64(adID), reqBody.UserID, reqBody.Published)
		if errors.Is(err, app.ErrNotAuthor) {
			c.JSON(http.StatusForbidden, ErrorResponse(err))
			return
		} else if err != nil {
			c.JSON(http.StatusNotFound, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, AdSuccessResponse(ad))
	}
}

func deleteAd(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		var reqBody deleteAdRequest
		if err := c.ShouldBindJSON(&reqBody); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		adID, err := strconv.Atoi(c.Param("ad_id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		ad, err := a.DeleteAd(c, int64(adID), reqBody.UserID)
		if errors.Is(err, app.ErrNotAuthor) {
			c.JSON(http.StatusForbidden, ErrorResponse(err))
			return
		} else if err != nil {
			c.JSON(http.StatusNotFound, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, AdSuccessResponse(ad))
	}
}

func parseAdQueryKeys(c *gin.Context) (model.AdFilter, error) {
	adFilter := model.DefaultAdFilter()

	publishedOnlyKey := c.Query("published_only")
	if publishedOnlyKey != "" {
		publishedOnly, err := strconv.ParseBool(publishedOnlyKey)
		if err != nil {
			return model.AdFilter{}, err
		}
		adFilter.SetPublishedOnly(publishedOnly)
	}

	authorIDKey := c.Query("author_id")
	if authorIDKey != "" {
		authorID, err := strconv.Atoi(authorIDKey)
		if err != nil {
			return model.AdFilter{}, err
		}
		adFilter.SetAuthorID(int64(authorID))
	}

	creationDateKey := c.Query("creation_date")
	if creationDateKey != "" {
		creationDate, err := time.Parse(time.DateOnly, creationDateKey)
		if err != nil {
			return model.AdFilter{}, err
		}
		adFilter.SetCreationDate(creationDate)
	}

	return adFilter, nil
}

func createUser(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		var reqBody UserRequest
		if err := c.ShouldBindJSON(&reqBody); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		user, err := a.CreateUser(c, reqBody.Nickname, reqBody.Email)
		if err != nil {
			c.JSON(http.StatusInternalServerError, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, UserSuccessResponse(user))
	}
}

func listUsers(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		users, err := a.GetAllUsers(c)
		if err != nil {
			c.JSON(http.StatusInternalServerError, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, UsersListSuccessResponse(users))
	}
}

func getUser(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		userID, err := strconv.Atoi(c.Param("user_id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		user, err := a.GetUser(c, int64(userID))
		if err != nil {
			c.JSON(http.StatusNotFound, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, UserSuccessResponse(user))
	}
}

func updateUser(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		var reqBody UserRequest
		if err := c.ShouldBindJSON(&reqBody); err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
		}

		userID, err := strconv.Atoi(c.Param("user_id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		user, err := a.UpdateUser(c, int64(userID), reqBody.Nickname, reqBody.Email)
		if err != nil {
			c.JSON(http.StatusNotFound, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, UserSuccessResponse(user))
	}
}

func deleteUser(a app.App) gin.HandlerFunc {
	return func(c *gin.Context) {
		userID, err := strconv.Atoi(c.Param("user_id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, ErrorResponse(err))
			return
		}

		user, err := a.DeleteUser(c, int64(userID))
		if err != nil {
			c.JSON(http.StatusNotFound, ErrorResponse(err))
			return
		}

		c.JSON(http.StatusOK, UserSuccessResponse(user))
	}
}
