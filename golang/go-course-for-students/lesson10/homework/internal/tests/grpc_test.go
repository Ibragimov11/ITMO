package tests

import (
	"github.com/stretchr/testify/assert"
	"google.golang.org/protobuf/types/known/emptypb"
	grpcPort "homework10/internal/ports/grpc"
	"testing"
)

func TestGRPCCreateAd(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	ad1, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user.Id,
	})
	assert.NoError(t, err)

	assert.Zero(t, ad1.Id)
	assert.Equal(t, ad1.Title, "hello")
	assert.Equal(t, ad1.Text, "world")
	assert.Zero(t, ad1.AuthorId)
	assert.False(t, ad1.Published)

	ad2, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "привет",
		Text:   "мир",
		UserId: user.Id,
	})
	assert.NoError(t, err)

	assert.Equal(t, ad2.Id, int64(1))
	assert.Equal(t, ad2.Title, "привет")
	assert.Equal(t, ad2.Text, "мир")
	assert.Zero(t, ad2.AuthorId)
	assert.False(t, ad2.Published)

	_, err = client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "404",
		Text:   "user not found",
		UserId: int64(123),
	})
	assert.Error(t, err)
}

func TestGRPCListAds(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user1, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	user2, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "lev",
		Email:    "tolstoy",
	})
	assert.NoError(t, err)

	ad, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	publishedAd, err := client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: true,
		UserId:    user1.Id,
	})
	assert.NoError(t, err)

	_, err = client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "best cat",
		Text:   "not for sale",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	_, err = client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user2.Id,
	})
	assert.NoError(t, err)

	ads, err := client.ListAds(ctx, &grpcPort.FilteredListAdsRequest{
		PublishedOnlyChecked: true,
		PublishedOnly:        true,
		AuthorIdChecked:      false,
		AuthorId:             0,
		CreationDateChecked:  false,
		CreationDate:         nil,
	})
	assert.NoError(t, err)

	assert.Len(t, ads.Ads, 1)
	assert.Equal(t, ads.Ads[0].Id, publishedAd.Id)
	assert.Equal(t, ads.Ads[0].Title, publishedAd.Title)
	assert.Equal(t, ads.Ads[0].Text, publishedAd.Text)
	assert.Equal(t, ads.Ads[0].AuthorId, publishedAd.AuthorId)
	assert.True(t, ads.Ads[0].Published)
}

func TestGRPCGetAd(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	ad, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user.Id,
	})
	assert.NoError(t, err)

	ad, err = client.GetAd(ctx, &grpcPort.GetAdRequest{
		Id: ad.Id,
	})
	assert.NoError(t, err)

	assert.Zero(t, ad.Id)
	assert.Equal(t, ad.Title, "hello")
	assert.Equal(t, ad.Text, "world")
	assert.Equal(t, ad.AuthorId, user.Id)
	assert.False(t, ad.Published)

	_, err = client.GetAd(ctx, &grpcPort.GetAdRequest{
		Id: int64(-1),
	})
	assert.Error(t, err)

	_, err = client.GetAd(ctx, &grpcPort.GetAdRequest{
		Id: int64(123),
	})
	assert.Error(t, err)
}

func TestGRPCListAdsByTitle(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user1, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	user2, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "lev",
		Email:    "tolstoy",
	})
	assert.NoError(t, err)

	ad1, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	ad1, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad1.Id,
		Published: true,
		UserId:    user1.Id,
	})
	assert.NoError(t, err)

	ad2, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "helllo",
		Text:   "world",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	_, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad2.Id,
		Published: true,
		UserId:    user1.Id,
	})
	assert.NoError(t, err)

	_, err = client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "not published",
		UserId: user2.Id,
	})
	assert.NoError(t, err)

	ads, err := client.GetAdsByTitle(ctx, &grpcPort.GetAdsByTitleRequest{
		Title: "hello",
	})
	assert.NoError(t, err)

	assert.Len(t, ads.Ads, 1)

	assert.Equal(t, ads.Ads[0].Id, ad1.Id)
	assert.Equal(t, ads.Ads[0].Title, ad1.Title)
	assert.Equal(t, ads.Ads[0].Text, ad1.Text)
	assert.Equal(t, ads.Ads[0].AuthorId, ad1.AuthorId)
	assert.True(t, ads.Ads[0].Published)
}

