n = int(input())
a1 = []
a2 = []
for i in range(n):
    a1.append([int(s) for s in input().split()])
for i in range(n):
    a2.append([int(s) for s in input().split()])
b1 = [1] * 5
b2 = [1] * 5
r1 = r2 = s1 = s2 = as1 = as2 = 1
ar1 = ar2 = 0
for i in range(n):
    r1 *= a1[i][i]
    r2 *= a2[i][i]
    ar1 += a1[i][i]
    ar2 += a2[i][i]
    for j in range(i, n):
        if a1[i][j] != a1[j][i]:
            s1 = 0
        if a2[i][j] != a2[j][i]:
            s2 = 0
        if a1[i][j] == 1 and a1[j][i] == 1 and i != j:
            as1 = 0
        if a2[i][j] == 1 and a2[j][i] == 1 and i != j:
            as2 = 0
b1[0] = r1
b2[0] = r2
if ar1 > 0:
    b1[1] = 0
if ar2 > 0:
    b2[1] = 0
b1[2] = s1
b2[2] = s2
b1[3] = as1
b2[3] = as2
t1 = t2 = 1
for i in range(n):
    for j in range(n):
        if t1 == 0:
            break
        if a1[i][j] == 1:
            for k in range(n):
                if t1 == 0:
                    break
                if a1[j][k] == 1 and a1[i][k] == 0:
                    t1 = 0
for i in range(n):
    if t2 == 0:
        break
    for j in range(n):
        if t2 == 0:
            break
        if a2[i][j] == 1:
            for k in range(n):
                if t2 == 0:
                    break
                if a2[j][k] == 1 and a2[i][k] == 0:
                    t2 = 0
b1[4] = t1
b2[4] = t2
a3 = []
for i in range(n):
    a3.append([0]*n)
for i in range(n):
    for j in range(n):
        if a1[i][j] == 1:
            for k in range(n):
                if a2[j][k] == 1 and a3[i][k] == 0:
                    a3[i][k] = 1
for i in range(5):
    print(b1[i], end=" ")
print()
for i in range(5):
    print(b2[i], end=" ")
print()
for i in range(n):
    for j in range(n):
        print(a3[i][j], end=" ")
    print()