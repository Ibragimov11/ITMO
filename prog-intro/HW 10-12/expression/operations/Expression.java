package expression.operations;

public interface Expression extends ToMiniString {
    int evaluate(int valueOfVariable);
}
