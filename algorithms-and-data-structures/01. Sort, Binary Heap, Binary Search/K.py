def ans():
    s = 1
    e = 0
    for i in range(n):
        e += l[i]
    a = 0
    while (s <= e):
        mid = (s + e) // 2
        if (good(mid)):
            a = mid
            e = mid - 1
        else:
            s = mid + 1
    return a
def good(mid):
    count = 0
    sum = 0
    for i in range(n):
        if (l[i] > mid):
            return False
        sum += l[i]
        if (sum > mid):
            count += 1
            sum = l[i]
    count += 1
    if (count <= k):
        return True
    return False
n, k = map(int, input().split())
l = list(map(int, input().split()))
print(ans())
