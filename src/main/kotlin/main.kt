import java.util.*

const val size = 3

data class Answer(val seq: MutableList<Int>, val secondTypeAnswers: Int)

fun main(args: Array<String>) {
    val input = randomInput()
    println(input)
    println(applyAlgo(input, ::lru))
    println(oldLru(input))
}

fun randomInput(): List<Int> {
//    random sequence of requests with random size

    val size = (100..150).random()
    return List<Int>(size) {
        (1..10).random()
    }
}

// Carcass

fun applyAlgo(listOfQueries: List<Int>,
              algo: (restQueries: List<Int>, loadedPages: MutableList<Int>) -> Pair<Int, MutableList<Int>>): Pair<Int, MutableList<Int>> {
    val listOfAnswers: MutableList<Int> = mutableListOf<Int>()
    var loadedPages: MutableList<Int> = mutableListOf<Int>()

    var counter = 0

//    fill memory to end
    var i = 0
    while (loadedPages.size < size) {
        if (listOfQueries[i] !in loadedPages) {
            loadedPages.add(listOfQueries[i])
            listOfAnswers.add(-2)
        } else listOfAnswers.add(-1)
        i++
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

fun lru(restQueries: List<Int>, loadedPages: MutableList<Int>):Pair<Int, MutableList<Int>> {
    return if (restQueries.first() in loadedPages) {
//        move page to the end of list
        loadedPages.remove(restQueries.first())
        loadedPages.add(restQueries.first())

        Pair(-1, loadedPages)
    } else {
        Pair(loadedPages.first(), loadedPages)
    }
}