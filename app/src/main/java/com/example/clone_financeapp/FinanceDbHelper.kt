package com.example.clone_financeapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FinanceDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "FinanceDB"

        private const val TABLE_ACCOUNTS = "Accounts"
        private const val TABLE_BALANCE = "Balance"
        private const val TABLE_MONTHLY_TRANSACTIONS = "Monthly_Transactions"
        private const val TABLE_TRANSACTIONS = "Transactions"

        private const val KEY_ID = "id"
        private const val KEY_ACCOUNT_NAME = "account_name"
        private const val KEY_BALANCE = "balance"
        private const val KEY_INCOMES_COUNT = "incomes_count"
        private const val KEY_EXPENSES_COUNT = "expenses_count"
        private const val KEY_MONTH = "month"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_CATEGORY = "category"
        private const val KEY_DATE = "date"
        private const val KEY_TYPE = "type"
        private const val KEY_WHOM = "whom"
        private const val KEY_COMMENT = "comment"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createAccountsTable = ("CREATE TABLE $TABLE_ACCOUNTS($KEY_ID INTEGER PRIMARY KEY, $KEY_ACCOUNT_NAME TEXT, $KEY_AMOUNT REAL)")
        db.execSQL(createAccountsTable)

        val createBalanceTable = ("CREATE TABLE $TABLE_BALANCE($KEY_ID INTEGER PRIMARY KEY, $KEY_BALANCE REAL)")
        db.execSQL(createBalanceTable)

        val createMonthlyTransactionsTable = ("CREATE TABLE $TABLE_MONTHLY_TRANSACTIONS($KEY_ID INTEGER PRIMARY KEY, $KEY_INCOMES_COUNT INTEGER, $KEY_EXPENSES_COUNT INTEGER, $KEY_MONTH TEXT)")
        db.execSQL(createMonthlyTransactionsTable)

        val createTransactionsTable = ("CREATE TABLE $TABLE_TRANSACTIONS($KEY_ID INTEGER PRIMARY KEY, $KEY_AMOUNT REAL, $KEY_CATEGORY TEXT, $KEY_DATE TEXT, $KEY_TYPE TEXT, $KEY_WHOM TEXT, $KEY_COMMENT TEXT)")
        db.execSQL(createTransactionsTable)

        // Инициализируем таблицу баланса начальным значением
        val initialBalanceValues = ContentValues().apply {
            put(KEY_BALANCE, 0.0)
        }
        db.insert(TABLE_BALANCE, null, initialBalanceValues)

        val initialAccountsValues = ContentValues().apply {
            put(KEY_ID, 1)
            put(KEY_ACCOUNT_NAME, "Кошелек")
            put(KEY_AMOUNT, 0.0)
        }
        db.insert(TABLE_ACCOUNTS, null, initialAccountsValues)
    }

    fun addTransaction(amount: Double, type: String, category: String, account: String, date: String, whom: String, comment: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_AMOUNT, amount)
            put(KEY_TYPE, type)
            put(KEY_CATEGORY, category)
            put(KEY_ACCOUNT_NAME, account)
            put(KEY_DATE, date)
            put(KEY_WHOM, whom)
            put(KEY_COMMENT, comment)
        }
        db.insert(TABLE_TRANSACTIONS, null, values)

        // Обновляем баланс после добавления транзакции
        updateBalanceOrAccount(amount, type, TABLE_BALANCE)
        updateBalanceOrAccount(amount, type, TABLE_ACCOUNTS)

        // Обновляем месячные транзакции
        updateMonthlyTransactions(type, date)

        db.close()
    }

    private fun updateBalanceOrAccount(amount: Double, type: String, tableName: String) {
        val db = this.writableDatabase
        val keyColumn = if (tableName == TABLE_BALANCE) KEY_BALANCE else KEY_AMOUNT

        val cursor = db.rawQuery("SELECT $keyColumn FROM $tableName WHERE $KEY_ID = 1", null)
        var currentAmount = 0.0

        if (cursor.moveToFirst()) {
            currentAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(keyColumn))
        }
        cursor.close()

        currentAmount = if (type == "расход") {
            currentAmount - amount
        } else {
            currentAmount + amount
        }

        val values = ContentValues().apply {
            put(keyColumn, currentAmount)
        }
        db.update(tableName, values, "$KEY_ID = ?", arrayOf("1"))

        db.close()
    }

    private fun updateMonthlyTransactions(type: String, date: String) {
        val db = this.writableDatabase

        val month = date.substring(0, 7) // Предполагаем формат даты "YYYY-MM-DD"

        val cursor = db.rawQuery("SELECT * FROM $TABLE_MONTHLY_TRANSACTIONS WHERE $KEY_MONTH = ?", arrayOf(month))
        if (cursor.moveToFirst()) {
            val incomesCount = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_INCOMES_COUNT))
            val expensesCount = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EXPENSES_COUNT))

            val values = ContentValues().apply {
                if (type == "доход") {
                    put(KEY_INCOMES_COUNT, incomesCount + 1)
                } else if (type == "расход") {
                    put(KEY_EXPENSES_COUNT, expensesCount + 1)
                }
            }
            db.update(TABLE_MONTHLY_TRANSACTIONS, values, "$KEY_MONTH = ?", arrayOf(month))
        } else {
            val values = ContentValues().apply {
                put(KEY_MONTH, month)
                if (type == "доход") {
                    put(KEY_INCOMES_COUNT, 1)
                    put(KEY_EXPENSES_COUNT, 0)
                } else if (type == "расход") {
                    put(KEY_INCOMES_COUNT, 0)
                    put(KEY_EXPENSES_COUNT, 1)
                }
            }
            db.insert(TABLE_MONTHLY_TRANSACTIONS, null, values)
        }
        cursor.close()
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BALANCE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MONTHLY_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNTS")
        onCreate(db)
    }
}

