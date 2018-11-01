import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class Block {

    private String hash;
    private String prevHash;
    private long timeStamp;
    private int nonce;
    public String merkleRoot;
    public List<Transaction> transactions = new ArrayList<>();

    public Block(String pevHash) {
        this.prevHash = pevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return Util.toSHA(
            prevHash + timeStamp + + nonce + merkleRoot
        );
    }

    public void mineBlock(int difficulty) {
        merkleRoot = Util.makeMerkleRoot(transactions);
        String target = Util.makeProfString(difficulty);

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Block Mined!!! : " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) return false;

        if (!prevHash.equals("0")) {
            if (!transaction.processTransaction()) {
                System.out.println("TRANSACTION ADD FAILED");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("TRANSACTION ADD SUCCESSFULLY");

        return true;
    }

}
