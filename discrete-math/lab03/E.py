s = str(input())
n = len(s)
dictionary = ["a", "b", "c", "d", "e", "f", "g", "h",
              "i", "j", "k", "l", "m", "n", "o", "p", "q",
              "r", "s", "t", "u", "v", "w", "x", "y", "z"]
buf = ""
for i in range(n + 1):
    if i == 0:
        buf = s[0]
    else:
        if i == n:
            print(dictionary.index(buf), end=" ")
        else:
            if buf + s[i] in dictionary:
                buf += s[i]
            else:
                print(dictionary.index(buf), end=" ")
                dictionary.append(buf + s[i])
                buf = s[i]
