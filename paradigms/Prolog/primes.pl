mult(X, Y) :- 0 is mod(X, Y).

fmult(X, Y) :- X >= Y * Y, mult(X, Y), !.
fmult(X, Y) :- X >= Y * Y, Y1 is Y + 1, fmult(X, Y1).

composite(X) :- composite_table(X).
composite(X) :- number(X), X > 1, fmult(X, 2), assert(composite_table(X)).

prime(X) :- prime_table(X), !.
prime(X) :- number(X), X > 1, not(composite(X)), assert(prime_table(X)).

sorted([]).
sorted([_]).
sorted([A, B | C]) :- A =< B , sorted([B | C]), !.

prime_divisors(A, B) :- number(A), is_list(B), sorted(B), check_divisors(A, B), !.
prime_divisors(A, B) :- is_list(B), sorted(B), get_number(A, B, 1), !.
prime_divisors(A, B) :- number(A), get_divisors(A, B, 2), !.

power_divisors(A, 0, []) :- !.
power_divisors(A, I, R) :- get_divisors(A, B, 2), concat(B, I, R), prime_divisors(A, B), !.

check_divisors(1, []) :- !.
check_divisors(N, [H | T]) :- H > 1, mult(N, H), prime(H), N1 is div(N, H), check_divisors(N1, T), !.

get_number(R, [], A) :- R is A, !.
get_number(R, [H | T], A) :- prime(H), A1 is A * H, get_number(R, T, A1), !.

get_divisors(1, [], _) :- !.
get_divisors(A, [Q | B], Q) :- A >= Q, prime(Q), 0 is mod(A, Q), A1 is div(A, Q), get_divisors(A1, B, Q), !.
get_divisors(A, B, Q) :- A >= Q, Q1 is Q + 1, get_divisors(A, B, Q1), !.

concat(A, I, R) :- concat (A, I, I, R).

concat([], _, _, []) :- !.
concat([H | T], I, 1, [H | R]) :- concat(T, I, I, R), !.
concat([H | T], I, J, [H | R]) :- J1 is J - 1, concat([H | T], I, J1,R), !.
