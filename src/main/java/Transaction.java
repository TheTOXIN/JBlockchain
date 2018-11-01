import lombok.Data;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Data
public class Transaction {

    private String id;
    private PublicKey sender;
    private PublicKey receiver;
    private float value;
    private byte[] signature;

    private List<TransactionInput> inputs = new ArrayList<>();
    private List<TransactionOutput> outputs = new ArrayList<>();

    public Transaction(
        PublicKey sender,
        PublicKey receiver,
        float value,
        List<TransactionInput> inputs
    ) {
        this.sender = sender;
        this.receiver = receiver;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        return Util.toSHA(
            Util.keyToString(sender) +
            Util.keyToString(receiver) +
            value +
            id
        );
    }

    public void generateSignature(PrivateKey privateKey) {
        this.signature = Util.applyECDSAsig(privateKey, makeData());
    }

    public boolean verifySignature() {
        return Util.verifyECDSAsig(sender, makeData(), signature);
    }

    private String makeData() {
        return Util.keyToString(sender) +
            Util.keyToString(receiver) +
            value;
    }

    public boolean processTransaction() {
        if (!verifySignature()) {
            System.out.println("TRANSACTION NOT VERIFY");
            return false;
        }

        for (TransactionInput input : inputs) {
            input.setUTXO(Chain.UTXOs.get(input.getId()));
        }

        if (getInputsValue() < Chain.minimumTransaction) {
            System.out.println("TRANSACTION INPUTS TO SMALL");
            return false;
        }

        float leftOver = getInputsValue() - value;
        this.id = calculateHash();

        this.outputs.add(new TransactionOutput(id, receiver, value));
        this.outputs.add(new TransactionOutput(id, sender, leftOver));

        for (TransactionOutput output : outputs) {
            Chain.UTXOs.put(output.getId(), output);
        }

        for (TransactionInput input : inputs) {
            if (input.getUTXO() == null) continue;
            Chain.UTXOs.remove(input.getId());
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;

        for (TransactionInput input : inputs) {
            if (input.getUTXO() == null) continue;
            total += input.getUTXO().getValue();
        }

        return total;
    }

    public float getOutputsValue() {
        float total = 0;

        for (TransactionOutput output: outputs) {
            total += output.getValue();
        }

        return total;
    }

}
