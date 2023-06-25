package tests

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestCreateAd(t *testing.T) {
	client := getTestClient()

	user, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	ad1, err := client.createAd(user.Data.ID, "hello", "world")
	assert.NoError(t, err)

	assert.Zero(t, ad1.Data.ID)
	assert.Equal(t, ad1.Data.Title, "hello")
	assert.Equal(t, ad1.Data.Text, "world")
	assert.Zero(t, ad1.Data.AuthorID)
	assert.False(t, ad1.Data.Published)

	ad2, err := client.createAd(user.Data.ID, "привет", "мир")
	assert.NoError(t, err)

	assert.Equal(t, ad2.Data.ID, int64(1))
	assert.Equal(t, ad2.Data.Title, "привет")
	assert.Equal(t, ad2.Data.Text, "мир")
	assert.Zero(t, ad2.Data.AuthorID)
	assert.False(t, ad2.Data.Published)

	_, err = client.createAd(int64(123), "404", "user not found")
	assert.Error(t, err)
}

func TestListAds(t *testing.T) {
	client := getTestClient()

	user1, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	user2, err := client.createUser("lev", "tolstoy")
	assert.NoError(t, err)

	ad, err := client.createAd(user1.Data.ID, "hello", "world")
	assert.NoError(t, err)

	publishedAd, err := client.changeAdStatus(user1.Data.ID, ad.Data.ID, true)
	assert.NoError(t, err)

	_, err = client.createAd(user1.Data.ID, "best cat", "not for sale")
	assert.NoError(t, err)

	_, err = client.createAd(user2.Data.ID, "hello", "world")
	assert.NoError(t, err)

	ads, err := client.listAds()
	assert.NoError(t, err)

	assert.Len(t, ads.Data, 1)
	assert.Equal(t, ads.Data[0].ID, publishedAd.Data.ID)
	assert.Equal(t, ads.Data[0].Title, publishedAd.Data.Title)
	assert.Equal(t, ads.Data[0].Text, publishedAd.Data.Text)
	assert.Equal(t, ads.Data[0].AuthorID, publishedAd.Data.AuthorID)
	assert.True(t, ads.Data[0].Published)
}

func TestGetAd(t *testing.T) {
	client := getTestClient()

	user, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	ad, err := client.createAd(user.Data.ID, "hello", "world")
	assert.NoError(t, err)

	ad, err = client.getAd(ad.Data.ID)
	assert.NoError(t, err)

	assert.Zero(t, ad.Data.ID)
	assert.Equal(t, ad.Data.Title, "hello")
	assert.Equal(t, ad.Data.Text, "world")
	assert.Equal(t, ad.Data.AuthorID, user.Data.ID)
	assert.False(t, ad.Data.Published)

	_, err = client.getAd(int64(-1))
	assert.Error(t, err)

	_, err = client.getAd(int64(123))
	assert.Error(t, err)
}

func TestListAdsByTitle(t *testing.T) {
	client := getTestClient()

	user1, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	user2, err := client.createUser("lev", "tolstoy")
	assert.NoError(t, err)

	ad1, err := client.createAd(user1.Data.ID, "hello", "world")
	assert.NoError(t, err)

	ad1, err = client.changeAdStatus(user1.Data.ID, ad1.Data.ID, true)
	assert.NoError(t, err)

	ad2, err := client.createAd(user1.Data.ID, "helllo", "world")
	assert.NoError(t, err)

	_, err = client.changeAdStatus(user1.Data.ID, ad2.Data.ID, true)
	assert.NoError(t, err)

	_, err = client.createAd(user2.Data.ID, "hello", "not published")
	assert.NoError(t, err)

	ads, err := client.getAdsByTitle("hello")
	assert.NoError(t, err)

	assert.Len(t, ads.Data, 1)

	assert.Equal(t, ads.Data[0].ID, ad1.Data.ID)
	assert.Equal(t, ads.Data[0].Title, ad1.Data.Title)
	assert.Equal(t, ads.Data[0].Text, ad1.Data.Text)
	assert.Equal(t, ads.Data[0].AuthorID, ad1.Data.AuthorID)
	assert.True(t, ads.Data[0].Published)
}

func TestUpdateAd(t *testing.T) {
	client := getTestClient()

	user1, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	user2, err := client.createUser("lev", "tolstoy")
	assert.NoError(t, err)

	ad, err := client.createAd(user1.Data.ID, "hello", "world")
	assert.NoError(t, err)

	ad, err = client.updateAd(user1.Data.ID, ad.Data.ID, "привет", "мир")
	assert.NoError(t, err)

	assert.Equal(t, ad.Data.Title, "привет")
	assert.Equal(t, ad.Data.Text, "мир")

	_, err = client.updateAd(int64(123), ad.Data.ID, "привет", "мир")
	assert.Error(t, err)

	_, err = client.updateAd(user1.Data.ID, int64(123), "привет", "мир")
	assert.Error(t, err)

	_, err = client.updateAd(user2.Data.ID, ad.Data.ID, "привет", "мир")
	assert.Error(t, err)
}