func TestGRPCUpdateAd(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user1, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	user2, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "lev",
		Email:    "tolstoy",
	})
	assert.NoError(t, err)

	ad, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	ad, err = client.UpdateAd(ctx, &grpcPort.UpdateAdRequest{
		AdId:   ad.Id,
		Title:  "привет",
		Text:   "мир",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	assert.Equal(t, ad.Title, "привет")
	assert.Equal(t, ad.Text, "мир")

	_, err = client.UpdateAd(ctx, &grpcPort.UpdateAdRequest{
		AdId:   ad.Id,
		Title:  "404",
		Text:   "user not found",
		UserId: int64(123),
	})
	assert.Error(t, err)

	_, err = client.UpdateAd(ctx, &grpcPort.UpdateAdRequest{
		AdId:   int64(123),
		Title:  "404",
		Text:   "ad not found",
		UserId: user1.Id,
	})
	assert.Error(t, err)

	_, err = client.UpdateAd(ctx, &grpcPort.UpdateAdRequest{
		AdId:   ad.Id,
		Title:  "403",
		Text:   "not author",
		UserId: user2.Id,
	})
	assert.Error(t, err)
}

func TestGRPCChangeAdStatus(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user1, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	user2, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "lev",
		Email:    "tolstoy",
	})
	assert.NoError(t, err)

	ad, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	ad, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: true,
		UserId:    user1.Id,
	})
	assert.NoError(t, err)
	assert.True(t, ad.Published)

	ad, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: false,
		UserId:    user1.Id,
	})
	assert.NoError(t, err)
	assert.False(t, ad.Published)

	ad, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: false,
		UserId:    user1.Id,
	})
	assert.NoError(t, err)
	assert.False(t, ad.Published)

	_, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: false,
		UserId:    int64(123),
	})
	assert.Error(t, err)

	_, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      int64(123),
		Published: false,
		UserId:    user1.Id,
	})
	assert.Error(t, err)

	_, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: false,
		UserId:    user2.Id,
	})
	assert.Error(t, err)
}

func TestGRPCDeleteAd(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user1, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	user2, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "lev",
		Email:    "tolstoy",
	})
	assert.NoError(t, err)

	ad, err := client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user1.Id,
	})
	assert.NoError(t, err)

	_, err = client.DeleteAd(ctx, &grpcPort.DeleteAdRequest{
		AdId:   int64(-1),
		UserId: user1.Id,
	})
	assert.Error(t, err)

	_, err = client.DeleteAd(ctx, &grpcPort.DeleteAdRequest{
		AdId:   ad.Id,
		UserId: int64(123),
	})
	assert.Error(t, err)

	_, err = client.DeleteAd(ctx, &grpcPort.DeleteAdRequest{
		AdId:   ad.Id,
		UserId: user2.Id,
	})
	assert.Error(t, err)

	ad, err = client.DeleteAd(ctx, &grpcPort.DeleteAdRequest{
		AdId:   ad.Id,
		UserId: user1.Id,
	})
	assert.NoError(t, err)
	assert.Zero(t, ad.Id)
	assert.Equal(t, ad.Title, "hello")
	assert.Equal(t, ad.Text, "world")
	assert.Equal(t, ad.AuthorId, user1.Id)
	assert.False(t, ad.Published)

	ads, err := client.ListAds(ctx, &grpcPort.FilteredListAdsRequest{
		PublishedOnlyChecked: true,
		PublishedOnly:        true,
		AuthorIdChecked:      false,
		AuthorId:             0,
		CreationDateChecked:  false,
		CreationDate:         nil,
	})
	assert.NoError(t, err)
	assert.Len(t, ads.Ads, 0)
}

