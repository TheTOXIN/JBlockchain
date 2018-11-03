public class Test {

    private static final Wallet walletA = new Wallet();
    private static final Wallet walletB = new Wallet();

    public static void testChain() {
        Block block0 = new Block(Initer.genesisBlock.getHash());
        System.out.println("\nWallet COINBASE balance is: " + Initer.genesisWallet.computeBalance());
        System.out.println("\nWallet COINBASE is Attempting to send funds (100) to WalletA...");
        block0.addTransaction(Initer.genesisWallet.sendCoins(walletA.getPublicKey(), 100f));
        addBlock(block0);

        checkBalance(100, 0);
        logBalance();

        Block block1 = new Block(block0.getHash());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendCoins(walletB.getPublicKey(), 40f));
        addBlock(block1);

        checkBalance(60, 40);
        logBalance();

        Block block2 = new Block(block1.getHash());
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendCoins(walletB.getPublicKey(), 1000f));
        addBlock(block2);

        checkBalance(60, 40);
        logBalance();

        Block block3 = new Block(block2.getHash());
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendCoins(walletA.getPublicKey(), 20));
        addBlock(block3);

        checkBalance(80, 20);
        logBalance();
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(Const.DIFCLT);
        Chain.BLOCKCHAIN.add(newBlock);
    }

    public static void logBalance() {
        System.out.println("\nWalletA's balance is: " + walletA.computeBalance());
        System.out.println("WalletB's balance is: " + walletB.computeBalance());
    }

    public static void checkBalance(float a, float b) {
        if (walletA.computeBalance() != a || walletB.computeBalance() != b) {
            System.out.println("TEST FAILED!!!");
        }
    }

}
