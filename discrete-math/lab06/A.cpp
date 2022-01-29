#include <iostream>
#include <queue>

int main() {
    int n;
    std::cin >> n;
    std::vector<std::vector<bool>> graph;
    graph.resize(n);

    for (int i = 0; i < n; i++) {
        graph[i].resize(n);
    }

    for (int i = 1; i < n; i++) {
        std::string s;
        std::cin >> s;

        for (int j = 0; j < i; j++) {
            if (s[j] == '1') {
                graph[i][j] = true;
                graph[j][i] = true;
            }
        }
    }

    std::deque<int> deque;

    for (int i = 0; i < n; i++) {
        deque.push_back(i);
    }

    int k = n * (n - 1);
    while (k-- > 0) {
        if (!graph[deque[0]][deque[1]]) {
            int i = 2;

            while (!graph[deque[0]][deque[i]] || !graph[deque[1]][deque[i + 1]]) {
                i++;
            }

            int j = 0;
            while (1 + j < i - j) {
                std::swap(deque[1 + j], deque[i - j]);

                j++;
            }
        }

        deque.push_back(deque.front());
        deque.pop_front();
    }

    while (!deque.empty()) {
        std::cout << deque.front() + 1 << " ";
        deque.pop_front();
    }

    return 0;
}
