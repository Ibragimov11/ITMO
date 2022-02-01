public class Main {
    public static void main(String[] args) {
        // чтобы провести 1 игру между 2-мя игроками, нужно просто создать турнир на 2 игроков с 1 кругом
        Tournament tournament = new Tournament();
        tournament.start();
        tournament.getResult();
    }
}
