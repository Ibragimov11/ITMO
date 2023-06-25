package storage

import (
	"context"
	"errors"
	"sync"
	"sync/atomic"

	"golang.org/x/sync/errgroup"
)

// Result represents the Size function result
type Result struct {
	// Total Size of File objects
	Size int64
	// Count is a count of File objects processed
	Count int64
}

type DirSizer interface {
	// Size calculate a size of given Dir, receive a ctx and the root Dir instance
	// will return Result or error if happened
	Size(ctx context.Context, d Dir) (Result, error)
}

// sizer implement the DirSizer interface
type sizer struct {
	// maxWorkersCount number of workers for asynchronous run
	maxWorkersCount int
}

// NewSizer returns new DirSizer instance
func NewSizer() DirSizer {
	return &sizer{4}
}

func (a *sizer) Size(ctx context.Context, d Dir) (Result, error) {
	if a.maxWorkersCount < 0 {
		return Result{0, 0}, errors.New("maxWorkersCount should not be less than 0")
	}

	var wg sync.WaitGroup
	group, ctx := errgroup.WithContext(ctx)

	size := int64(0)
	count := int64(0)

	dirCh := make(chan Dir, a.maxWorkersCount-1)

	wg.Add(1)
	dirCh <- d

	go func() {
		for {
			select {
			case dir := <-dirCh:
				group.Go(func() error {
					return worker(&wg, &dirCh, ctx, dir, &size, &count)
				})
			case <-ctx.Done():
				return
			}
		}
	}()

	wg.Wait()
	err := group.Wait()

	return Result{size, count}, err
}

func worker(
	wg *sync.WaitGroup,
	dirCh *chan Dir,
	ctx context.Context,
	d Dir,
	size *int64,
	count *int64,
) error {
	defer wg.Done()

	dirs, files, err := d.Ls(ctx)
	if err != nil {
		return err
	}

	for _, file := range files {
		s, err1 := file.Stat(ctx)
		if err1 != nil {
			return err1
		}

		switch {
		case s < 0:
			return errors.New("unexpected error: file.Stat() returned negative value")
		case s == 0:
			return errors.New("file does not exist")
		case s > 0:
			atomic.AddInt64(size, s)
			atomic.AddInt64(count, 1)
		}
	}

	wg.Add(len(dirs))
	for _, dir := range dirs {
		*dirCh <- dir
	}

	return nil
}
