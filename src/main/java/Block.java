import lombok.Data;

import java.util.Date;


@Data
public class Block {

    private String hash;
    private String prevHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String data, String pevHash) {
        this.data = data;
        this.prevHash = pevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return Util.toSHA(
            prevHash + timeStamp + + nonce + data
        );
    }

    public void mineBlock(int difficulty) {
        String target = Util.makeProfString(difficulty);

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Block Mined!!! : " + hash);
    }
}
