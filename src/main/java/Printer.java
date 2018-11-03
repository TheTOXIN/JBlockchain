import com.google.gson.GsonBuilder;

public class Printer {

    private static final GsonBuilder gsonBuilder = new GsonBuilder();

    public static void printChain() {
        String json = gsonBuilder.setPrettyPrinting().create().toJson(Chain.BLOCKCHAIN);
        System.out.println(json);
    }

}
