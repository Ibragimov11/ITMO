module HW1.T5
  ( splitOn
  , joinWith
  ) where

import Data.List.NonEmpty( NonEmpty(..) )

splitOn :: Eq a => a -> [a] -> NonEmpty [a]
splitOn sep = foldr (\c (h :| t) -> if c /= sep then (c:h) :| t else [] :| (h:t)) ([] :| [])

joinWith :: a -> NonEmpty [a] -> [a]
joinWith sep (h :| t) = concat (h : map (sep :) t)
