package homework

import (
	"github.com/pkg/errors"
	"reflect"
	"strconv"
	"strings"
)

var ErrNotStruct = errors.New("wrong argument given, should be a struct")
var ErrInvalidValidatorSyntax = errors.New("invalid validator syntax")
var ErrValidateForUnexportedFields = errors.New("validation for unexported field is not allowed")

type ValidationError struct {
	Err error
}

type ValidationErrors []ValidationError

func (v ValidationErrors) Error() string {
	res := ""
	for _, err := range v {
		res += err.Err.Error()
	}

	return res
}

func Validate(v any) error {
	value := reflect.ValueOf(v)
	if value.Kind() != reflect.Struct {
		return ErrNotStruct
	}

	errs := ValidationErrors{}

	for i := 0; i < value.NumField(); i++ {
		structField := reflect.TypeOf(v).Field(i)
		tag := structField.Tag

		tv, ok := tag.Lookup("validate")
		if !ok {
			continue
		}

		if !structField.IsExported() {
			errs = append(errs, ValidationError{ErrValidateForUnexportedFields})
			continue
		}

		fieldValue := value.Field(i)

		switch fieldValue.Kind() {
		case reflect.Int:
			err := checkIntValidator(int(fieldValue.Int()), tv)
			if err != nil {
				errs = append(errs, ValidationError{err})
			}
		case reflect.String:
			err := checkStringValidator(fieldValue.String(), tv)
			if err != nil {
				errs = append(errs, ValidationError{err})
			}
		case reflect.Slice:
			l := fieldValue.Len()
			if l == 0 {
				continue
			}

			slice := fieldValue.Slice(0, l)

			switch slice.Index(0).Kind() {
			case reflect.Int:
				for j := 0; j < l; j++ {
					err := checkIntValidator(int(slice.Index(j).Int()), tv)
					if err != nil {
						errs = append(errs, ValidationError{err})
					}
				}
			case reflect.String:
				for j := 0; j < l; j++ {
					err := checkStringValidator(slice.Index(j).String(), tv)
					if err != nil {
						errs = append(errs, ValidationError{err})
					}
				}
			default:
				errs = append(errs, ValidationError{errors.New("unsupported slice type")})
			}
		default:
			errs = append(errs, ValidationError{errors.New("unsupported type")})
		}
	}

	if len(errs) != 0 {
		return errs
	}

	return nil
}

func checkIntValidator(num int, v string) error {
	l, r, err := vSplit(v)
	if err != nil {
		return err
	}

	switch l {
	case "in":
		strs := strings.Split(r, ",")
		ints, err := strsToInts(strs)
		if err != nil {
			return err
		}

		return vIn(num, ints)
	case "min":
		return vMin(num, r)
	case "max":
		return vMax(num, r)
	default:
		return errors.New("unknown int validator")
	}
}

func checkStringValidator(s string, v string) error {
	l, r, err := vSplit(v)
	if err != nil {
		return err
	}

	switch l {
	case "len":
		return vLen(s, r)
	case "in":
		strs := strings.Split(r, ",")
		return vIn(s, strs)
	case "min":
		return vMin(len(s), r)
	case "max":
		return vMax(len(s), r)
	default:
		return errors.New("unknown string validator")
	}
}

func strsToInts(strs []string) ([]int, error) {
	ints := make([]int, len(strs))

	for i, s := range strs {
		n, err := strconv.Atoi(s)
		if err != nil {
			return nil, ErrInvalidValidatorSyntax
		} else {
			ints[i] = n
		}
	}

	return ints, nil
}

func vSplit(v string) (string, string, error) {
	if strings.Count(v, ":") != 1 {
		return "", "", ErrInvalidValidatorSyntax
	}

	index := strings.Index(v, ":")
	l, r := v[:index], v[index+1:]

	if r == "" {
		return "", "", ErrInvalidValidatorSyntax
	}

	return l, r, nil
}

func vLen(s, r string) error {
	n, err := strconv.Atoi(r)
	if err != nil {
		return ErrInvalidValidatorSyntax
	}

	if len(s) != n {
		return errors.New("failed 'len' validator")
	}

	return nil
}

func vIn[T comparable](v T, args []T) error {
	for _, arg := range args {
		if v == arg {
			return nil
		}
	}

	return errors.New("failed 'in' validator")
}

func vMin(v int, r string) error {
	m, err := strconv.Atoi(r)
	if err != nil {
		return ErrInvalidValidatorSyntax
	}

	if v < m {
		return errors.New("failed 'min' validator")
	}

	return nil
}

func vMax(v int, r string) error {
	m, err := strconv.Atoi(r)
	if err != nil {
		return ErrInvalidValidatorSyntax
	}

	if v > m {
		return errors.New("failed 'max' validator")
	}

	return nil
}
