a = [s for s in input().split()]
b = []
for x in a:
    if x == "*":
        k = b[len(b) - 2] * b[len(b) - 1]
        b.pop()
        b.pop()
        b.append(k)
    elif x == "+":
        k = b[len(b) - 2] + b[len(b) - 1]
        b.pop()
        b.pop()
        b.append(k)
    elif x == "-":
        k = b[len(b) - 2] - b[len(b) - 1]
        b.pop()
        b.pop()
        b.append(k)
    else:
        b.append(int(x))
print(b[0])