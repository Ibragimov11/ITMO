package userrepo

import (
	"context"
	"errors"
	"homework10/internal/model"
	"strconv"
	"sync"
)

type UserRepository interface {
	AddUser(ctx context.Context, user model.User) int64
	GetAllUsers(ctx context.Context) []*model.User
	GetUserByID(ctx context.Context, id int64) (*model.User, error)
	DeleteUserByID(ctx context.Context, id int64) *model.User
}

type userRepository struct {
	mx    sync.RWMutex
	count int64
	data  map[int64]*model.User
}

func New() UserRepository {
	return &userRepository{count: 0, data: map[int64]*model.User{}}
}

func (r *userRepository) AddUser(_ context.Context, user model.User) int64 {
	r.mx.Lock()
	defer r.mx.Unlock()

	id := r.count
	user.ID = id
	r.data[id] = &user
	r.count++

	return id
}

func (r *userRepository) GetAllUsers(_ context.Context) []*model.User {
	r.mx.Lock()
	defer r.mx.Unlock()

	users := make([]*model.User, 0, r.count)
	for _, user := range r.data {
		users = append(users, user)
	}

	return users
}

func (r *userRepository) GetUserByID(_ context.Context, id int64) (*model.User, error) {
	r.mx.Lock()
	defer r.mx.Unlock()

	if id < 0 {
		return nil, errors.New("id can not be less than 0")
	}

	if user, found := r.data[id]; found {
		return user, nil
	}

	return nil, errors.New("user with id = " + strconv.FormatInt(id, 10) + " was not found")
}

func (r *userRepository) DeleteUserByID(_ context.Context, id int64) *model.User {
	r.mx.Lock()
	defer r.mx.Unlock()

	user := r.data[id]
	delete(r.data, id)

	return user
}
