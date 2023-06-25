{-# LANGUAGE DerivingStrategies         #-}
{-# LANGUAGE GeneralizedNewtypeDeriving #-}
{-# LANGUAGE LambdaCase #-}

module HW2.T6
    ( runP
    , pChar
    , parseError
    , pEof
    , pAbbr
    , parseExpr
    , ParseError (..)
    ) where

import GHC.Natural         (Natural)
import HW2.T5              (ExceptState (runES, ES))
import HW2.T1              (Except (..), Annotated ((:#)))
import Control.Applicative (Alternative(..))
import Control.Monad       (MonadPlus, mfilter)
import HW2.T4              (Expr (..), Prim (..))
import Data.Char           (isUpper, isDigit, isSpace, digitToInt)
import Data.Scientific     (scientific, toRealFloat)

newtype ParseError = ErrorAtPos Natural

newtype Parser a = P (ExceptState ParseError (Natural, String) a)
    deriving newtype (Functor, Applicative, Monad)
-- ExceptState ParseError (Natural, String) a = 
    -- ES { runES :: (Natural, String) -> Except ParseError (Annotated (Natural, String) a) }

runP :: Parser a -> String -> Except ParseError a
runP (P es) s = case runES es (0, s) of
    Error e          -> Error e
    Success (a :# _) -> Success a

pChar :: Parser Char
pChar = P $ ES $ \(pos, s) ->
  case s of
    []     -> Error (ErrorAtPos pos)
    (c:cs) -> Success (c :# (pos + 1, cs))

parseError :: Parser a
parseError = P $ ES $ \(pos, _) -> Error $ ErrorAtPos pos

instance Alternative Parser where
    empty = parseError
    (P p1) <|> (P p2) = P $ ES $ \(pos, s) -> case runES p1 (pos, s) of
        (Success res) -> Success res
        (Error _)     -> runES p2 (pos, s)

instance MonadPlus Parser   -- No methods.

pEof :: Parser ()
pEof = P $ ES $ \(pos, s) -> case s of
    []    -> Success (() :# (pos, s))
    (_:_) -> Error $ ErrorAtPos pos

pAbbr :: Parser String
pAbbr = do
    abbr <- some (mfilter isUpper pChar)
    pEof
    pure abbr

parseExpr :: String -> Except ParseError Expr
parseExpr = runP (pIgnoreWS pExpr <* pEof)

pExpr :: Parser Expr
pExpr = do
    term <- pIgnoreWS pTerm
    pTry pExpr' term

pExpr' :: Expr -> Parser Expr
pExpr' term1 = do
    op <- pIgnoreWS (pChar' '+' <|> pChar' '-')
    term2 <- pIgnoreWS pTerm
    wrapped <- wrapOp op ['+', '-'] term1 term2
    pTry pExpr' wrapped

pTerm :: Parser Expr
pTerm = do
    factor <- pIgnoreWS pFactor
    pTry pTerm' factor

pTerm' :: Expr -> Parser Expr
pTerm' factor1 = do
    op <- pIgnoreWS (pChar' '*' <|> pChar' '/')
    factor2 <- pIgnoreWS pFactor
    wrapped <- wrapOp op ['*', '/'] factor1 factor2
    pTry pTerm' wrapped

pFactor :: Parser Expr
pFactor = pIgnoreWS (pDouble <|> pExprInParentheses)
    where 
        pExprInParentheses :: Parser Expr
        pExprInParentheses = pChar' '(' *> pExpr <* pChar' ')'

pDouble :: Parser Expr
pDouble = do
    a <- pDigits
    pChar' '.'
    b <- pDigits
    let len = -length b
    pure $ Val $ toRealFloat $ scientific (parseInt (a ++ b)) len
    where
        parseInt :: [Char] -> Integer
        parseInt = foldl(\res digit -> res * 10 + toInteger (digitToInt digit)) 0

pTry :: (Expr -> Parser Expr) -> Expr -> Parser Expr
pTry p e = p e <|> pure e

pIgnoreWS :: Parser a -> Parser a
pIgnoreWS p = pWhiteSpaces *> p <* pWhiteSpaces
    where
        pWhiteSpaces :: Parser String
        pWhiteSpaces = many (mfilter isSpace pChar)

pChar' :: Char -> Parser Char
pChar' c = mfilter (== c) pChar

pDigits :: Parser String
pDigits = some (mfilter isDigit pChar)

wrapOp :: Char -> [Char] -> Expr -> Expr -> Parser Expr
wrapOp op allowed e1 e2 = 
    if op `elem` allowed
        then pure $ Op $ toPrim op e1 e2 
        else parseError
            where
                toPrim :: Char -> (Expr -> Expr -> Prim Expr) 
                toPrim = \case
                    '+' -> Add
                    '-' -> Sub
                    '*' -> Mul
                    '/' -> Div
                    _   -> error "Something went wrong"
