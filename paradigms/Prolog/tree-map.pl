map_build(ListMap, TreeMap) :- length(ListMap, Size), map_build(ListMap, Size, TreeMap).

map_build([], 0, []) :- !.
map_build([V], 1, [V, [], []]) :- !.
map_build(ListMap, Size, [V, L, R]) :- Middle is div(Size, 2), get_V(ListMap, Middle, V),
					                    get_Left(ListMap, Middle, Left), map_build(Left, L),
										get_Right(ListMap, Middle, Right), map_build(Right, R).

get_V([H | _], 0, H) :- !.
get_V([H | T], I, R) :- I1 is I - 1, get_V(T, I1, R).

get_Left(_, 0, []) :- !.
get_Left([H | T], I, [H | R]) :- I1 is I - 1, get_Left(T, I1, R).

get_Right([H | T], 0, T) :- !.
get_Right([H | T], I, R) :- I1 is I - 1, get_Right(T, I1, R).

map_get([(K, V), _, _], K, V) :- !.
map_get([(K1, _), L, _], K, V) :- K < K1, map_get(L, K, V), !.
map_get([(K1, _), _, R], K, V) :- K > K1, map_get(R, K, V), !.

map_keys([], []) :- !.
map_keys([(K, _), L, R], Keys) :- map_keys(L, L_Keys), map_keys(R, R_Keys),
										add_all(L_Keys, [K], Q), add_all(Q, R_Keys, Keys), !.

map_values([], []) :- !.
map_values([(_, V), L, R], Values) :- map_values(L, L_Values), map_values(R, R_Values),
										add_all(L_Values, [V], Q), add_all(Q, R_Values, Values), !.

add_all([], [], []) :- !.
add_all([], [H2 | T2], [H2 | R2]) :- add_all([], T2, R2), !.
add_all([H1 | T1], B, [H1 | R1]) :- add_all(T1, B, R1).
