package app

import (
	"context"
	"homework6/internal/adapters/adrepo"
	"homework6/internal/ads"
)

type App interface {
	CreateAd(ctx context.Context, title string, text string, userId int64) (*ads.Ad, error)
	ChangeAdStatus(ctx context.Context, adId int64, userID int64, published bool) (*ads.Ad, error)
	UpdateAd(ctx context.Context, adId int64, userID int64, title string, text string) (*ads.Ad, error)
}

type adApp struct {
	repository adrepo.Repository
}

func NewApp(repo adrepo.Repository) App {
	return &adApp{repository: repo}
}

func (a *adApp) CreateAd(ctx context.Context, title string, text string, userId int64) (*ads.Ad, error) {
	ad := ads.Ad{
		Title:     title,
		Text:      text,
		AuthorID:  userId,
		Published: false,
	}

	id, err := a.repository.AddAd(ctx, ad)
	if err != nil {
		return nil, err
	}

	ad.ID = id

	return &ad, nil
}

func (a *adApp) ChangeAdStatus(ctx context.Context, adId int64, userID int64, published bool) (*ads.Ad, error) {
	ad, err := a.repository.GetAdByID(ctx, adId)
	if err != nil {
		return nil, err
	}

	if ad.AuthorID != userID {
		return nil, ads.ErrNotAuthor
	}

	ad.Published = published

	return ad, nil
}

func (a *adApp) UpdateAd(ctx context.Context, adId int64, userID int64, title string, text string) (*ads.Ad, error) {
	ad, err := a.repository.GetAdByID(ctx, adId)
	if err != nil {
		return nil, err
	}

	if ad.AuthorID != userID {
		return nil, ads.ErrNotAuthor
	}

	ad.Title = title
	ad.Text = text

	return ad, nil
}
