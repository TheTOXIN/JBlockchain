import lombok.Data;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Wallet {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Map<String, TransactionOutput> UTXOs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float computeBalance() {
        float total = 0;

        for (Map.Entry<String, TransactionOutput> item : Chain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.itsMe(publicKey)) {
                UTXOs.put(UTXO.getId(), UTXO);//TODO вынесьт запонение
                total += UTXO.getValue();
            }
        }

        return total;
    }

    public Transaction sendCoins(PublicKey receiver, float value) {
        if (computeBalance() < value) {
            System.out.println("NOT ENOUGH COINS");
            return null;
        }

        List<TransactionInput> inputs = new ArrayList<>();

        float total = 0;

        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if (total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, receiver, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs) {
            UTXOs.remove(input.getTransactionOutputId());
        }

        return newTransaction;
    }

}
