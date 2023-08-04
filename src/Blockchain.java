import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Blockchain {
    static List<Block> blockchain = new ArrayList<>();

    private static int userMenu() {
        System.out.println("Enter 1 to add a block to the blockchain");
        System.out.println("Enter 2 to print the contents of the current block");
        System.out.println("Enter 3 to print the contents of the entire blockchain");
        System.out.println("Enter 4 to exit");
        Scanner scanner = new Scanner(System.in);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        while (true) {
            int userInput = userMenu();
            switch (userInput) {
                case 1:
                    System.out.println("Input the data for the current block: ");
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();
                    Block block = new Block(input);
                    blockchain.add(block);
                    if (blockchain.size() == 1) {
                        blockchain.get(0).setPreviousHash("NULL");
                    }
                    break;
                case 2:
                    Block currentBlock = blockchain.get(blockchain.size() - 1);
                    currentBlock.displayBlock();
                    break;
                case 3:
                    displayBlockchain();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Enter a valid input.");
                    break;
            }
        }
    }

    private static void displayBlockchain() {
        System.out.println("\nThe contents of the entire blockchain are as follows:");
        for (Block block : blockchain) {
            block.displayBlock();
        }
    }
}

class Block {
    private String previousHash;
    private String currentHash;
    private String blockData;
    private String timestamp;

    public Block(String blockData) {
        this.blockData = blockData;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try {
            this.currentHash = hashCurrentData(blockData);
            if (Blockchain.blockchain.size() > 0) {
                this.previousHash = Blockchain.blockchain.get(Blockchain.blockchain.size() - 1).getCurrentHash();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static String hashCurrentData(String blockData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] encodedHash = digest.digest(blockData.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void displayBlock() {
        System.out.println("Current data in block is: " + blockData);
        System.out.println("Timestamp of current block is: " + timestamp);
        System.out.println("Current hash is: " + currentHash);
        System.out.println("Previous hash is: " + previousHash + "\n");
    }
}
