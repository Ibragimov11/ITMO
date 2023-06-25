module HW2.T4
    ( State (..)
    , mapState
    , wrapState
    , joinState
    , modifyState
    , Prim (..)
    , Expr (..)
    , eval
    ) where

import HW2.T1 ( Annotated ((:#)), mapAnnotated )
import qualified Control.Monad

newtype State s a = S { runS :: s -> Annotated s a }

mapState :: (a -> b) -> State s a -> State s b
mapState f state = S $ mapAnnotated f . runS state

wrapState :: a -> State s a
wrapState a = S $ \s -> a :# s

joinState :: State s (State s a) -> State s a
joinState oldState = S $ \s ->
    let newState :# s' = runS oldState s
    in runS newState s'

modifyState :: (s -> s) -> State s ()
modifyState f = S $ \s -> () :# f s

instance Functor (State s) where
    fmap = mapState

instance Applicative (State s) where
    pure = wrapState
    p <*> q = Control.Monad.ap p q

instance Monad (State s) where
    m >>= f = joinState (fmap f m)

data Prim a
    = Add a a      -- (+)
    | Sub a a      -- (-)
    | Mul a a      -- (*)
    | Div a a      -- (/)
    | Abs a        -- abs
    | Sgn a        -- signum

data Expr = Val Double | Op (Prim Expr)

instance Num Expr where
    x + y       = Op (Add x y)
    x * y       = Op (Mul x y)
    x - y       = Op (Sub x y)
    abs         = Op . Abs
    signum      = Op . Sgn
    fromInteger = Val . fromInteger

instance Fractional Expr where
    x / y        = Op (Div x y)
    fromRational = Val . fromRational

eval :: Expr -> State [Prim Double] Double
eval (Op (Add e1 e2)) = evalBinary e1 e2 Add (+)
eval (Op (Sub e1 e2)) = evalBinary e1 e2 Sub (-)
eval (Op (Mul e1 e2)) = evalBinary e1 e2 Mul (*)
eval (Op (Div e1 e2)) = evalBinary e1 e2 Div (/)
eval (Op (Abs e))   = evalUnary e Abs abs
eval (Op (Sgn e))   = evalUnary e Sgn signum
eval (Val e)        = evalDouble e

evalBinary 
    :: Expr 
    -> Expr 
    -> (Double -> Double -> Prim Double) 
    -> (Double -> Double -> Double) 
    -> State [Prim Double] Double
evalBinary e1 e2 toPrim f = do
    a <- eval e1
    b <- eval e2
    modifyState (toPrim a b :)
    return (f a b)

evalUnary 
    :: Expr 
    -> (Double -> Prim Double) 
    -> (Double -> Double) 
    -> State [Prim Double] Double
evalUnary e toPrim f = do
    a <- eval e
    modifyState (toPrim a :)
    return (f a)

evalDouble :: Double -> State [Prim Double] Double
evalDouble = return
