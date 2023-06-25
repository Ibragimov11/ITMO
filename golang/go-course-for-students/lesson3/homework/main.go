package main

import (
	"bytes"
	"errors"
	"flag"
	"fmt"
	"io"
	"os"
	"strings"
)

const (
	UpperCase  = "upper_case"
	LowerCase  = "lower_case"
	TrimSpaces = "trim_spaces"
	Empty      = ""
)

type Options struct {
	From      string
	To        string
	Offset    uint
	Limit     uint
	BlockSize uint // not supported
	Conv      string
}

type upperCase struct {
	output io.Writer
}

type lowerCase struct {
	output io.Writer
}

type trimSpace struct {
	output io.Writer
}

func NewUpperCase(output io.Writer) io.Writer {
	return &upperCase{output}
}

func NewLowerCase(output io.Writer) io.Writer {
	return &lowerCase{output}
}

func NewTrimSpace(output io.Writer) io.Writer {
	return &trimSpace{output}
}

func (u *upperCase) Write(p []byte) (int, error) {
	data := bytes.ToUpper(p)
	return u.output.Write(data)
}

func (l *lowerCase) Write(p []byte) (int, error) {
	data := bytes.ToLower(p)
	return l.output.Write(data)
}

func (t *trimSpace) Write(p []byte) (int, error) {
	data := bytes.TrimSpace(p)
	return t.output.Write(data)
}

func parseFlags() *Options {
	var opts Options

	flag.StringVar(&opts.From, "from", "", "file to read. by default - stdin")
	flag.StringVar(&opts.To, "to", "", "file to write. by default - stdout")
	flag.UintVar(&opts.Offset, "offset", 0, "number of bytes to skip at the beginning")
	flag.UintVar(&opts.Limit, "limit", 0, "maximum number of bytes to read")
	flag.UintVar(&opts.BlockSize, "block-size", 0, "size of one block in bytes when reading and writing")
	flag.StringVar(&opts.Conv, "conv", "", "file to write. by default - stdout")

	flag.Parse()

	return &opts
}

func validateFlags(opts *Options) error {
	if opts.From != "" {
		if _, err := os.Stat(opts.From); errors.Is(err, os.ErrNotExist) {
			return errors.New("input file does not exist")
		}
	}

	if opts.To != "" {
		if _, err := os.Stat(opts.To); err == nil {
			return errors.New("output file already exists")
		} else if errors.Is(err, os.ErrNotExist) {
			if _, err := os.Create(opts.To); err != nil {
				return errors.New("output file creating error")
			}
		}
	}

	upper := false
	lower := false
	for _, conv := range strings.Split(opts.Conv, ",") {
		switch conv {
		case UpperCase:
			upper = true
		case LowerCase:
			lower = true
		case TrimSpaces, Empty:
		default:
			return errors.New("unknown conversion")
		}
	}

	if upper && lower {
		return errors.New("upper and lower conversions conflict")
	}

	return nil
}

func main() {
	opts := parseFlags()

	if err := validateFlags(opts); err != nil {
		_, _ = fmt.Fprintln(os.Stderr, "can not parse flags: ", err)
		os.Exit(1)
	}

	byteData, err := read(opts)
	if err != nil {
		_, _ = fmt.Fprintln(os.Stderr, "input file reading error: ", err)
		os.Exit(1)
	}

	if err := write(opts, &byteData); err != nil {
		_, _ = fmt.Fprintln(os.Stderr, "output file writing error: ", err)
		os.Exit(1)
	}
}

func read(opts *Options) ([]byte, error) {
	input := os.Stdin
	var err error

	if opts.From != "" {
		if input, err = os.OpenFile(opts.From, os.O_RDONLY, 0o644); err != nil {
			return nil, errors.New("can not open input file")
		}
	}

	reader := io.Reader(input)
	if opts.Limit != 0 {
		reader = io.LimitReader(input, int64(opts.Offset+opts.Limit))
	}

	byteData, err := io.ReadAll(reader)
	if err != nil {
		return nil, errors.New("can not read data")
	}

	if opts.Offset > uint(len(byteData)) {
		return nil, errors.New("offset greater than input file size")
	}

	if err = input.Close(); err != nil {
		return nil, errors.New("can not close input file")
	}

	return byteData[opts.Offset:], nil
}

func write(opts *Options, byteData *[]byte) error {
	output := os.Stdout
	var err error

	if opts.To != "" {
		if output, err = os.OpenFile(opts.To, os.O_WRONLY, 0o644); err != nil {
			return errors.New("can not open output file")
		}
	}

	writer := convWriter(opts, output)
	if _, err = writer.Write(*byteData); err != nil {
		return errors.New("can not write data")
	}

	if err = output.Close(); err != nil {
		return errors.New("can not close output file")
	}

	return nil
}

func convWriter(opts *Options, writer io.Writer) io.Writer {
	for _, conv := range strings.Split(opts.Conv, ",") {
		switch conv {
		case UpperCase:
			writer = NewUpperCase(writer)
		case LowerCase:
			writer = NewLowerCase(writer)
		case TrimSpaces:
			writer = NewTrimSpace(writer)
		}
	}

	return writer
}
