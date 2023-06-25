package adrepo

import (
	"context"
	"errors"
	"homework10/internal/model"
	"strconv"
	"sync"
)

type AdRepository interface {
	AddAd(ctx context.Context, ad model.Ad) int64
	GetAllAds(ctx context.Context) []*model.Ad
	GetAdByID(ctx context.Context, id int64) (*model.Ad, error)
	DeleteAdByID(ctx context.Context, id int64) *model.Ad
	DeleteAdsByAuthor(ctx context.Context, authorID int64)
}

type adRepository struct {
	mx    sync.RWMutex
	count int64
	data  map[int64]*model.Ad
}

func New() AdRepository {
	return &adRepository{count: 0, data: map[int64]*model.Ad{}}
}

func (r *adRepository) AddAd(_ context.Context, ad model.Ad) int64 {
	r.mx.Lock()
	defer r.mx.Unlock()

	id := r.count
	ad.ID = id
	r.data[id] = &ad
	r.count++

	return id
}

func (r *adRepository) GetAllAds(_ context.Context) []*model.Ad {
	r.mx.Lock()
	defer r.mx.Unlock()

	ads := make([]*model.Ad, 0, r.count)
	for _, ad := range r.data {
		ads = append(ads, ad)
	}

	return ads
}

func (r *adRepository) GetAdByID(_ context.Context, id int64) (*model.Ad, error) {
	r.mx.Lock()
	defer r.mx.Unlock()

	if id < 0 {
		return nil, errors.New("id can not be less than 0")
	}

	if ad, found := r.data[id]; found {
		return ad, nil
	}

	return nil, errors.New("ad with id = " + strconv.FormatInt(id, 10) + " was not found")
}

func (r *adRepository) DeleteAdByID(_ context.Context, id int64) *model.Ad {
	r.mx.Lock()
	defer r.mx.Unlock()

	ad := r.data[id]
	delete(r.data, id)

	return ad
}

func (r *adRepository) DeleteAdsByAuthor(_ context.Context, authorID int64) {
	r.mx.Lock()
	defer r.mx.Unlock()

	for id, ad := range r.data {
		if ad.ID == authorID {
			delete(r.data, id)
		}
	}
}
