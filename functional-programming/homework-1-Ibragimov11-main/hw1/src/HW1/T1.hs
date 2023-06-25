{-# LANGUAGE LambdaCase #-}

module HW1.T1 
  ( Day(..)
  , nextDay
  , afterDays
  , isWeekend
  , daysToParty
  ) where

import GHC.Natural (Natural)

data Day = 
    Monday 
    | Tuesday 
    | Wednesday 
    | Thursday 
    | Friday 
    | Saturday 
    | Sunday 
    deriving (Eq, Show)

nextDay :: Day -> Day
nextDay = \case
    Monday    -> Tuesday
    Tuesday   -> Wednesday
    Wednesday -> Thursday
    Thursday  -> Friday
    Friday    -> Saturday
    Saturday  -> Sunday
    Sunday    -> Monday

afterDays :: Natural -> Day -> Day
afterDays n d = case n of
    0 -> d
    _ -> afterDays (n - 1) . nextDay $ d

isWeekend :: Day -> Bool
isWeekend = \case
    Saturday -> True
    Sunday   -> True
    _        -> False

daysToParty :: Day -> Natural
daysToParty = \case
    Friday -> 0
    d -> (+ 1) . daysToParty . nextDay $ d
