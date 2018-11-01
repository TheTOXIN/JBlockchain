import lombok.Data;

import java.security.PublicKey;

@Data
public class TransactionOutput {

    private String id;
    private String parentTransactionId;
    private PublicKey receiver;
    private float value;

    public TransactionOutput(String parentTransactionId, PublicKey receiver, float value) {
        this.id = Util.toSHA(Util.keyToString(receiver) + value + parentTransactionId);

        this.parentTransactionId = parentTransactionId;
        this.receiver = receiver;
        this.value = value;
    }

    public boolean itsMe(PublicKey publicKey) {
        return publicKey == receiver;
    }

}
