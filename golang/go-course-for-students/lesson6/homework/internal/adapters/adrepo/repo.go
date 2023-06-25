package adrepo

import (
	"context"
	"errors"
	"homework6/internal/ads"
	"strconv"
)

type Repository interface {
	AddAd(ctx context.Context, ad ads.Ad) (int64, error)
	GetAdByID(ctx context.Context, id int64) (*ads.Ad, error)
}

type adRepository struct {
	count int64
	data  map[int64]*ads.Ad
}

func New() Repository {
	return &adRepository{count: 0, data: map[int64]*ads.Ad{}}
}

func (r *adRepository) AddAd(_ context.Context, ad ads.Ad) (int64, error) {
	id := r.count
	ad.ID = id
	r.data[id] = &ad
	r.count++

	return id, nil
}

func (r *adRepository) GetAdByID(_ context.Context, id int64) (*ads.Ad, error) {
	if id < 0 {
		return nil, errors.New("id can not be less than 0")
	}

	if id >= r.count {
		return nil, errors.New("ad with id = " + strconv.FormatInt(id, 10) + " was not found")
	}

	return r.data[id], nil
}
