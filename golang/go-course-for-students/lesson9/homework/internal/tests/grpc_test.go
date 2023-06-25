package tests

import (
	"context"
	"github.com/stretchr/testify/assert"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/test/bufconn"
	"google.golang.org/protobuf/types/known/emptypb"
	"homework9/internal/adapters/adrepo"
	"homework9/internal/adapters/userrepo"
	"homework9/internal/app"
	grpcPort "homework9/internal/ports/grpc"
	"net"
	"testing"
	"time"
)

func getGRPCTestClient(t *testing.T) (grpcPort.AdServiceClient, context.Context) {
	lis := bufconn.Listen(1024 * 1024)
	t.Cleanup(func() {
		lis.Close()
	})

	srv := grpc.NewServer()
	t.Cleanup(func() {
		srv.Stop()
	})

	svc := grpcPort.NewService(app.NewApp(adrepo.New(), userrepo.New()))
	grpcPort.RegisterAdServiceServer(srv, svc)

	go func() {
		assert.NoError(t, srv.Serve(lis), "srv.Serve")
	}()

	dialer := func(context.Context, string) (net.Conn, error) {
		return lis.Dial()
	}

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	t.Cleanup(func() {
		cancel()
	})

	conn, err := grpc.DialContext(
		ctx,
		"",
		grpc.WithContextDialer(dialer),
		grpc.WithTransportCredentials(insecure.NewCredentials()),
	)
	assert.NoError(t, err, "grpc.DialContext")

	t.Cleanup(func() {
		conn.Close()
	})

	return grpcPort.NewAdServiceClient(conn), ctx
}

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

	ad, err = client.UpdateAd(ctx, &grpcPort.UpdateAdRequest{
		AdId:   ad.Id,
		Title:  "привет",
		Text:   "мир",
		UserId: user.Id,
	})
	assert.NoError(t, err)

	assert.Equal(t, ad.Title, "привет")
	assert.Equal(t, ad.Text, "мир")
}

func TestGRPCChangeAdStatus(t *testing.T) {
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

	ad, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: true,
		UserId:    user.Id,
	})
	assert.NoError(t, err)
	assert.True(t, ad.Published)

	ad, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: false,
		UserId:    user.Id,
	})
	assert.NoError(t, err)
	assert.False(t, ad.Published)

	ad, err = client.ChangeAdStatus(ctx, &grpcPort.ChangeAdStatusRequest{
		AdId:      ad.Id,
		Published: false,
		UserId:    user.Id,
	})
	assert.NoError(t, err)
	assert.False(t, ad.Published)
}

func TestGRPCDeleteAd(t *testing.T) {
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

	ad, err = client.DeleteAd(ctx, &grpcPort.DeleteAdRequest{
		AdId:   ad.Id,
		UserId: user.Id,
	})
	assert.NoError(t, err)
	assert.Zero(t, ad.Id)
	assert.Equal(t, ad.Title, "hello")
	assert.Equal(t, ad.Text, "world")
	assert.Equal(t, ad.AuthorId, user.Id)
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
