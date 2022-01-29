#include <iostream>
#include <vector>
#include <cmath>
#include <iomanip>

using namespace std;

vector<pair<int, int>> V;

double distance(pair<int, int> &x, pair<int, int> &y) {
    int x1 = x.first;
    int y1 = x.second;
    int x2 = y.first;
    int y2 = y.second;

    return sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
}

int main() {
    int n;
    cin >> n;

    V.resize(n, pair<int, int>(0, 0));
    vector<double> d(n, 0.0);
    vector<bool> inA(n, false);

    for (int i = 0; i < n; i++) {
        int x, y;
        cin >> x >> y;

        V[i] = pair<int, int>(x, y);
    }

    inA[0] = true;
    d[0] = -1;

    for (int i = 1; i < n; i++) {
        d[i] = distance(V[0], V[i]);
    }

    double summaryWeight = 0.0;

    for (int i = 0; i < n - 1; i++) {
        int v = 0;

        while (d[v] <= 0) {
            v++;
        }

        for (int j = v + 1; j < n; j++) {
            if (!inA[j] && d[j] < d[v]) {
                 v = j;
            }
        }

        summaryWeight += d[v];

        inA[v] = true;
        d[v] = -1.0;

        for (int u = 0; u < n; u++) {
            if (!inA[u] && distance(V[min(u, v)], V[max(u, v)]) < d[u]) {
                d[u] = distance(V[min(u, v)], V[max(u, v)]);
            }
        }
    }

    const auto digits = numeric_limits<double>::digits10;
    cout << setprecision(digits) << summaryWeight << endl;

    return 0;
}
