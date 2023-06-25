module HW2.T5
    ( ExceptState (..)
    , mapExceptState
    , wrapExceptState
    , joinExceptState
    , modifyExceptState
    , throwExceptState
    , EvaluationError (..)
    , eval
    ) where

import HW2.T1 (Except (Success, Error), Annotated ((:#)), mapAnnotated, mapExcept)
import HW2.T4 (Expr (..), Prim (..))
import qualified Control.Monad
import Control.Monad (when)

newtype ExceptState e s a = ES { runES :: s -> Except e (Annotated s a) }

mapExceptState :: (a -> b) -> ExceptState e s a -> ExceptState e s b
mapExceptState f exceptState = ES $ mapExcept (mapAnnotated f) . runES exceptState

wrapExceptState :: a -> ExceptState e s a
wrapExceptState a = ES $ \s -> Success (a :# s)

joinExceptState :: ExceptState e s (ExceptState e s a) -> ExceptState e s a
joinExceptState oldState = ES $ \s ->
    case runES oldState s of
        (Error e)                  -> Error e
        (Success (newState :# s')) -> runES newState s'

modifyExceptState :: (s -> s) -> ExceptState e s ()
modifyExceptState f = ES $ \s -> Success (() :# f s)

throwExceptState :: e -> ExceptState e s a
throwExceptState e = ES $ \_ -> Error e

instance Functor (ExceptState e s) where
    fmap = mapExceptState

instance Applicative (ExceptState e s) where
    pure = wrapExceptState
    p <*> q = Control.Monad.ap p q

instance Monad (ExceptState e s) where
    m >>= f = joinExceptState (fmap f m)

data EvaluationError = DivideByZero

eval
    :: Expr
    -> ExceptState EvaluationError [Prim Double] Double
eval (Op (Add x y)) = evalBinary x y Add (+)
eval (Op (Sub x y)) = evalBinary x y Sub (-)
eval (Op (Mul x y)) = evalBinary x y Mul (*)
eval (Op (Div x y)) = evalDiv x y
eval (Op (Abs e))   = evalUnary e Abs abs
eval (Op (Sgn e))   = evalUnary e Sgn signum
eval (Val d)        = evalDouble d

evalBinary
    :: Expr
    -> Expr
    -> (Double -> Double -> Prim Double)
    -> (Double -> Double -> Double)
    -> ExceptState EvaluationError [Prim Double] Double
evalBinary e1 e2 toPrim f = do
    a <- eval e1
    b <- eval e2
    modifyExceptState (toPrim a b :)
    return (f a b)

evalDiv
    :: Expr
    -> Expr
    -> ExceptState EvaluationError [Prim Double] Double
evalDiv e1 e2 = do
    a <- eval e1
    b <- eval e2
    when (b == 0) $ throwExceptState DivideByZero
    modifyExceptState (Div a b :)
    return (a / b)

evalUnary
    :: Expr
    -> (Double -> Prim Double)
    -> (Double -> Double)
    -> ExceptState EvaluationError [Prim Double] Double
evalUnary e toPrim f = do
    a <- eval e
    modifyExceptState (toPrim a :)
    return (f a)

evalDouble
    :: Double
    -> ExceptState EvaluationError [Prim Double] Double
evalDouble = return
