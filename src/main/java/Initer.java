public class Initer {

    public static Transaction genesisTransaction;
    public static Wallet genesisWallet;
    public static Block genesisBlock;

    public static void initChain() {
        genesisWallet = new Wallet();

        genesisTransaction = new Transaction(genesisWallet.getPublicKey(), genesisWallet.getPublicKey(), 100f, null);
        genesisTransaction.generateSignature(genesisWallet.getPrivateKey());
        genesisTransaction.setTransactionId("0");

        genesisTransaction.getOutputs().add(
                new TransactionOutput(
                        genesisTransaction.getTransactionId(),
                        genesisTransaction.getReceiver(),
                        genesisTransaction.getValue()
                )
        );

        Chain.UTXOs.put(
                genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)
        );

        genesisBlock = new Block("0");
        genesisBlock.addTransaction(genesisTransaction);
        genesisBlock.mineBlock(Const.DIFCLT);

        Chain.BLOCKCHAIN.add(genesisBlock);
    }

}
