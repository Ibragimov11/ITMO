package ads

import "errors"

type Ad struct {
	ID        int64
	Title     string
	Text      string
	AuthorID  int64
	Published bool
}

var ErrNotAuthor = errors.New("only the author can change")