func TestChangeAdStatus(t *testing.T) {
	client := getTestClient()

	user1, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	user2, err := client.createUser("lev", "tolstoy")
	assert.NoError(t, err)

	ad, err := client.createAd(user1.Data.ID, "hello", "world")
	assert.NoError(t, err)

	ad, err = client.changeAdStatus(user1.Data.ID, ad.Data.ID, true)
	assert.NoError(t, err)
	assert.True(t, ad.Data.Published)

	ad, err = client.changeAdStatus(user1.Data.ID, ad.Data.ID, false)
	assert.NoError(t, err)
	assert.False(t, ad.Data.Published)

	ad, err = client.changeAdStatus(user1.Data.ID, ad.Data.ID, false)
	assert.NoError(t, err)
	assert.False(t, ad.Data.Published)

	_, err = client.changeAdStatus(int64(123), ad.Data.ID, false)
	assert.Error(t, err)

	_, err = client.changeAdStatus(user1.Data.ID, int64(123), false)
	assert.Error(t, err)

	_, err = client.changeAdStatus(user2.Data.ID, ad.Data.ID, false)
	assert.Error(t, err)
}

func TestDeleteAd(t *testing.T) {
	client := getTestClient()

	user1, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	user2, err := client.createUser("lev", "tolstoy")
	assert.NoError(t, err)

	_, err = client.createAd(user1.Data.ID, "hello", "world")
	assert.NoError(t, err)

	ad, err := client.deleteAd(int64(-1), user1.Data.ID)
	assert.Error(t, err)

	ad, err = client.deleteAd(ad.Data.ID, int64(123))
	assert.Error(t, err)

	ad, err = client.deleteAd(ad.Data.ID, user2.Data.ID)
	assert.Error(t, err)

	ad, err = client.deleteAd(ad.Data.ID, user1.Data.ID)
	assert.NoError(t, err)
	assert.Zero(t, ad.Data.ID)
	assert.Equal(t, ad.Data.Title, "hello")
	assert.Equal(t, ad.Data.Text, "world")
	assert.Equal(t, ad.Data.AuthorID, user1.Data.ID)
	assert.False(t, ad.Data.Published)

	ads, err := client.listAds()
	assert.NoError(t, err)
	assert.Len(t, ads.Data, 0)
}

func TestCreateUser(t *testing.T) {
	client := getTestClient()

	user1, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	assert.Zero(t, user1.Data.ID)
	assert.Equal(t, user1.Data.Nickname, "leo")
	assert.Equal(t, user1.Data.Email, "messi")

	user2, err := client.createUser("lev", "tolstoy")
	assert.NoError(t, err)

	assert.Equal(t, user2.Data.ID, int64(1))
	assert.Equal(t, user2.Data.Nickname, "lev")
	assert.Equal(t, user2.Data.Email, "tolstoy")
}

func TestListUsers(t *testing.T) {
	client := getTestClient()

	user, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	users, err := client.listUsers()
	assert.NoError(t, err)

	assert.Len(t, users.Data, 1)

	assert.Equal(t, users.Data[0].ID, user.Data.ID)
	assert.Equal(t, users.Data[0].Nickname, user.Data.Nickname)
	assert.Equal(t, users.Data[0].Email, user.Data.Email)
}

func TestGetUser(t *testing.T) {
	client := getTestClient()

	user, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	user, err = client.getUser(user.Data.ID)
	assert.NoError(t, err)

	assert.Zero(t, user.Data.ID)
	assert.Equal(t, user.Data.Nickname, "leo")
	assert.Equal(t, user.Data.Email, "messi")

	_, err = client.getUser(int64(-1))
	assert.Error(t, err)

	_, err = client.getUser(int64(123))
	assert.Error(t, err)
}

func TestUpdateUser(t *testing.T) {
	client := getTestClient()

	user, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	user, err = client.updateUser(user.Data.ID, "lionel", "messi10")
	assert.NoError(t, err)

	assert.Equal(t, user.Data.Nickname, "lionel")
	assert.Equal(t, user.Data.Email, "messi10")

	user, err = client.updateUser(int64(123), "lionel", "messi10")
	assert.Error(t, err)
}

func TestDeleteUser(t *testing.T) {
	client := getTestClient()

	user, err := client.createUser("leo", "messi")
	assert.NoError(t, err)

	_, err = client.createAd(user.Data.ID, "hello", "world")
	assert.NoError(t, err)

	_, err = client.createAd(user.Data.ID, "привет", "мир")
	assert.NoError(t, err)

	_, err = client.deleteUser(int64(-1))
	assert.Error(t, err)

	_, err = client.deleteUser(int64(123))
	assert.Error(t, err)

	user, err = client.deleteUser(user.Data.ID)
	assert.NoError(t, err)
	assert.Zero(t, user.Data.ID)
	assert.Equal(t, user.Data.Nickname, "leo")
	assert.Equal(t, user.Data.Email, "messi")

	users, err := client.listUsers()
	assert.NoError(t, err)
	assert.Len(t, users.Data, 0)

	ads, err := client.listAds()
	assert.NoError(t, err)
	assert.Len(t, ads.Data, 0)
}
