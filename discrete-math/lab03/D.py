s = str(input())
letters = ["a", "b", "c", "d", "e", "f", "g", "h",
           "i", "j", "k", "l", "m", "n", "o", "p", "q",
           "r", "s", "t", "u", "v", "w", "x", "y", "z"]
for i in range(len(s)):
    print(letters.index(s[i]) + 1, end=' ')
    let = [letters[letters.index(s[i])]]
    for i in range(26):
        if not letters[i] in let:
            let.append(letters[i])
    letters = let
