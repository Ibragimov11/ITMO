package grpc

import (
	"context"
	"errors"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"google.golang.org/protobuf/types/known/emptypb"
	"homework10/internal/app"
	"homework10/internal/model"
)

type AdService struct {
	app app.App
}

func NewService(a app.App) AdService {
	return AdService{a}
}

func (s AdService) CreateAd(ctx context.Context, request *CreateAdRequest) (*AdResponse, error) {
	if err := ValidateAdRequest(request.Title, request.Text); err != nil {
		return nil, status.Error(codes.InvalidArgument, err.Error())
	}

	ad, err := s.app.CreateAd(ctx, request.Title, request.Text, request.UserId)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return adResponse(ad), nil
}

func (s AdService) ListAds(ctx context.Context, request *FilteredListAdsRequest) (*ListAdResponse, error) {
	adFilter := model.DefaultAdFilter()
	if request.PublishedOnlyChecked {
		adFilter.SetPublishedOnly(request.PublishedOnly)
	}
	if request.AuthorIdChecked {
		adFilter.SetAuthorID(request.AuthorId)
	}
	if request.CreationDateChecked {
		adFilter.SetCreationDate(request.CreationDate.AsTime())
	}

	ads, err := s.app.GetAllAds(ctx, adFilter)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return listAdResponse(ads), nil
}

func (s AdService) GetAd(ctx context.Context, request *GetAdRequest) (*AdResponse, error) {
	ad, err := s.app.GetAd(ctx, request.Id)
	if err != nil {
		return nil, status.Error(codes.NotFound, err.Error())
	}

	return adResponse(ad), nil
}

func (s AdService) GetAdsByTitle(ctx context.Context, request *GetAdsByTitleRequest) (*ListAdResponse, error) {
	ads, err := s.app.GetAdsByTitle(ctx, request.Title)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return listAdResponse(ads), nil
}

func (s AdService) UpdateAd(ctx context.Context, request *UpdateAdRequest) (*AdResponse, error) {
	if err := ValidateAdRequest(request.Title, request.Text); err != nil {
		return nil, status.Error(codes.InvalidArgument, err.Error())
	}

	ad, err := s.app.UpdateAd(ctx, request.AdId, request.UserId, request.Title, request.Text)
	if errors.Is(err, app.ErrNotAuthor) {
		return nil, status.Error(codes.PermissionDenied, err.Error())
	} else if err != nil {
		return nil, status.Error(codes.NotFound, err.Error())
	}

	return adResponse(ad), nil
}

func (s AdService) ChangeAdStatus(ctx context.Context, request *ChangeAdStatusRequest) (*AdResponse, error) {
	ad, err := s.app.ChangeAdStatus(ctx, request.AdId, request.UserId, request.Published)
	if errors.Is(err, app.ErrNotAuthor) {
		return nil, status.Error(codes.PermissionDenied, err.Error())
	} else if err != nil {
		return nil, status.Error(codes.NotFound, err.Error())
	}

	return adResponse(ad), nil
}

func (s AdService) DeleteAd(ctx context.Context, request *DeleteAdRequest) (*AdResponse, error) {
	ad, err := s.app.DeleteAd(ctx, request.AdId, request.UserId)
	if errors.Is(err, app.ErrNotAuthor) {
		return nil, status.Error(codes.PermissionDenied, err.Error())
	} else if err != nil {
		return nil, status.Error(codes.NotFound, err.Error())
	}

	return adResponse(ad), nil
}

func (s AdService) CreateUser(ctx context.Context, request *CreateUserRequest) (*UserResponse, error) {
	user, err := s.app.CreateUser(ctx, request.Nickname, request.Email)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return userResponse(user), nil
}

func (s AdService) ListUsers(ctx context.Context, _ *emptypb.Empty) (*ListUserResponse, error) {
	users, err := s.app.GetAllUsers(ctx)
	if err != nil {
		return nil, status.Error(codes.Internal, err.Error())
	}

	return listUserResponse(users), nil
}

func (s AdService) GetUser(ctx context.Context, request *GetUserRequest) (*UserResponse, error) {
	user, err := s.app.GetUser(ctx, request.Id)
	if err != nil {
		return nil, status.Error(codes.NotFound, err.Error())
	}

	return userResponse(user), nil
}

func (s AdService) UpdateUser(ctx context.Context, request *UpdateUserRequest) (*UserResponse, error) {
	user, err := s.app.UpdateUser(ctx, request.Id, request.Nickname, request.Email)
	if err != nil {
		return nil, status.Error(codes.NotFound, err.Error())
	}

	return userResponse(user), nil
}

func (s AdService) DeleteUser(ctx context.Context, request *DeleteUserRequest) (*UserResponse, error) {
	user, err := s.app.DeleteUser(ctx, request.Id)
	if err != nil {
		return nil, status.Error(codes.NotFound, err.Error())
	}

	return userResponse(user), nil
}
