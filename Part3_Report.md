## Problem Summary
The original bank simulation code suffered from:
- Race conditions in the withdraw method.
- Inconsistent balances and possible negative values due to unsynchronized access.
- Inefficient CPU usage.

## Key Fixes in Solution

The withdraw method is now marked with the "synchronized" keyword, ensuring only one thread can modify the balance at a time.
Sleep intervals are randomized to simulate real-world bank activity.

### Updated Code Snippet
```java
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
```

## Scalability & Design Considerations
Could incorporate AtomicInteger or ConcurrentHashMap for larger scale to help with managing multiple users/accounts, as well as runtime speed.
