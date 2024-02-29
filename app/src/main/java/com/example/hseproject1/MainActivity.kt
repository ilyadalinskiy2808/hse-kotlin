package com.example.hseproject1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hseproject1.ui.theme.Hseproject1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hseproject1Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Hseproject1Theme {
        Greeting("Android")
    }

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerHorizontal="true"
    android:layout_centerVertical="true"
    android:padding="@dimen/padding_medium"
    android:text="@string/hello_world"
    tools:context=".MainActivity" />

    </RelativeLayout>

    import java.time.LocalDateTime
    import java.time.format.DateTimeFormatter
    import kotlin.system.exitProcess

    fun main() {
        val tracker = ExpenseTracker()

        while (true) {
            println("\nExpense Tracker Menu:")
            println("1. Display Balance")
            println("2. Add Expense")
            println("3. Add Income")
            println("4. Display History")
            println("5. Undo Last Transaction")
            println("6. Display Balance by Category")
            println("7. Exit")

            print("Select an option: ")
            when (readLine()!!) {
                "1" -> tracker.displayBalance()
                "2" -> tracker.addTransaction(TransactionType.EXPENSE)
                "3" -> tracker.addTransaction(TransactionType.INCOME)
                "4" -> tracker.displayHistory()
                "5" -> tracker.undoLastTransaction()
                "6" -> tracker.displayBalanceByCategory()
                "7" -> {
                    println("Exiting... Thank you for using Expense Tracker.")
                    exitProcess(0)
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    enum class TransactionType {
        INCOME, EXPENSE
    }

    data class Transaction(val type: TransactionType, val amount: Double, val category: String, val dateTime: LocalDateTime) {
        override fun toString(): String {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            return "Type: $type, Amount: $amount, Category: $category, Date: ${dateTime.format(formatter)}"
        }
    }

    class ExpenseTracker {
        private var balance = 0.0
        private val history = mutableListOf<Transaction>()
        private val categories = mutableSetOf("Food", "Transport", "Utilities")

        fun displayBalance() {
            println("Current balance: $balance")
        }

        fun addTransaction(type: TransactionType) {
            print("Enter amount: ")
            val amountInput = readLine()
            val amount = amountInput?.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                println("Invalid amount. Please enter a positive number.")
                return
            }

            print("Enter category (Optional, press enter to skip): ")
            var category = readLine()!!.trim()
            if (category.isEmpty()) {
                category = if (type == TransactionType.EXPENSE) "General Expense" else "General Income"
            } else {
                categories.add(category)
            }

            val transaction = Transaction(type, if (type == TransactionType.EXPENSE) -amount else amount, category, LocalDateTime.now())
            balance += transaction.amount
            history.add(transaction)
            println("Transaction added successfully.")
        }

        fun displayHistory() {
            if (history.isEmpty()) {
                println("Transaction history is empty.")
                return
            }
            history.forEach { println(it) }
        }

        fun undoLastTransaction() {
            if (history.isNotEmpty()) {
                val lastTransaction = history.removeAt(history.lastIndex)
                balance -= lastTransaction.amount
                println("Last transaction undone. Amount: ${lastTransaction.amount}, Category: ${lastTransaction.category}")
            } else {
                println("No transactions to undo.")
            }
        }

        fun displayBalanceByCategory() {
            if (history.isEmpty()) {
                println("Transaction history is empty.")
                return
            }
            val balanceByCategory = history.groupBy { it.category }.mapValues { entry ->
                entry.value.sumOf { it.amount }
            }
            balanceByCategory.forEach { (category, balance) ->
                println("$category: $balance")
            }
        }
    }