s = ""
def p(k):
    global s
    t = str(k - 1)
    if k == 1:
        s += "((A0|B0)|(A0|B0))"
    else:
        s += "(("
        p(k-1)
        s += "|((A" + t + "|A" + t + ")|(B" + t + "|B" + t + ")))|(A" + t + "|B" + t + "))"

n = int(input())
p(n)
print(s)
