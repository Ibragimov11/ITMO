package grpc

import (
	"errors"
	"google.golang.org/protobuf/types/known/timestamppb"
	"homework10/internal/model"
)

func ValidateAdRequest(title, text string) error {
	if title == "" {
		return errors.New("title must be non-empty")
	}

	if len(title) > 100 {
		return errors.New("title len must be less then or equal 100")
	}

	if text == "" {
		return errors.New("text must be non-empty")
	}

	if len(text) > 100 {
		return errors.New("text len must be less then or equal t00")
	}

	return nil
}

func adResponse(ad *model.Ad) *AdResponse {
	return &AdResponse{
		Id:           ad.ID,
		Title:        ad.Title,
		Text:         ad.Text,
		AuthorId:     ad.AuthorID,
		Published:    ad.Published,
		CreationDate: timestamppb.New(ad.CreateDate),
		UpdateDate:   timestamppb.New(ad.UpdateDate),
	}
}

func listAdResponse(ads []*model.Ad) *ListAdResponse {
	res := make([]*AdResponse, 0, len(ads))
	for _, ad := range ads {
		res = append(res, adResponse(ad))
	}

	return &ListAdResponse{Ads: res}
}

func userResponse(user *model.User) *UserResponse {
	return &UserResponse{
		Id:       user.ID,
		Nickname: user.Nickname,
		Email:    user.Email,
	}
}

func listUserResponse(users []*model.User) *ListUserResponse {
	res := make([]*UserResponse, 0, len(users))
	for _, user := range users {
		res = append(res, userResponse(user))
	}

	return &ListUserResponse{Users: res}
}
