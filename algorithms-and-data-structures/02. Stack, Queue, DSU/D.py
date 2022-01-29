from collections import deque
import sys

dq = deque()

for t in range(int(sys.stdin.readline())):
    s = sys.stdin.readline()
    b = str(s.split(' ', 1)[0])
    if b != "*" and b != "+":
        sys.stdout.write(str(dq[0]) + str('\n'))
        dq.popleft()
    elif b == '+':
        dq.append(int(s.split(' ', 1)[1]))
    else:
        dq.insert((len(dq) + 1) // 2, int(s.split(' ', 1)[1]))
