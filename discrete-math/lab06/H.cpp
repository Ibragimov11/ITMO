#include <iostream>
#include <vector>
#include <set>

int n;

bool is_zero(const std::vector<std::set<int>>& graph) {
    for (int i = 0; i < n; ++i) {
        if (!graph[i].empty()) {
            return false;
        }
    }

    return true;
}

std::vector<int> chromatic_polynomial(const std::vector<std::set<int>>& graph, int countV) {
    std::vector<int> returnArray;
    returnArray.resize(n + 1);

    if (is_zero(graph)) {
        returnArray[countV] = 1;
        return returnArray;
    }

    std::vector<std::set<int>> graph1(graph), graph2(graph);

    int v = 0, u;

    while (graph[v].empty()) {
        v++;
    }

    u = *graph[v].begin();

    graph1[v].erase(u);
    graph1[u].erase(v);

    graph2[v].erase(u);
    graph2[u].erase(v);

    for (auto w : graph2[u]) {
        graph2[v].insert(w);
        graph2[w].insert(v);
        graph2[w].erase(u);
    }

    graph2[u].clear();

    std::vector<int> result1 = chromatic_polynomial(graph1, countV);
    std::vector<int> result2 = chromatic_polynomial(graph2, countV - 1);

    for (int i = 0; i <= n; i++) {
        returnArray[i] = result1[i] - result2[i];
    }

    return returnArray;
}

int main() {
    int m;
    std::cin >> n >> m;

    std::vector<std::set<int>> graph;
    graph.resize(n);

    for (int i = 0; i < m; i++) {
        int v, u;
        std::cin >> v >> u;
        v--;
        u--;

        graph[v].insert(u);
        graph[u].insert(v);
    }

    std::vector<int> result = chromatic_polynomial(graph, n);

    int index = n;
    while (result[index] == 0) {
        index--;
    }

    std::cout << index << '\n';
    for (int i = n; i >= 0; i--) {
        std::cout << result[i] << " ";
    }

    return 0;
}
