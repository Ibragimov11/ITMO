module HW2.T3 
    ( joinOption
    , joinExcept
    , joinAnnotated
    , joinList
    , joinFun
    ) where

import HW2.T1
    ( Fun(..)
    , List(..)
    , Except(..)
    , Annotated(..)
    , Option(..)
    )

joinOption :: Option (Option a) -> Option a
joinOption None      = None
joinOption (Some op) = op

joinExcept :: Except e (Except e a) -> Except e a
joinExcept (Error e)    = Error e
joinExcept (Success ex) = ex

joinAnnotated :: Semigroup e => Annotated e (Annotated e a) -> Annotated e a
joinAnnotated ((a :# e1) :# e2) = a :# (e2 <> e1)

joinList :: List (List a) -> List a
joinList Nil              = Nil
joinList (Nil :. t)       = joinList t
joinList ((x :. xs) :. t) = x :. joinList (xs :. t)

joinFun :: Fun i (Fun i a) -> Fun i a
joinFun f = F (\i -> unpackFun (unpackFun f i) i) where
    unpackFun (F fun) = fun