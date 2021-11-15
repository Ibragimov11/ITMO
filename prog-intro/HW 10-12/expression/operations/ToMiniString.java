package expression.operations;

public interface ToMiniString {
    default String toMiniString() {
        return toString();
    }
}
