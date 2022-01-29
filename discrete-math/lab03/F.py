n = int(input())
a = [int(x) for x in input().split()]
dictionary = ["a", "b", "c", "d", "e", "f", "g", "h",
              "i", "j", "k", "l", "m", "n", "o", "p", "q",
              "r", "s", "t", "u", "v", "w", "x", "y", "z"]
buf = 0
for i in range(n + 1):
    if i == 0:
        buf = a[0]
    else:
        if i == n:
            print(dictionary[buf], end="")
        else:
            if a[i] >= len(dictionary):
                print(dictionary[buf], end="")
                dictionary.append(dictionary[buf] + dictionary[buf][0])
                buf = a[i]
            elif dictionary[buf] + dictionary[a[i]][0] in dictionary:
                buf = dictionary.index(dictionary[buf] + dictionary[a[i]][0])
            else:
                print(dictionary[buf], end="")
                dictionary.append(dictionary[buf] + dictionary[a[i]][0])
                buf = a[i]
