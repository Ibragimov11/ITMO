import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Bank implementation.
 *
 * @author Ibragimov Said
 */
class BankImpl(n: Int) : Bank {
    private val accounts: Array<Account> = Array(n) { Account() }

    override val numberOfAccounts: Int
        get() = accounts.size

    override fun getAmount(index: Int): Long {
        with(accounts[index]) {
            return lock.withLock { amount }
        }
    }

    override val totalAmount: Long
        get() {
            for (account in accounts) {
                account.lock.lock()
            }
            try {
                return accounts.sumOf { it.amount }
            } finally {
                for (account in accounts.reversed()) {
                    account.lock.unlock()
                }
            }
        }

    override fun deposit(index: Int, amount: Long): Long {
        require(amount > 0) { "Invalid amount: $amount" }
        with(accounts[index]) {
            return lock.withLock {
                check(!(amount > Bank.MAX_AMOUNT || this.amount + amount > Bank.MAX_AMOUNT)) { "Overflow" }
                this.amount += amount
                this.amount
            }
        }
    }

    override fun withdraw(index: Int, amount: Long): Long {
        require(amount > 0) { "Invalid amount: $amount" }
        with(accounts[index]) {
            return lock.withLock {
                check(this.amount - amount >= 0) { "Underflow" }
                this.amount -= amount
                this.amount
            }
        }
    }

    override fun transfer(fromIndex: Int, toIndex: Int, amount: Long) {
        require(amount > 0) { "Invalid amount: $amount" }
        require(fromIndex != toIndex) { "fromIndex == toIndex" }
        val from = accounts[fromIndex]
        val to = accounts[toIndex]
        val minIdx = minOf(fromIndex, toIndex)
        val maxIdx = maxOf(fromIndex, toIndex)

        accounts[minIdx].lock.withLock {
            accounts[maxIdx].lock.withLock {
                check(amount <= from.amount) { "Underflow" }
                check(!(amount > Bank.MAX_AMOUNT || to.amount + amount > Bank.MAX_AMOUNT)) { "Overflow" }
                from.amount -= amount
                to.amount += amount
            }
        }
    }

    class Account {
        /**
         * Amount of funds in this account.
         */
        var amount: Long = 0
        val lock = ReentrantLock()
    }
}
