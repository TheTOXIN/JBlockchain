import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Block> blockchain = new ArrayList<>();

    public static int difficulty = 5;

    public static void main(String[] args) {
        System.out.println("Trying to Mine block 1... ");
        addBlock(new Block("Hi im the first block", "0"));

        System.out.println("Trying to Mine block 2... ");
        addBlock(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).getHash()));

        System.out.println("Trying to Mine block 3... ");
        addBlock(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).getHash()));

        if (!isChainValid()) return;

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(json);
    }

    public static boolean isChainValid() {
        Block currBlock;
        Block prevBlock;

        for (int i = 0; i < blockchain.size() - 1; i++) {
            currBlock = blockchain.get(i);
            prevBlock = blockchain.get(i + 1);

            if (!currBlock.calculateHash().equals(prevBlock.getPrevHash()) ||
                !currBlock.getHash().substring(0, difficulty).equals(Util.makeProfString(difficulty))) {
                System.out.println("CHAIN NOT VALID");
                return false;
            }
        }

        return true;
    }

    public static void addBlock(Block block) {
        block.mineBlock(difficulty);
        blockchain.add(block);
    }

}
