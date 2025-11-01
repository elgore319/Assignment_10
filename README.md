There are a couple issues with the code provided, with the major bug being the race condition between the different threads and the withdraw() method. Since the threads run at the same time, they withdraw in a random order and there are no catches to stop them from interrupting each other. Since this happens you will get scenarios where thread "a" and thread "b" both read the current balance as "1000$" and after withdrawing 50$ each they both see the remaining balance as "950$" even though the actual balance should be "900$"

![](Images/Withdraw.png)