data class Transaction(val amount: Double, val category: String, val date: String)
data class Account(val name: String, val Money_count: Double)




//    @SuppressLint("Range")
//    fun getExpenses(): List<Transaction> {
//        val expenses = mutableListOf<Transaction>()
//        val db = readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM $TABLE_TRANSACTIONS WHERE $KEY_TYPE = 'расход'", null)
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                val amount = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT))
//                val category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY))
//                val date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
//                expenses.add(Transaction(amount, category, date))
//            }
//            cursor.close()
//        }
//        return expenses
//    }
//
//    // Метод для получения данных о доходах
//    @SuppressLint("Range")
//    fun getIncomes(): List<Transaction> {
//        val incomes = mutableListOf<Transaction>()
//        val db = readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM TABLE_ $TABLE_TRANSACTIONS WHERE $KEY_TYPE = 'доход'", null)
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                val amount = cursor.getDouble(cursor.getColumnIndex(KEY_AMOUNT))
//                val category = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY))
//                val date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
//                incomes.add(Transaction(amount, category, date))
//            }
//            cursor.close()
//        }
//        return incomes
//    }
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
//        db.execSQL("DROP TABLE IF EXISTS $TABLE_BALANCE")
//        onCreate(db)
//    }

//    fun addTransaction(amount: Double, category: String, date: String, type: String) {
//        val db = this.writableDatabase
//        val values = ContentValues().apply {
//            put(KEY_AMOUNT, amount)
//            put(KEY_CATEGORY, category)
//            put(KEY_DATE, date)
//            put(KEY_TYPE, type)
//        }
//        db.insert(TABLE_TRANSACTIONS, null, values)
//        db.close()
//
//        // Обновляем баланс после добавления транзакции
//        updateBalance()
//
//    }

//
//    fun getCurrentBalance(): Double {
//        val db = this.readableDatabase
//
//        val balanceQuery = "SELECT $KEY_AMOUNT FROM $TABLE_BALANCE WHERE $KEY_ID = 1"
//        val cursor = db.rawQuery(balanceQuery, null)
//        var balance = 0.0
//        if (cursor.moveToFirst()) {
//            balance = cursor.getDouble(0)
//        }
//        cursor.close()
//        db.close()
//
//        return balance
//    }
//    fun getLastTransactions(limit: Int): List<Transaction> {
//        val transactions = mutableListOf<Transaction>()
//        val db = this.readableDatabase
//        val cursor = db.query(
//            TABLE_TRANSACTIONS,
//            arrayOf(KEY_AMOUNT, KEY_CATEGORY, KEY_DATE),
//            null,
//            null,
//            null,
//            null,
//            "$KEY_ID DESC",
//            "$limit"
//        )
//        with(cursor) {
//            while (moveToNext()) {
//                val amount = getDouble(getColumnIndexOrThrow(KEY_AMOUNT))
//                val category = getString(getColumnIndexOrThrow(KEY_CATEGORY))
//                val date = getString(getColumnIndexOrThrow(KEY_DATE))
//                transactions.add(Transaction(amount, category, date))
//            }
//        }
//        cursor.close()
//        return transactions
//    }
//
//    fun getTransactionsSum(startDate: Date, endDate: Date, type: String): Double {
//        var sum = 0.0
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(
//            "SELECT SUM($KEY_AMOUNT) FROM $TABLE_TRANSACTIONS WHERE $KEY_DATE BETWEEN ? AND ? AND $KEY_TYPE = ?",
//            arrayOf(startDate.time.toString(), endDate.time.toString(), type)
//        )
//        if (cursor.moveToFirst()) {
//            sum = cursor.getDouble(0)
//        }
//        cursor.close()
//        return sum
//    }
//