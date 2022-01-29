"use strict";

function Const (v) {
    this.value = v;
}
Const.prototype = {
    toString : function () { return this.value.toString(); },
    prefix : function () { return this.toString() },
    evaluate : function () { return this.value; }
}

function Variable (ch) {
    this.ch = ch;
}
Variable.prototype = {
    toString : function () { return this.ch; },
    prefix : function ()  {return this.toString() },
    evaluate : function (x, y, z) { return this.ch === 'x' ? x : this.ch === 'y' ? y : z }
}

function Operation (f, operand, args) {
    this.f = f;
    this.operand = operand;
    this.args = args;
}
Operation.prototype = {
    toString: function () {
        let s = "";
        this.args.forEach(item => s += item + " ");
        return s + this.operand;
    },
    prefix: function () {
        let s = "(" + this.operand;
        this.args.forEach(item => s += " " + item.prefix());
        return s + ")";
    },
    evaluate: function (x, y, z) {
        return this.f(this.args.map(item => item.evaluate(x, y, z)))
    }
}

function AbstractOperation (f, operand, args) {
    return Operation.call(this, f, operand, args)
}
AbstractOperation.prototype = Object.create(Operation.prototype)

function CreateBinary (f, s) {
    return function (...args) {
        return new AbstractOperation(f, s, args);
    }
}

const Add = CreateBinary(([a, b]) => a + b, '+');

const Subtract = CreateBinary(([a, b]) => a - b, '-');

const Multiply = CreateBinary(([a, b]) => a * b, '*');

const Divide = CreateBinary(([a, b]) => a / b, '/');

const Negate = CreateBinary(([x]) => -x, 'negate');

const Avg5 = CreateBinary((args1) => args1.reduce((sum, current) => sum + current, 0) / args1.length, 'avg5');

const Med3 = CreateBinary((args1) => args1.sort((a, b) => a - b)[1], 'med3');

const ArithMean = CreateBinary((args1) => args1.reduce((sum, current) => sum + current, 0) / args1.length, 'arith-mean');

const GeomMean = CreateBinary((args1) => Math.pow(args1.reduce((p, current) => p * Math.abs(current), 1), 1 / args1.length), 'geom-mean');

const HarmMean = CreateBinary((args1) => args1.length / (args1.reduce((sum, current) => sum + 1 / current, 0)), 'harm-mean');

const mp = {'+': Add, '-': Subtract,
            '*': Multiply, '/': Divide,
            'negate': Negate, 'med3': Med3,
            'avg5': Avg5, 'arith-mean': ArithMean,
            'geom-mean': GeomMean, 'harm-mean': HarmMean}

const mp2 = {'+': 2, '-': 2,
            '*': 2, '/': 2,
            'negate': 1, 'med3': 3,
            'avg5': 5}

function parsePrefix(s) {
    let i = 0

    function parse() {
        skipWhiteSpace();
        let open_ = false;
        if (i === s.length) {
            throw new ParsingError('Empty input,  pos = ' + (i + 1));
        }
        if (s[i] === '(') {
            i++;
            open_ = true;
        }
        skipWhiteSpace();
        let token = ''
        while (!(isWhiteSpace(s[i]) || s[i] === '(' || s[i] === ')')) {
            token += s[i++];
            if (i === s.length) {
                break;
            }
        }
        if (token === 'x' || token === 'y' || token === 'z') {
            if (open_) {
                throw new ParsingError('Variable op,  pos = ' + (i + 1));
            }
            return new Variable(token);
        } else if (token in mp) {
            let operand = token;
            skipWhiteSpace()
            let arr = []
            while (i < s.length && s[i] !== ')') {
                arr.push(parse())
                skipWhiteSpace()
            }
            if (!open_ && s[i++] === ')') {
                throw new ParsingError('Invalid brackets,  pos = ' + (i + 1));
            } else if (open_ && (i === s.length || s[i++] !== ')')) {
                throw new ParsingError('Missing ),  pos = ' + (i + 1));
            } else if (operand in mp2 && mp2[operand] !== arr.length) {
                throw new ParsingError('Invalid count of arguments: expected ' + mp2[operand] + ' found: ' + arr.length + ',  pos = ' + (i + 1));
            }
            return mp[operand](...arr)
        } else if (!(isNaN(token)) && token.length > 0) {
            if (open_) {
                throw new ParsingError('Const op,  pos = ' + (i + 1));
            }
            return new Const(Number.parseInt(token));
        } else {
            if (open_ && token.length !== 0) {
                throw new ParsingError('Unknown operation,  pos = ' + (i + 1));
            }
            if (open_) {
                throw new ParsingError('Empty op,  pos = ' + (i + 1));
            }
            if ('a' <= token[0].toLowerCase() && token[0].toLowerCase() <= 'z') {
                throw new ParsingError('Unknown variable,  pos = ' + (i + 1));
            }
            throw new ParsingError('Invalid number,  pos = ' + (i + 1));
        }
    }

    function isWhiteSpace(x) {
        return (x === ' ' || x === '\t' || x === '\n')
    }

    function skipWhiteSpace() {
        while (isWhiteSpace(s[i])) {
            i++;
        }
    }

    const ans = parse()
    skipWhiteSpace()
    if (i !== s.length) {
        throw new ParsingError('The expression has extra characters at the end,  pos = ' + (i + 1));
    }
    return ans;
}

function ParsingError(message) {
    Error.call(this, message)
    this.message = message;
}
ParsingError.prototype = Object.create(Error.prototype);
ParsingError.prototype.name = 'ParsingError';
ParsingError.prototype.constructor = ParsingError;
