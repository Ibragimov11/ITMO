module HW0.T4
  ( fac
  , fib
  , map'
  , repeat'
  ) where

import Data.Function (fix)
import GHC.Natural (Natural)

repeat' :: a -> [a] -- behaves like Data.List.repeat
repeat' x = fix (x :)

map' :: (a -> b) -> [a] -> [b] -- behaves like Data.List.map
map' f = fix (\rec a -> case a of
        [] -> []
        (x : xs) -> f x : rec xs
        )

fib :: Natural -> Natural -- computes the n-th Fibonacci number
fib n = case n of
    0 -> 1
    1 -> 1
    _ -> fix (\rec last2 last1 i ->
            if i == n
            then last1 + last2
            else rec last1 (last1 + last2) (i + 1)
            ) 1 1 2

fac :: Natural -> Natural -- computes the factorial
fac = fix (\rec n -> if n <= 1 then 1 else n * rec (n - 1))
