import java.util.Queue
import java.util.LinkedList

const val size = 3

data class Answer(val seq: MutableList<Int>, val secondTypeAnswers: Int)

fun main(args: Array<String>) {
    val input = randomInput()
    println(input)
    println(applyAlgo(input, ::fifo))
}

fun randomInput(): List<Int> {
//    random sequence of requests with random size

    val size = (100..150).random()
    return List<Int>(size) {
        (1..10).random()
    }
}

// Carcass

fun applyAlgo(listOfQueries: List<Int>, algo: (restQueries: List<Int>, loadedPages: List<Int>) -> Int): Pair<Int, MutableList<Int>> {
    val listOfAnswers: MutableList<Int> = mutableListOf<Int>()
    val loadedPages: MutableList<Int> = mutableListOf<Int>()

    var counter = 0

    for (i in 1..size) {
        listOfAnswers.add(-1)
        loadedPages.add(listOfQueries[i])
    }

    for (i in size+1 until listOfQueries.size) {
        if (listOfQueries[i] in loadedPages) {
            listOfAnswers.add(-1)
        } else {
            val pageToReplace: Int = algo(listOfQueries.slice(i until listOfQueries.size), loadedPages)
            if (pageToReplace != -1) {
                counter++
                loadedPages.remove(pageToReplace)
                loadedPages.add(listOfQueries[i])
                listOfAnswers.add(pageToReplace)
            }
        }

    }

    return Pair(counter, listOfAnswers)
}


// Algorithms

fun fifo(restQueries: List<Int>, loadedPages: List<Int>): Int {
    return loadedPages.first()
}

fun lru(listOfQueries: List<Int>): Answer {
    val loadedPages: MutableList<Int> = mutableListOf<Int>()
    val listOfAnswers = mutableListOf<Int>()

    loadedPages.add(listOfQueries.first())

    listOfAnswers.add(-2)

    var counter = 0

    for (queryPage in listOfQueries.slice(1 until listOfQueries.size)) {
        if (queryPage in loadedPages) {
//            Page is already loaded, let move it to the end of list
            loadedPages.remove(queryPage)
            loadedPages.add(queryPage)

            listOfAnswers.add(-1)
        } else if (loadedPages.size < size) {
//            Page is not loaded but we don't have to replace anything
            loadedPages.add(queryPage)

            listOfAnswers.add(-1)
        } else {
//            Page is not loaded and we have to replace something

            listOfAnswers.add(loadedPages.first())
            counter++
            loadedPages.removeFirst()
            loadedPages.add(queryPage)
        }
    }

    return Answer(listOfAnswers, counter)
}

fun opt(listOfQueries: List<Int>): Answer {
    val loadedPages: MutableList<Int> = mutableListOf()
    val listOfAnswers: MutableList<Int> = mutableListOf()

    for ((index, queryPage) in listOfQueries.withIndex()) {
        if (listOfAnswers.isEmpty()) {
            listOfAnswers.add(-1)

            loadedPages.add(queryPage)
        } else if (queryPage in loadedPages) {
            listOfAnswers.add(-1)
        } else if (loadedPages.size < size) {
            loadedPages.add(queryPage)

            listOfAnswers.add(-1)
        } else {

//          replace
            val firstQueriesList: MutableList<Int> = mutableListOf()
            loadedPages.map { pageNumber -> listOfQueries.slice(index until listOfQueries.size).indexOf(pageNumber) }
                .reduce { acc, value ->
                    if (value > acc) value else acc
                }

            listOfAnswers.add(firstQueriesList.last())
        }
    }
    return Answer(listOfAnswers, 0)
}