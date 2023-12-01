import java.io.File
import java.util.Scanner
import kotlin.system.exitProcess

class Trustline(val user: String, val partner: String) {
    val userFile: File
    val partnerFile: File

    init {
        userFile = createFileIfNotExists("${user}.trustline")
        partnerFile = createFileIfNotExists("${partner}.trustline")
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
}

fun main(args: Array<String>) {
    val trustline = Trustline(args[0], args[1])

	  println("Welcome to your Trustline!")

    commandLoop(trustline)
}

fun commandLoop(trustline: Trustline) {
    println(trustline.user)
    println(trustline.partner)

    val input = Scanner(System.`in`)

    while(true) {
        print("> ")

        processCommand(input, trustline)
    }
}

fun processCommand(input: Scanner, trustline: Trustline) {
    var command = input.next()

    when (command) {
        "pay" -> {
            val ammount = input.nextInt()
            println("Sent ${ammount}")
            trustline.payPartner(ammount)
        }
        "balance" -> {
            println(trustline.userBalance())
        }
        "exit" -> {
            println("Goodbye.")
            exitProcess(0)
        }
    }
}
