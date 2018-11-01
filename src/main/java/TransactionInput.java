import lombok.Data;

@Data
public class TransactionInput {

    private String id;
    private TransactionOutput UTXO;

    public TransactionInput(String id) {
        this.id = id;
    }

}
