import java.io.File
import java.lang.Exception

fun main(args: Array<String>) {
    val (listOfQueries, size) = askInput()
    println(applyAlgo(size, listOfQueries, ::fifo))
}

fun askInput(): Pair<Int, List<Int>> {
    println("Enter path to file")
    return validateInput(readLine())
}

fun validateInput(path: String?): Pair<Int, List<Int>> {
    try {
        if (path == null) throw Exception("Path is not given")
        val inputData = File(path).readLines()

        if (!inputData[0].all { it.isDigit() }) throw Exception("First line is not digit")
        if (inputData.size < 2) throw Exception("Two strings are required")
        if (inputData.size > 2) println("Be careful: strings after the second will be ignored")
        if (inputData[0].split(" ").size > 1) println("Be careful: there is only one memory size")

        return Pair(inputData[0].toInt(), inputData[1].split(" ").mapNotNull { num ->
            num.map { char ->
                if (!char.isDigit() && !char.isWhitespace()) println("Be careful: only digits and whitespaces are allowed")
            }
            num.toIntOrNull()
        })

    } catch (e: Exception) {
        println("Error")
        println(e.message)
        println("Try again:")

        return askInput()
    }
}

fun randomInput(): List<Int> {
//    random sequence of requests with random size

    val size = (100..150).random()
    return List<Int>(size) {
        (1..10).random()
    }
}

// Carcass

fun applyAlgo(listOfQueries: List<Int>, memorySize: Int,
              algo: (restQueries: List<Int>, loadedPages: MutableList<Int>) -> Pair<Int, MutableList<Int>>): Pair<Int, MutableList<Int>> {
    val listOfAnswers: MutableList<Int> = mutableListOf<Int>()
    var loadedPages: MutableList<Int> = mutableListOf<Int>()

    var counter = 0

//    fill memory to end
    var i = 0
    while (loadedPages.size < memorySize) {
        if (listOfQueries[i] !in loadedPages) {
            loadedPages.add(listOfQueries[i])
            listOfAnswers.add(-2)
        } else listOfAnswers.add(-1)

        i++

        if (i == listOfQueries.size) {
//            if requests are over before memory is filled
            return Pair(0, listOfAnswers)
        }
    }

//    memory is overloaded
    for (index in i until listOfQueries.size) {
        val algoResult: Pair<Int, MutableList<Int>> = algo(listOfQueries.slice(index until listOfQueries.size), loadedPages)

        loadedPages = algoResult.second

        if (algoResult.first != -1) {
            loadedPages.remove(algoResult.first)
            loadedPages.add(listOfQueries[index])

            listOfAnswers.add(algoResult.first)
            counter++
        } else listOfAnswers.add(-1)
    }

    return Pair(counter, listOfAnswers)
}

// Algorithms

fun fifo(restQueries: List<Int>, loadedPages: MutableList<Int>): Pair<Int, MutableList<Int>> {
    return if (restQueries.first() in loadedPages) {
        Pair(-1, loadedPages)
    } else {
        Pair(loadedPages.first(), loadedPages)
    }
}

fun lru(restQueries: List<Int>, loadedPages: MutableList<Int>): Pair<Int, MutableList<Int>> {
    return if (restQueries.first() in loadedPages) {
//        move page to the end of list
        loadedPages.remove(restQueries.first())
        loadedPages.add(restQueries.first())

        Pair(-1, loadedPages)
    } else {
        Pair(loadedPages.first(), loadedPages)
    }
}

fun opt(restQueries: List<Int>, loadedPages: MutableList<Int>): Pair<Int, MutableList<Int>>  {

    if (restQueries.first() in loadedPages) return Pair(-1, loadedPages)

    fun firstQueryToPage(page: Int): Int {
//  count the first access to the page from rest queries
        for (i in 1 until restQueries.size) {
            if (restQueries[i] == page) return i
        }

        return restQueries.size
    }

//    make such list of pairs (for already loaded pages): <number of first query to page, page number>
    val pagesWithFirstQueries: List<Pair<Int, Int>> = loadedPages.map { loadedPage ->
        val firstQuery: Int = firstQueryToPage(loadedPage)
        if (firstQueryToPage(loadedPage) == restQueries.size) {
//            page without any query in future, return out of algo
            return Pair(loadedPage, loadedPages)
        }

        Pair(firstQuery, loadedPage)
    }

    val replaceTo = pagesWithFirstQueries.reduce {acc, value ->
        if (acc.first > value.first) acc else value
    }.second

    return Pair(replaceTo, loadedPages)
}