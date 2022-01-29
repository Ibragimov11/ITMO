//
// Created by Саид Ибрагимов on 05.01.2022.
//

#include <map>
#include <set>
#include <iostream>

using namespace std;

int main() {
    freopen("check.in", "r", stdin);
    freopen("check.out", "w", stdout);

    long long n, m;
    bool axiom1 = false, axiom2 = true, axiom3 = true;
    cin >> n >> m;

    set<set<int>> hashSets;

    for (int i = 0; i < m; i++) {
        int size;
        cin >> size;

        if (size == 0) {
            axiom1 = true;
        }

        set<int> set;
        for (int j = 0; j < size; j++) {
            int x;
            cin >> x;
            set.insert(x);
        }

        hashSets.insert(set);
    }

    for (const auto& set1 : hashSets) {
        for (auto e : set1) {
            set<int> set2 = set1;
            set2.erase(e);
            if (hashSets.count(set2) == 0) {
                axiom2 = false;
            }
        }
    }

    if (!axiom1 || !axiom2) {
        cout << "NO";
    } else {
        for (const auto& set1 : hashSets) {
            for (const auto& set2 : hashSets) {
                if (set1.size() <= set2.size()) {
                    continue;
                }
                /*set1.size > set2.size*/
                set<int> set3 = set1;
                for (auto e : set2) {
                    set3.erase(e);
                }

                bool flag = false;
                for (auto e : set3) {
                    set<int> set4 = set2;
                    set4.insert(e);
                    if (hashSets.count(set4) != 0) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    axiom3 = false;
                }
            }
        }

        if (axiom3) {
            cout << "YES";
        } else {
            cout << "NO";
        }
    }

    exit(0);
}