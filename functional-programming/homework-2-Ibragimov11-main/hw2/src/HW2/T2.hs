module HW2.T2
  ( distAnnotated
  , wrapAnnotated
  , distExcept
  , wrapExcept
  , distFun
  , wrapFun
  , distList
  , wrapList
  , distOption
  , wrapOption
  , distPair
  , wrapPair
  , distPrioritised
  , wrapPrioritised
  , distQuad
  , wrapQuad
  , distStream
  , wrapStream
  ) where

import HW2.T1
    ( Fun(..)
    , List(..)
    , Stream(..)
    , Prioritised(..)
    , Except(..)
    , Annotated(..)
    , Quad(..)
    , Pair(..)
    , Option(..)
    )

distOption :: (Option a, Option b) -> Option (a, b)
distOption (Some a, Some b) = Some (a, b)
distOption _                = None

wrapOption :: a -> Option a
wrapOption = Some

distPair :: (Pair a, Pair b) -> Pair (a, b)
distPair (P a1 a2, P b1 b2) = P (a1, b1) (a2, b2)

wrapPair :: a -> Pair a
wrapPair a = P a a

distQuad :: (Quad a, Quad b) -> Quad (a, b)
distQuad (Q a1 a2 a3 a4, Q b1 b2 b3 b4) = Q (a1, b1) (a2, b2) (a3, b3) (a4, b4)

wrapQuad :: a -> Quad a
wrapQuad a = Q a a a a

distAnnotated :: Semigroup e => (Annotated e a, Annotated e b) -> Annotated e (a, b)
distAnnotated (a :# e1, b :# e2) = (a, b) :# e1 <> e2

wrapAnnotated :: Monoid e => a -> Annotated e a
wrapAnnotated a = a :# mempty

distExcept :: (Except e a, Except e b) -> Except e (a, b)
distExcept (Error e, _)           = Error e
distExcept (_, Error e)           = Error e
distExcept (Success a, Success b) = Success (a, b)

wrapExcept :: a -> Except e a
wrapExcept = Success

distPrioritised :: (Prioritised a, Prioritised b) -> Prioritised (a, b)
distPrioritised (p1, p2) = case (p1, p2) of
    (High a, _)    -> High (a, get p2)
    (_, High b)    -> High (get p1, b)
    (Medium a, _)  -> Medium (a, get p2)
    (_, Medium b)  -> Medium (get p1, b)
    (Low a, Low b) -> Low (a, b)
  where
    get :: Prioritised a -> a
    get (Low a)    = a
    get (Medium a) = a
    get (High a)   = a

wrapPrioritised :: a -> Prioritised a
wrapPrioritised = Low

distStream :: (Stream a, Stream b) -> Stream (a, b)
distStream (a :> atail, b :> btail) = (a, b) :> distStream (atail, btail)

wrapStream :: a -> Stream a
wrapStream a = a :> wrapStream a

distList :: (List a, List b) -> List (a, b)
distList (Nil, _)        = Nil
distList (_, Nil)        = Nil
distList (x :. xs, list) = merge (pairs x list) (distList (xs, list))
    where
        pairs :: a -> List b -> List (a, b)
        pairs _ Nil       = Nil
        pairs a (y :. ys) = (a, y) :. pairs a ys
       
        merge :: List a -> List a -> List a
        merge Nil l      = l
        merge (h :. t) l = h :. merge t l

wrapList :: a -> List a
wrapList a = a :. Nil

distFun :: (Fun i a, Fun i b) -> Fun i (a, b)
distFun (F f, F g) = F (\i -> (f i, g i))

wrapFun :: a -> Fun i a
wrapFun a = F (const a)
