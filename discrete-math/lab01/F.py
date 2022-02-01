n, k = map(int, input().split())
a = [[0] * n] * k
pr = False

for i in range(k):
    a[i] = [int(s) for s in input().split()]


def check():
    for i in range(k):
        if a[i].count(-1) == n - 1:
            return True
    return False


while check():
    for i in range(k):
        if a[i].count(-1) == n - 1:
            for j in range(n):
                if a[i][j] >= 0:
                    m = a[i][j]
                    for p in range(k):
                        if a[p][j] == m:
                            a[p] = [-1] * n
                        elif a[p][j] == 1 - m and a[p].count(-1) != n - 1:
                            a[p][j] = -1
                        elif a[p][j] == 1 - m:
                            pr = True
                            a[p][j] = -1
if pr:
    print("YES")
else:
    print("NO")
