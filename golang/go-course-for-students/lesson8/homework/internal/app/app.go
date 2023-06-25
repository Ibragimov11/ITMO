package app

import (
	"context"
	"homework8/internal/adapters/adrepo"
	"homework8/internal/adapters/userrepo"
	"homework8/internal/model"
	"strings"
	"time"
)

type App interface {
	CreateAd(ctx context.Context, title string, text string, userId int64) (*model.Ad, error)
	ChangeAdStatus(ctx context.Context, adId int64, userID int64, published bool) (*model.Ad, error)
	UpdateAd(ctx context.Context, adId int64, userID int64, title string, text string) (*model.Ad, error)

	CreateUser(ctx context.Context, nickname string, email string) (*model.User, error)
	UpdateUser(ctx context.Context, userID int64, nickname string, email string) (*model.User, error)

	GetAdById(ctx context.Context, adID int64) (*model.Ad, error)
	GetAllAdsByFilter(ctx context.Context, adFilter model.AdFilter) ([]*model.Ad, error)
	GetAdsByTitle(ctx context.Context, title string) ([]*model.Ad, error)

	GetUserById(ctx context.Context, userId int64) (*model.User, error)
	GetAllUsers(ctx context.Context) ([]*model.User, error)
}

type appImpl struct {
	adRepository   adrepo.AdRepository
	userRepository userrepo.UserRepository
}

func NewApp(adRepository adrepo.AdRepository, userRepository userrepo.UserRepository) App {
	return &appImpl{adRepository, userRepository}
}

func (a *appImpl) CreateAd(ctx context.Context, title string, text string, userId int64) (*model.Ad, error) {
	user, err := a.userRepository.GetUserById(ctx, userId)
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

func (a *appImpl) ChangeAdStatus(ctx context.Context, adId int64, userId int64, published bool) (*model.Ad, error) {
	user, err := a.userRepository.GetUserById(ctx, userId)
	if err != nil {
		return nil, err
	}

	ad, err := a.adRepository.GetAdById(ctx, adId)
	if err != nil {
		return nil, err
	}

	if ad.AuthorID != user.ID {
		return nil, model.ErrNotAuthor
	}

	ad.Published = published
	ad.UpdateDate = time.Now().UTC()

	return ad, nil
}

func (a *appImpl) UpdateAd(ctx context.Context, adId int64, userId int64, title string, text string) (*model.Ad, error) {
	user, err := a.userRepository.GetUserById(ctx, userId)
	if err != nil {
		return nil, err
	}

	ad, err := a.adRepository.GetAdById(ctx, adId)
	if err != nil {
		return nil, err
	}

	if ad.AuthorID != user.ID {
		return nil, model.ErrNotAuthor
	}

	ad.Title = title
	ad.Text = text
	ad.UpdateDate = time.Now().UTC()

	return ad, nil
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

func (a *appImpl) UpdateUser(ctx context.Context, userID int64, nickname string, email string) (*model.User, error) {
	user, err := a.userRepository.GetUserById(ctx, userID)
	if err != nil {
		return nil, err
	}

	user.Nickname = nickname
	user.Email = email

	return user, nil
}

func (a *appImpl) GetAdById(ctx context.Context, adID int64) (*model.Ad, error) {
	return a.adRepository.GetAdById(ctx, adID)
}

func (a *appImpl) GetAllAdsByFilter(ctx context.Context, adFilter model.AdFilter) ([]*model.Ad, error) {
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

func (a *appImpl) GetUserById(ctx context.Context, userId int64) (*model.User, error) {
	return a.userRepository.GetUserById(ctx, userId)
}

func (a *appImpl) GetAllUsers(ctx context.Context) ([]*model.User, error) {
	return a.userRepository.GetAllUsers(ctx)
}
