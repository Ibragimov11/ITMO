package main

import (
	"context"
	"fmt"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/protobuf/types/known/emptypb"
	"lesson9/grpc/foo"
	"log"
	"net/http"
	"time"
)

func main() {
	conn, err := grpc.DialContext(context.Background(), "localhost:50054", grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatal(err)
	}

	client := foo.NewBarServiceClient(conn)

	for {
		go func() {
			obj, err := client.Bar(context.Background(), &emptypb.Empty{})
			if err != nil {
				log.Println(err)
			}

			if obj != nil {
				fmt.Println(obj)
			}

			req, err := http.NewRequest(http.MethodGet, "http://localhost:9000/foo", nil)
			if err != nil {
				log.Println(err)
			}

			resp, err := http.DefaultClient.Do(req)
			if err != nil {
				log.Println(err)
			}

			if resp != nil {
				fmt.Println(resp.Proto)
			}
		}()

		time.Sleep(2 * time.Second)
	}

}
