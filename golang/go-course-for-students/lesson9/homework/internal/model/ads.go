package model

import (
	"time"
)

type Ad struct {
	ID         int64
	Title      string
	Text       string
	AuthorID   int64
	Published  bool
	CreateDate time.Time
	UpdateDate time.Time
}

type AdFilter struct {
	PublishedOnly *FilterField[bool]
	AuthorID      *FilterField[int64]
	CreationDate  *FilterField[time.Time]
}

func DefaultAdFilter() AdFilter {
	return AdFilter{
		PublishedOnly: newFilterField(true, true),
		AuthorID:      newFilterField(false, int64(-1)),
		CreationDate:  newFilterField(false, time.Time{}),
	}
}

type FilterField[T any] struct {
	Checked bool
	Value   T
}

func newFilterField[T any](checked bool, value T) *FilterField[T] {
	return &FilterField[T]{
		Checked: checked,
		Value:   value,
	}
}

func (f *AdFilter) SetPublishedOnly(publishedOnly bool) {
	f.PublishedOnly.Checked = true
	f.PublishedOnly.Value = publishedOnly
}

func (f *AdFilter) SetAuthorID(authorID int64) {
	f.PublishedOnly.Checked = true
	f.AuthorID.Value = authorID
}

func (f *AdFilter) SetCreationDate(date time.Time) {
	f.PublishedOnly.Checked = true
	f.CreationDate.Value = date
}

func (f *AdFilter) Check(ad *Ad) bool {
	if f.PublishedOnly.Checked && f.PublishedOnly.Value && !ad.Published {
		return false
	}

	if f.AuthorID.Checked && f.AuthorID.Value != ad.AuthorID {
		return false
	}

	if f.CreationDate.Checked && f.CreationDate.Value != ad.CreateDate {
		return false
	}

	return true
}
