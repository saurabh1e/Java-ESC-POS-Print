public class Main {

    public static void main(String[] args) {
        try {
            new HTTPServer().main();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
