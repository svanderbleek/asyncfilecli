import java.io.File
import java.nio.file.FileSystems
import java.util.Scanner
import java.nio.file.StandardWatchEventKinds

import kotlin.system.exitProcess
import kotlin.io.path.Path
import kotlin.concurrent.thread

class Payments(val user: String, val partner: String) {
    val userFile: File
    val partnerFile: File
    var prevBalance: Int

    init {
        userFile = createFileIfNotExists(fileString(user))
        partnerFile = createFileIfNotExists(fileString(partner))

        prevBalance = readBalance(userFile)
    }

    fun fileString(name: String): String {
        return "${name}.trustline"
    }

    fun createFileIfNotExists(filename: String): File {
        val file = File(filename)

        if(!file.exists()) {
            file.createNewFile()
        }

        return file
    }

    fun readBalance(file: File): Int {
        val balance = file.readText()

        if(balance.isBlank()) {
            return 0
        }

        return balance.toInt()
    }

    fun writeBalance(file: File, balance: Int) {
        file.writeText(balance.toString())
    }

    fun userBalance(): Int {
        return readBalance(userFile)
    }

    fun payPartner(ammount: Int) {
        var userBalance = readBalance(userFile)
        var partnerBalance = readBalance(partnerFile)

        userBalance -= ammount
        partnerBalance += ammount

        writeBalance(userFile, userBalance)
        writeBalance(partnerFile, partnerBalance)
    }

    fun matchPayment(file: String): Int {
        if(file == userFile.getName()) {
            val newBalance = readBalance(userFile)
            val amount = newBalance - prevBalance

            if(amount != 0) {
                prevBalance = newBalance
            }

            if(amount > 0) {
                return amount
            }
        }

        return 0
    }
}

fun main(args: Array<String>) {
    val payments = Payments(args[0], args[1])

    println("Welcome!")

    commandLoop(payments)
}

fun displayPrompt() {
    print("> ")
}

fun watchForPayment(payments: Payments) {
    val currentDir = Path("")
    val watchService = FileSystems.getDefault().newWatchService()

    currentDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)

    while (true) {
        val watchKey = watchService.take()

        for (event in watchKey.pollEvents()) {
            val fileName = event.context().toString()
            val amount = payments.matchPayment(fileName)

            if (amount > 0) {
                println()
                println("You were paid ${amount}!")
                displayPrompt()
            }
        }

        watchKey.reset()
    }
}

fun commandLoop(payments: Payments) {
    thread {
        watchForPayment(payments)
    }

    val input = Scanner(System.`in`)

    while(true) {
        displayPrompt()

        processCommand(input, payments)
    }
}

fun processCommand(input: Scanner, payments: Payments) {
    var command = input.next()

    when (command) {
        "pay" -> {
            val amount = input.nextInt()

            if(amount > 0) {
                println("Sent ${amount}")
                payments.payPartner(amount)
            }
        }
        "balance" -> {
            println(payments.userBalance())
        }
        "exit" -> {
            println("Goodbye.")
            exitProcess(0)
        }
    }
}
