module HW1.T3 
  ( Meta(..)
  , Tree(..)
  , mkBranch
  , tsize
  , tdepth
  , tmember
  , tinsert
  , tFromList
  ) where

data Meta = M Int Int deriving (Show)

data Tree a = Leaf | Branch Meta (Tree a) a (Tree a) deriving (Show)

mkBranch :: Tree a -> a -> Tree a -> Tree a
mkBranch l x r = case (l, r) of
    (Leaf, Leaf) -> Branch (M 1 1) l x r
    (Leaf, _)    -> Branch (M (tsize r + 1) (tdepth r + 1)) l x r
    (_, Leaf)    -> Branch (M (tsize l + 1) (tdepth l + 1)) l x r
    (_, _)       -> Branch (M (tsize l + 1 + tsize r) (max (tdepth l) (tdepth r) + 1)) l x r

-- | Size of the tree, O(1).
tsize :: Tree a -> Int
tsize Leaf                      = 0
tsize (Branch (M size _) _ _ _) = size

-- | Depth of the tree.
tdepth :: Tree a -> Int
tdepth Leaf                       = 0
tdepth (Branch (M _ depth) _ _ _) = depth

-- | Check if the element is in the tree, O(log n)
tmember :: Ord a => a -> Tree a -> Bool
tmember _ Leaf = False
tmember e (Branch _ l x r)
    | e < x     = tmember e l
    | e > x     = tmember e r
    | otherwise = True

-- | Insert an element into the tree, O(log n)
tinsert :: Ord a => a -> Tree a -> Tree a
tinsert e Leaf = mkBranch Leaf e Leaf
tinsert e tree@(Branch _ l x r)
    | tmember e tree = tree
    | e < x          = mkBranch (tinsert e l) x r
    | otherwise      = mkBranch l x (tinsert e r)

-- | Build a tree from a list, O(n log n)
tFromList :: Ord a => [a] -> Tree a
tFromList = foldl (flip tinsert) Leaf
