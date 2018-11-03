import java.util.HashMap;

public class Validation {

    public static boolean checkChain() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = Util.makeProfString(Const.DIFCLT);

        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(Initer.genesisTransaction.getOutputs().get(0).getId(), Initer.genesisTransaction.getOutputs().get(0));

        for (int i = 1; i < Chain.BLOCKCHAIN.size(); i++) {
            currentBlock = Chain.BLOCKCHAIN.get(i);
            previousBlock = Chain.BLOCKCHAIN.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("#Current Hashes not equal");
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPrevHash())) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }

            if (!currentBlock.getHash().substring(0, Const.DIFCLT).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if (!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }

                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for (TransactionInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());

                    if (tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if (input.getUTXO().getValue() != tempOutput.getValue()) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if (currentTransaction.getOutputs().get(0).getReceiver() != currentTransaction.getReceiver()) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }

                if (currentTransaction.getOutputs().get(1).getReceiver() != currentTransaction.getSender()) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }
        }

        System.out.println("Blockchain is valid");
        return true;
    }

}
