package grpc

import (
	"context"
	"fmt"
	"google.golang.org/grpc"
	"log"
	"time"
)

func UnaryClientInterceptor(
	ctx context.Context,
	method string,
	req, reply any,
	cc *grpc.ClientConn,
	invoker grpc.UnaryInvoker,
	opts ...grpc.CallOption,
) error {
	reqTime := time.Now()
	err := invoker(ctx, method, req, reply, cc, opts...)
	resTime := time.Now()

	log.Println(
		"request time", reqTime,
		"method", method,
		"response time", resTime,
		"reply", reply,
	)

	return err
}

func UnaryServerInterceptor(
	ctx context.Context,
	req any,
	info *grpc.UnaryServerInfo,
	handler grpc.UnaryHandler,
) (any, error) {
	t := time.Now()

	resp, err := handler(ctx, req)

	log.Println(
		"latency", time.Since(t),
		"method", info.FullMethod,
		"grpc response", fmt.Sprintf("%v", resp),
		"error", err,
	)

	return resp, err
}
