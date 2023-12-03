import java.io.File
import java.nio.file.FileSystems
import java.util.Scanner
import java.nio.file.StandardWatchEventKinds

import kotlin.system.exitProcess
import kotlin.io.path.Path
import kotlin.concurrent.thread

class Trustline(val user: String, val partner: String) {
    val userFile: File
    val partnerFile: File

    init {
        userFile = createFileIfNotExists(fileString(user))
        partnerFile = createFileIfNotExists(fileString(partner))
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

    fun userFileString(): String {
        return fileString(user)
    }
}

fun main(args: Array<String>) {
    val trustline = Trustline(args[0], args[1])

	  println("Welcome to your Trustline!")

    commandLoop(trustline)
}

fun displayPrompt() {
  print("> ")
}

fun watchForPayment(file: String) {
  val currentDir = Path("")
  val watchService = FileSystems.getDefault().newWatchService()
  currentDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)

  while (true) {
    val watchKey = watchService.take()

    for (event in watchKey.pollEvents()) {
        if (file == event.context().toString()) {
            println()
            println("You were paid!")
            displayPrompt()
        }
    }

    watchKey.reset()
  }
}
fun commandLoop(trustline: Trustline) {
    thread {
        watchForPayment(trustline.userFileString())
    }

    val input = Scanner(System.`in`)

    while(true) {
        displayPrompt()

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
