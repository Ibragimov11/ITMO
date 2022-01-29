n, x, y = map(int, input().split())
k = n - 1
l = 0
r = max(x, y) * k
while r - l > 1:
    m = (r + l) // 2
    if (m // x) + (m // y) >= k:
        r = m
    else:
        l = m
print(r + min(x, y))
