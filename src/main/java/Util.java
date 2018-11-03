import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Util {

    public static String toSHA(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] applyECDSAsig(PrivateKey privateKey, String input) {
        try {
            Signature dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);

            byte[] inputByte = input.getBytes();
            dsa.update(inputByte);

            return dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyECDSAsig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");

            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());

            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeMerkleRoot(List<Transaction> transactions) {
        List<String> prevTreeLayer = new ArrayList<>();

        for (Transaction transaction : transactions) {
            prevTreeLayer.add(transaction.getTransactionId());
        }

        List<String> treeLayer = prevTreeLayer;

        while (treeLayer.size() > 1) {
            treeLayer = new ArrayList<>();
            for (int i = 0; i < prevTreeLayer.size() - 1; i++) {
                treeLayer.add(toSHA(prevTreeLayer.get(i) + prevTreeLayer.get(i + 1)));
            }
            prevTreeLayer = treeLayer;
        }

        if (treeLayer.size() != 1) return "";

        return treeLayer.get(0);
    }

    public static String makeProfString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    public static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
