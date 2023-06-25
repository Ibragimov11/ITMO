{-# LANGUAGE LambdaCase #-}

module HW1.T6
  ( mcat
  , epart
  ) where

import Data.Foldable (Foldable (fold))

mcat :: Monoid a => [Maybe a] -> a
mcat = foldr (mappend . fold) mempty

epart :: (Monoid a, Monoid b) => [Either a b] -> (a, b)
epart = foldMap (\case
                Left l -> (l, mempty)
                Right r -> (mempty, r)
                )
