import java.security.Security;

public class Main {

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Initer.initChain();
        Test.testChain();
        Validation.checkChain();
        Printer.printChain();
    }

}
