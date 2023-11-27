import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Bank {
    private List<Customer> customers;

    public Bank() {
        customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    public void displayAllCustomers() {
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    public void transferFunds(Account sourceAccount, Account targetAccount, double amount) {
        if (sourceAccount != null && targetAccount != null) {
            Transaction transaction = new Transaction(amount);
            transaction.performTransaction(sourceAccount, targetAccount);
            transaction.displayTransactionDetails();
        } else {
            System.out.println("Akun Tujuan Tidak Valid");
        }
    }
}

class Customer {
    private String name;
    private String address;
    private List<Account> accounts;

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public void displayAllAccounts() {
        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Override
    public String toString() {
        return "Pelanggan: " + name + ", Alamat: " + address;
    }
}

class Account {
    private static int nextAccountNumber = 1;
    private int accountNumber;
    private double balance;

    public Account() {
        this.accountNumber = generateAccountNumber();
        this.balance = 0.0;
    }

    private int generateAccountNumber() {
        return nextAccountNumber++;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Tarik Tunai. Sisa Saldo: Rp. " + balance);
        } else {
            System.out.println("Dana Tidak Cukup!");
        }
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Akun #" + accountNumber + " - Saldo: Rp. " + balance;
    }
}

class Transaction {
    private static int nextTransactionId = 1;
    private int transactionId;
    private double amount;
    private Date date;

    public Transaction(double amount) {
        this.transactionId = generateTransactionId();
        this.amount = amount;
        this.date = new Date();
    }

    private int generateTransactionId() {
        return nextTransactionId++;
    }

    public void performTransaction(Account sourceAccount, Account targetAccount) {
        if (sourceAccount != null && targetAccount != null) {
            if (sourceAccount.getBalance() >= amount) {
                sourceAccount.withdraw(amount);
                targetAccount.deposit(amount);
            } else {
                System.out.println("Dana Tidak Cukup untuk Transaksi ini !");
            }
        }
    }

    public void displayTransactionDetails() {
        System.out.println("ID Transaksi: " + transactionId +
                           ", Jumlah: Rp. " + amount +
                           ", Tanggal: " + date);
    }
}

class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(double interestRate) {
        super();
        this.interestRate = interestRate;
    }

    public double calculateInterest() {
        return getBalance() * interestRate;
    }
}

class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(double overdraftLimit) {
        super();
        this.overdraftLimit = overdraftLimit;
    }

    public void withdrawWithOverdraft(double amount) {
        double overdraft = amount - getBalance() + overdraftLimit;
        if (overdraft <= 0) {
            withdraw(amount);
        } else {
            System.out.println("Tarik Tunai Tidak Diizinkan. Limit Tarik Tunai Terlampaui!");
        }
    }
}

public class BankApp {
    public static void main(String[] args) {
        Bank bank = new Bank();

        Customer customer1 = new Customer("Azzahra", "Buduran");
        Customer customer2 = new Customer("Marshanda", "Sidoarjo");

        SavingsAccount savingsAccount1 = new SavingsAccount(0.05);
        savingsAccount1.deposit(1000000);

        CheckingAccount checkingAccount1 = new CheckingAccount(500000);
        checkingAccount1.deposit(200000);

        customer1.addAccount(savingsAccount1);
        customer2.addAccount(checkingAccount1);

        bank.addCustomer(customer1);
        bank.addCustomer(customer2);

        bank.displayAllCustomers();

        Transaction transaction = new Transaction(500000);
        transaction.performTransaction(savingsAccount1, checkingAccount1);
        transaction.displayTransactionDetails();

        System.out.println("Bunga yang dihasilkan dari Tabungan: Rp. " + ((SavingsAccount) savingsAccount1).calculateInterest());
        checkingAccount1.withdrawWithOverdraft(700000);
        bank.displayAllCustomers();
    }
}
