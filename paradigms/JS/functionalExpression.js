"use strict";

const operation = (f) => {
    return (a, b) => (x, y, z) => f(a(x, y, z), b(x, y, z));
}
const cnst = v => (x, y, z) => v;

const one = cnst(1);
const two = cnst(2);

const negate = v => (x, y, z) => -v(x, y, z);

const add = operation((a, b) => a + b);

const subtract = operation((a, b) => a - b);

const multiply = operation((a, b) => a * b);

const divide = operation((a, b) => a / b);

const variable = v =>
    (x, y, z) => {
        switch (v) {
            case "x":
                return x
            case "y":
                return y
            case 'z':
                return z
        }
    }
