module HW0.T2
  ( Not
  , doubleNeg
  , reduceTripleNeg
  ) where

import Data.Void (Void)

type Not a = a -> Void

doubleNeg :: a -> Not (Not a)
-- doubleNeg :: a -> (a -> Void) -> Void
doubleNeg x f = f x

reduceTripleNeg :: Not (Not (Not a)) -> Not a
-- reduceTripleNeg :: (((a -> Void) -> Void) -> Void) -> a -> Void
reduceTripleNeg f = f . doubleNeg
