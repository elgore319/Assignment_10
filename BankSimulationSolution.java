
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankSimulationSolution {
    static class BankAccount {
        private int balance;
        public BankAccount(int startingBalance) {
            this.balance = startingBalance;
        }
    public synchronized void withdraw(int amount, String who) {
            try {
                Thread.sleep(new Random().nextInt(5) + 1); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if (balance >= amount) {
                int oldBalance = balance;
                try {
                    Thread.sleep(new Random().nextInt(5) + 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                int newBalance = oldBalance - amount;
                balance = newBalance;
                System.out.println(
                    who + " withdrew $" + amount +
                    " | old balance = " + oldBalance +
                    " -> new balance = " + newBalance
                );
            } else {
                System.out.println(
                    who + " tried to withdraw $" + amount +
                    " but INSUFFICIENT FUNDS. (current balance = " + balance + ")"
                );
            }
        }
        public int getBalance() {
            return balance;
        }
    }
    static class WithdrawTask implements Runnable {
        private final BankAccount account;
        private final String userName;
        private final int amountPerWithdrawal;
        private final int times;
        public WithdrawTask(BankAccount account,
                            String userName,
                            int amountPerWithdrawal,
                            int times) {
            this.account = account;
            this.userName = userName;
            this.amountPerWithdrawal = amountPerWithdrawal;
            this.times = times;
        }
        @Override
        public void run() {
            for (int i = 0; i < times; i++) {
                account.withdraw(amountPerWithdrawal, userName);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {}
            }
        }
    }
    public static void main(String[] args) {
        BankAccount shared = new BankAccount(1000);
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new WithdrawTask(shared, "Alice", 50, 10)));
        threads.add(new Thread(new WithdrawTask(shared, "Bob", 50, 10)));
        threads.add(new Thread(new WithdrawTask(shared, "Charlie", 50, 10)));
        threads.add(new Thread(new WithdrawTask(shared, "Diana", 50, 10)));
        threads.add(new Thread(new WithdrawTask(shared, "ATM-Kiosk", 20, 40)));
        System.out.println("=== Starting transactions with balance = $" + shared.getBalance() + " ===");
        long startTime = System.currentTimeMillis();
        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) {}
        }
        long endTime = System.currentTimeMillis();
        System.out.println("\n=== All transactions finished. ===");
        System.out.println("Expected balance (theoretically) should never go below $0.");
        System.out.println("Actual FINAL balance reported by program = $" + shared.getBalance());
        System.out.println("Total runtime ms: " + (endTime - startTime));
    }
}
