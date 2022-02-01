public interface Position {
    boolean isValid(Move move);
    int getN();
    int getM();
    Cell getCell(int r, int c);
}
