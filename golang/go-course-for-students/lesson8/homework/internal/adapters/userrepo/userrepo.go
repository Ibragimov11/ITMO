package userrepo

import (
	"context"
	"errors"
	"homework8/internal/model"
	"strconv"
	"sync"
)

type UserRepository interface {
	GetAllUsers(ctx context.Context) ([]*model.User, error)
	AddUser(ctx context.Context, user model.User) (int64, error)
	GetUserById(ctx context.Context, id int64) (*model.User, error)
}

type userRepository struct {
	mx    sync.RWMutex
	count int64
	data  map[int64]*model.User
}

func New() UserRepository {
	return &userRepository{count: 0, data: map[int64]*model.User{}}
}

func (r *userRepository) GetAllUsers(_ context.Context) ([]*model.User, error) {
	r.mx.Lock()
	defer r.mx.Unlock()

	users := make([]*model.User, 0, r.count)
	for _, user := range r.data {
		users = append(users, user)
	}

	return users, nil
}

func (r *userRepository) AddUser(_ context.Context, user model.User) (int64, error) {
	r.mx.Lock()
	defer r.mx.Unlock()

	id := r.count
	user.ID = id
	r.data[id] = &user
	r.count++

	return id, nil
}

func (r *userRepository) GetUserById(_ context.Context, id int64) (*model.User, error) {
	r.mx.Lock()
	defer r.mx.Unlock()

	if id < 0 {
		return nil, errors.New("id can not be less than 0")
	}

	if id >= r.count {
		return nil, errors.New("user with id = " + strconv.FormatInt(id, 10) + " was not found")
	}

	return r.data[id], nil
}
