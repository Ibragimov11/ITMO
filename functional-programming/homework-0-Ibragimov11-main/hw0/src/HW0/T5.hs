module HW0.T5
  ( Nat
  , nz
  , ns
  , nplus
  , nmult
  , nFromNatural
  , nToNum
  ) where

import GHC.Natural (Natural)

type Nat a = (a -> a) -> a -> a

nz :: Nat a
nz _ = id

ns :: Nat a -> Nat a
-- ns :: ((a -> a) -> a -> a) -> (a -> a) -> a -> a
ns n f x = f (n f x)

nplus, nmult :: Nat a -> Nat a -> Nat a
nplus n1 n2 f = n1 f . n2 f
nmult n1 n2 f = n1 (n2 f)

nFromNatural :: Natural -> Nat a
-- nFromNatural :: Natural -> (a -> a) -> a -> a
nFromNatural 0 = nz
nFromNatural n = ns (nFromNatural (n - 1))

nToNum :: Num a => Nat a -> a
nToNum n = n (+ 1) 0
