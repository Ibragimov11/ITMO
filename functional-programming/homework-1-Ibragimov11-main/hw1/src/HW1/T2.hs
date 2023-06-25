module HW1.T2
  ( N (..)
  , nplus
  , nmult
  , nsub
  , ncmp
  , nFromNatural
  , nToNum
  , nEven
  , nOdd
  , ndiv
  , nmod
  ) where

import Data.Maybe (fromJust)
import GHC.Natural (Natural)

data N = Z | S N deriving (Show)

nplus :: N -> N -> N -- addition
nplus Z x     = x
nplus (S x) y = nplus x (S y)

nmult :: N -> N -> N -- multiplication
nmult Z _     = Z
nmult (S Z) x = x
nmult (S x) y = nplus (nmult x y) y

nsub :: N -> N -> Maybe N -- subtraction     (Nothing if result is negative)
nsub x Z         = Just x
nsub Z _         = Nothing
nsub (S x) (S y) = nsub x y

ncmp :: N -> N -> Ordering -- comparison      (Do not derive Ord)
ncmp Z Z         = EQ
ncmp Z _         = LT
ncmp _ Z         = GT
ncmp (S x) (S y) = ncmp x y

nFromNatural :: Natural -> N
nFromNatural 0 = Z
nFromNatural x = S $ nFromNatural $ x - 1

nToNum :: Num a => N -> a
nToNum Z     = 0
nToNum (S x) = nToNum x + 1

nEven, nOdd :: N -> Bool -- parity checking
nEven Z         = True
nEven (S Z)     = False
nEven (S (S x)) = nEven x

nOdd = not . nEven

ndiv :: N -> N -> N -- integer division
ndiv _ Z = error "division by zero"
ndiv Z _ = Z
ndiv x y = case ncmp x y of
    LT -> Z
    EQ -> S Z
    GT -> S $ ndiv (fromJust $ nsub x y) y

nmod :: N -> N -> N -- modulo operation
nmod _ Z = error "division by zero"
nmod Z _ = Z
nmod x y = case ncmp x y of
    LT -> x
    EQ -> Z
    GT -> nmod (fromJust $ nsub x y) y
