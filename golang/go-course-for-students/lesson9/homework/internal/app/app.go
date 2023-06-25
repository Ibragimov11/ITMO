package app

import (
	"context"
	"errors"
	"homework9/internal/adapters/adrepo"
	"homework9/internal/adapters/userrepo"
	"homework9/internal/model"
	"strings"
	"time"
)

type App interface {
	CreateAd(ctx context.Context, title string, text string, userID int64) (*model.Ad, error)
	GetAllAds(ctx context.Context, adFilter model.AdFilter) ([]*model.Ad, error)
	GetAd(ctx context.Context, adID int64) (*model.Ad, error)
	GetAdsByTitle(ctx context.Context, title string) ([]*model.Ad, error)
	UpdateAd(ctx context.Context, adID int64, userID int64, title string, text string) (*model.Ad, error)
	ChangeAdStatus(ctx context.Context, adID int64, userID int64, published bool) (*model.Ad, error)
	DeleteAd(ctx context.Context, adID int64, userID int64) (*model.Ad, error)

	CreateUser(ctx context.Context, nickname string, email string) (*model.User, error)
	GetAllUsers(ctx context.Context) ([]*model.User, error)
	GetUser(ctx context.Context, userID int64) (*model.User, error)
	UpdateUser(ctx context.Context, userID int64, nickname string, email string) (*model.User, error)
	DeleteUser(ctx context.Context, userID int64) (*model.User, error)
}

var ErrNotAuthor = errors.New("only its author has access to ad")

type appImpl struct {
	adRepository   adrepo.AdRepository
	userRepository userrepo.UserRepository
}

func NewApp(adRepository adrepo.AdRepository, userRepository userrepo.UserRepository) App {
	return &appImpl{adRepository, userRepository}
}

func (a *appImpl) CreateAd(ctx context.Context, title string, text string, userID int64) (*model.Ad, error) {
	user, err := a.userRepository.GetUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	dateTimeUtc := time.Now().UTC()
	ad := model.Ad{
		Title:      title,
		Text:       text,
		AuthorID:   user.ID,
		Published:  false,
		CreateDate: dateTimeUtc,
		UpdateDate: dateTimeUtc,
	}

	id, err := a.adRepository.AddAd(ctx, ad)
	if err != nil {
		return nil, err
	}

	ad.ID = id

	return &ad, nil
}

func (a *appImpl) GetAllAds(ctx context.Context, adFilter model.AdFilter) ([]*model.Ad, error) {
	allAds, err := a.adRepository.GetAllAds(ctx)
	if err != nil {
		return nil, err
	}

	filteredAds := make([]*model.Ad, 0, len(allAds))
	for _, ad := range allAds {
		if adFilter.Check(ad) {
			filteredAds = append(filteredAds, ad)
		}
	}

	return filteredAds, nil
}

func (a *appImpl) GetAd(ctx context.Context, adID int64) (*model.Ad, error) {
	return a.adRepository.GetAdByID(ctx, adID)
}

func (a *appImpl) GetAdsByTitle(ctx context.Context, title string) ([]*model.Ad, error) {
	allAds, err := a.adRepository.GetAllAds(ctx)
	if err != nil {
		return nil, err
	}

	adsByTitle := make([]*model.Ad, 0, len(allAds))
	for _, ad := range allAds {
		if ad.Published && strings.Contains(ad.Title, title) {
			adsByTitle = append(adsByTitle, ad)
		}
	}

	return adsByTitle, nil
}

func (a *appImpl) UpdateAd(ctx context.Context, adID int64, userID int64, title string, text string) (*model.Ad, error) {
	user, err := a.userRepository.GetUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	ad, err := a.adRepository.GetAdByID(ctx, adID)
	if err != nil {
		return nil, err
	}

	if ad.AuthorID != user.ID {
		return nil, ErrNotAuthor
	}

	ad.Title = title
	ad.Text = text
	ad.UpdateDate = time.Now().UTC()

	return ad, nil
}

func (a *appImpl) ChangeAdStatus(ctx context.Context, adID int64, userID int64, published bool) (*model.Ad, error) {
	user, err := a.userRepository.GetUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	ad, err := a.adRepository.GetAdByID(ctx, adID)
	if err != nil {
		return nil, err
	}

	if ad.AuthorID != user.ID {
		return nil, ErrNotAuthor
	}

	ad.Published = published
	ad.UpdateDate = time.Now().UTC()

	return ad, nil
}

func (a *appImpl) DeleteAd(ctx context.Context, adID int64, userID int64) (*model.Ad, error) {
	user, err := a.userRepository.GetUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	ad, err := a.adRepository.GetAdByID(ctx, adID)
	if err != nil {
		return nil, err
	}

	if ad.AuthorID != user.ID {
		return nil, ErrNotAuthor
	}

	return a.adRepository.DeleteAdByID(ctx, adID)
}

func (a *appImpl) CreateUser(ctx context.Context, nickname string, email string) (*model.User, error) {
	user := model.User{
		Nickname: nickname,
		Email:    email,
	}

	id, err := a.userRepository.AddUser(ctx, user)
	if err != nil {
		return nil, err
	}

	user.ID = id

	return &user, nil
}

func (a *appImpl) GetAllUsers(ctx context.Context) ([]*model.User, error) {
	return a.userRepository.GetAllUsers(ctx)
}

func (a *appImpl) GetUser(ctx context.Context, userID int64) (*model.User, error) {
	return a.userRepository.GetUserByID(ctx, userID)
}

func (a *appImpl) UpdateUser(ctx context.Context, userID int64, nickname string, email string) (*model.User, error) {
	user, err := a.userRepository.GetUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	user.Nickname = nickname
	user.Email = email

	return user, nil
}

func (a *appImpl) DeleteUser(ctx context.Context, userID int64) (*model.User, error) {
	user, err := a.userRepository.DeleteUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	if err := a.adRepository.DeleteAdsByAuthor(ctx, userID); err != nil {
		return nil, err
	}

	return user, nil
}