func TestGRPCCreateUser(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user1, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	assert.Zero(t, user1.Id)
	assert.Equal(t, user1.Nickname, "leo")
	assert.Equal(t, user1.Email, "messi")

	user2, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "lev",
		Email:    "tolstoy",
	})
	assert.NoError(t, err)

	assert.Equal(t, user2.Id, int64(1))
	assert.Equal(t, user2.Nickname, "lev")
	assert.Equal(t, user2.Email, "tolstoy")
}

func TestGRPCListUsers(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	users, err := client.ListUsers(ctx, &emptypb.Empty{})
	assert.NoError(t, err)

	assert.Len(t, users.Users, 1)

	assert.Equal(t, users.Users[0].Id, user.Id)
	assert.Equal(t, users.Users[0].Nickname, user.Nickname)
	assert.Equal(t, users.Users[0].Email, user.Email)
}

func TestGRPCGetUser(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	user, err = client.GetUser(ctx, &grpcPort.GetUserRequest{
		Id: user.Id,
	})
	assert.NoError(t, err)

	assert.Zero(t, user.Id)
	assert.Equal(t, user.Nickname, "leo")
	assert.Equal(t, user.Email, "messi")

	_, err = client.GetUser(ctx, &grpcPort.GetUserRequest{
		Id: int64(-1),
	})
	assert.Error(t, err)

	_, err = client.GetUser(ctx, &grpcPort.GetUserRequest{
		Id: int64(123),
	})
	assert.Error(t, err)
}

func TestGRPCUpdateUser(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	user, err = client.UpdateUser(ctx, &grpcPort.UpdateUserRequest{
		Id:       user.Id,
		Nickname: "lionel",
		Email:    "messi10",
	})
	assert.NoError(t, err)

	assert.Equal(t, user.Nickname, "lionel")
	assert.Equal(t, user.Email, "messi10")

	_, err = client.UpdateUser(ctx, &grpcPort.UpdateUserRequest{
		Id:       int64(123),
		Nickname: "lionel",
		Email:    "messi10",
	})
	assert.Error(t, err)
}

func TestGRPCDeleteUser(t *testing.T) {
	client, ctx := getGRPCTestClient(t)

	user, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
		Nickname: "leo",
		Email:    "messi",
	})
	assert.NoError(t, err)

	_, err = client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "hello",
		Text:   "world",
		UserId: user.Id,
	})
	assert.NoError(t, err)

	_, err = client.CreateAd(ctx, &grpcPort.CreateAdRequest{
		Title:  "привет",
		Text:   "мир",
		UserId: user.Id,
	})
	assert.NoError(t, err)

	_, err = client.DeleteUser(ctx, &grpcPort.DeleteUserRequest{
		Id: int64(-1),
	})
	assert.Error(t, err)

	_, err = client.DeleteUser(ctx, &grpcPort.DeleteUserRequest{
		Id: int64(123),
	})
	assert.Error(t, err)

	user, err = client.DeleteUser(ctx, &grpcPort.DeleteUserRequest{
		Id: user.Id,
	})
	assert.NoError(t, err)
	assert.Zero(t, user.Id)
	assert.Equal(t, user.Nickname, "leo")
	assert.Equal(t, user.Email, "messi")

	users, err := client.ListUsers(ctx, &emptypb.Empty{})
	assert.NoError(t, err)
	assert.Len(t, users.Users, 0)

	ads, err := client.ListAds(ctx, &grpcPort.FilteredListAdsRequest{
		PublishedOnlyChecked: true,
		PublishedOnly:        true,
		AuthorIdChecked:      false,
		AuthorId:             0,
		CreationDateChecked:  false,
		CreationDate:         nil,
	})
	assert.NoError(t, err)
	assert.Len(t, ads.Ads, 0)
}
