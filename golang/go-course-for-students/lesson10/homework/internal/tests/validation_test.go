package tests

import (
	"github.com/stretchr/testify/suite"
	grpcPort "homework10/internal/ports/grpc"
	"strings"
	"testing"

	"github.com/stretchr/testify/assert"
)

type Test struct {
	name, title, text string
}

func failureTests() []Test {
	return []Test{
		{
			name:  "Empty title",
			title: "",
			text:  "world",
		},
		{
			name:  "Too long title",
			title: strings.Repeat("a", 101),
			text:  "world",
		},
		{
			name:  "Empty text",
			title: "hello",
			text:  "",
		},
		{
			name:  "Too long text",
			title: "hello",
			text:  strings.Repeat("a", 501),
		},
	}
}

func TestFailHttpCreateAd(t *testing.T) {
	tests := failureTests()
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			client := getTestClient()

			user, err := client.createUser("leo", "messi")
			assert.NoError(t, err)

			_, err = client.createAd(user.Data.ID, tt.title, tt.text)
			assert.ErrorIs(t, err, ErrBadRequest)
		})
	}
}

func TestFailHttpUpdateAd(t *testing.T) {
	tests := failureTests()
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			client := getTestClient()

			user, err := client.createUser("leo", "messi")
			assert.NoError(t, err)

			resp, err := client.createAd(user.Data.ID, "hello", "world")
			assert.NoError(t, err)

			_, err = client.updateAd(user.Data.ID, resp.Data.ID, tt.title, tt.text)
			assert.ErrorIs(t, err, ErrBadRequest)
		})
	}
}

func TestFailGrpcCreateAd(t *testing.T) {
	tests := failureTests()
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			client, ctx := getGRPCTestClient(t)

			user, err := client.CreateUser(ctx, &grpcPort.CreateUserRequest{
				Nickname: "leo",
				Email:    "messi",
			})
			assert.NoError(t, err)

			_, err = client.CreateAd(ctx, &grpcPort.CreateAdRequest{
				Title:  tt.title,
				Text:   tt.text,
				UserId: user.Id,
			})
			assert.Error(t, err)
		})
	}
}

func TestFailGrpcUpdateAd(t *testing.T) {
	tests := failureTests()
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
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

			_, err = client.UpdateAd(ctx, &grpcPort.UpdateAdRequest{
				AdId:   ad.Id,
				Title:  tt.title,
				Text:   tt.text,
				UserId: user.Id,
			})
			assert.Error(t, err)
		})
	}
}

func FuzzValidation(f *testing.F) {
	tc := 0
	for {
		f.Add(tc)
		tc = tc + 10

		if tc > 100 {
			break
		}
	}

	f.Fuzz(func(t *testing.T, len int) {
		if len >= 0 {
			got := grpcPort.ValidateAdRequest(strings.Repeat("a", len), "hello") == nil
			expect := 1 <= len && len <= 100

			if got != expect {
				t.Errorf("For title's len (^%d) validation work not correct", len)
			}
		}
	})
}

func BenchmarkValidation(b *testing.B) {
	for i := 0; i < b.N; i++ {
		_ = grpcPort.ValidateAdRequest("hello", "hello")
	}
}

type ValidationTestSuite struct {
	suite.Suite
	title, text string
}

func (suite *ValidationTestSuite) SetupSuite() {
	suite.title = "hello"
	suite.text = "world"
}

func (suite *ValidationTestSuite) TestSuite() {
	assert.NoError(suite.T(), grpcPort.ValidateAdRequest(suite.title, suite.text))
}

func TestValidationTestSuite(t *testing.T) {
	suite.Run(t, new(ValidationTestSuite))
}
