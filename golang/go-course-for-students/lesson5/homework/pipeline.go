package executor

import (
	"context"
)

type (
	In  <-chan any
	Out = In
)

type Stage func(in In) (out Out)

func ExecutePipeline(ctx context.Context, in In, stages ...Stage) Out {
	out := make(chan any)

	go func() {
		defer close(out)
		for _, s := range stages {
			in = decorator(ctx, s)(in)
		}

		for v := range in {
			out <- v
		}
	}()

	return out
}

func decorator(ctx context.Context, fn func(In) Out) func(In) Out {
	return func(in In) Out {
		out := make(chan any)
		go func() {
			defer close(out)
			in1 := fn(in)

			for {
				select {
				case <-ctx.Done():
					return
				case v, ok := <-in1:
					if !ok {
						return
					}

					out <- v
				}
			}
		}()

		return out
	}
}
