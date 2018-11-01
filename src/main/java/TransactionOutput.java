import lombok.Data;

import java.security.PublicKey;

@Data
public class TransactionOutput {

    private String id;
    private String parentId;
    private PublicKey receiver;
    private float value;

    public TransactionOutput(String parentId, PublicKey receiver, float value) {
        this.id = Util.toSHA(Util.keyToString(receiver) + value + parentId);

        this.parentId = parentId;
        this.receiver = receiver;
        this.value = value;
    }

    public boolean itsMe(PublicKey publicKey) {
        return publicKey == receiver;
    }

}
