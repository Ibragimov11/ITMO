package httpgin

import (
	"errors"
	validator "github.com/Ibragimov11/validator"
	"github.com/gin-gonic/gin"
	"homework9/internal/model"
	"time"
)

type createAdRequest struct {
	Title  string `json:"title" validate:"max:100"`
	Text   string `json:"text" validate:"max:500"`
	UserID int64  `json:"user_id"`
}

type updateAdRequest struct {
	Title  string `json:"title" validate:"max:100"`
	Text   string `json:"text" validate:"max:500"`
	UserID int64  `json:"user_id"`
}

type changeAdStatusRequest struct {
	Published bool  `json:"published"`
	UserID    int64 `json:"user_id"`
}

type deleteAdRequest struct {
	UserID int64 `json:"user_id"`
}

type adResponse struct {
	ID         int64     `json:"id"`
	Title      string    `json:"title"`
	Text       string    `json:"text"`
	AuthorID   int64     `json:"author_id"`
	Published  bool      `json:"published"`
	CreateDate time.Time `json:"create_date" binding:"required"`
	UpdateDate time.Time `json:"update_date" binding:"required"`
}

func (r createAdRequest) validate() error {
	if r.Title == "" {
		return errors.New("title must be non-empty")
	}
	if r.Text == "" {
		return errors.New("text must be non-empty")
	}

	return validator.Validate(r)
}

func (r updateAdRequest) validate() error {
	if r.Title == "" {
		return errors.New("title must be non-empty")
	}
	if r.Text == "" {
		return errors.New("text must be non-empty")
	}

	return validator.Validate(r)
}

func newAdResponse(ad *model.Ad) adResponse {
	return adResponse{
		ID:         ad.ID,
		Title:      ad.Title,
		Text:       ad.Text,
		AuthorID:   ad.AuthorID,
		Published:  ad.Published,
		CreateDate: ad.CreateDate,
		UpdateDate: ad.UpdateDate,
	}
}

type UserRequest struct {
	Nickname string `json:"nickname"`
	Email    string `json:"email"`
}

type UserResponse struct {
	ID       int64  `json:"id"`
	Nickname string `json:"nickname"`
	Email    string `json:"email"`
}

func newUserResponse(user *model.User) UserResponse {
	return UserResponse{
		ID:       user.ID,
		Nickname: user.Nickname,
		Email:    user.Email,
	}
}

func AdSuccessResponse(ad *model.Ad) *gin.H {
	return &gin.H{
		"data":  newAdResponse(ad),
		"error": nil,
	}
}

func UserSuccessResponse(user *model.User) *gin.H {
	return &gin.H{
		"data":  newUserResponse(user),
		"error": nil,
	}
}

func AdsListSuccessResponse(ads []*model.Ad) *gin.H {
	var adsList []adResponse
	for _, ad := range ads {
		adsList = append(adsList, newAdResponse(ad))
	}

	return &gin.H{
		"data":  adsList,
		"error": nil,
	}
}

func UsersListSuccessResponse(users []*model.User) *gin.H {
	var usersList []UserResponse
	for _, user := range users {
		usersList = append(usersList, newUserResponse(user))
	}
	return &gin.H{
		"data":  usersList,
		"error": nil,
	}
}

func ErrorResponse(err error) *gin.H {
	return &gin.H{
		"data":  nil,
		"error": err.Error(),
	}
}
