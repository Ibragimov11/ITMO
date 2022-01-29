package queue;

public interface Queue {

/*
    Model:
        [..., a1, a2, ..., an, ...]
        n -- размер очереди

    Inv:
        n >= 0
        forall i = 1..n: a[i] != null

    Imm:
        n = n' && forall i = 1..n: a[i] == a'[i]
 */

    // Pred: e != null
    // Post: n = n' + 1 && a[n] = e && forall i = 1..n: a[i] == a'[i]
    public void enqueue(Object element);

    // Pred: n > 0
    // Post: R == a[0] && Imm
    public Object element();

    // Pred: n > 0
    // Post: n == n' - 1 && forall i = 1..n: a[i] == a'[i] && R == a[n']
    public Object dequeue();

    // Pred: True
    // Post: R == n && Imm
    public int size();

    // Pred: True
    // Post: R == (n == 0) && Imm
    public boolean isEmpty();

    // Pred: True
    // Post: n == 0
    public void clear();

    // Pred: n > 0 && 0 <= j <= n - 1
    // Post: R == a[j] && Imm
    public Object get (int i);

    // Pred: n > 0 && 0 <= j <= n - 1 && e != null
    // Post: a[j] == e && n == n' && forall i = 0..j-1, j+1..n: a[i] == a'[i]
    public void set(int i, Object element);

    //Pred: Inv
    //Post: result - Object[] && result != null && forall i = 1..n: result[i] == a[i]
    public Object[] toArray();

    // Pred: Inv
    // Post: n' == n && forall i = 1..n: a[i] == a'[i]
//    public ArrayQueue makeCopy();
}
