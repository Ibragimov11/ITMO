n = int(input())
a = [[int(x) for x in input().split()], [0] * n]
counts = a[0]
nums = []
for i in range(n):
    nums.append([i])
while len(counts) > 1:
    min1 = 0
    min2 = 1
    if counts[1] < counts[0]:
        min1, min2 = min2, min1
    for i in range(2, len(counts)):
        if counts[i] < counts[min1]:
            min2 = min1
            min1 = i
        elif counts[i] < counts[min2]:
            min2 = i
    newarr = []
    for x in nums[min1]:
        newarr.append(x)
        a[1][x] += 1
    for y in nums[min2]:
        newarr.append(y)
        a[1][y] += 1
    counts.append(counts[min1] + counts[min2])
    nums.append(nums[min1] + nums[min2])
    nums.append(newarr)
    newcounts = []
    newnums = []
    for i in range (len(counts)):
        if i != min1 and i != min2:
            newcounts.append(counts[i])
            newnums.append(nums[i])
    counts = newcounts
    nums = newnums
s = 0
for i in range(n):
    s += a[0][i] * a[1][i]
print(s)
