import java.util.Queue
import java.util.LinkedList

const val size = 3

data class Answer(val seq: MutableList<Int>, val secondTypeAnswers: Int)

fun main(args: Array<String>) {
    val input = randomInput()
    println(input)
    println(lru(input))
}

fun randomInput(): List<Int> {
//    random sequence of requests with random size

    val size = (100..150).random()
    return List<Int>(size) {
        (1..10).random()
    }
}


// Algorithms

fun fifo(listOfQueries: List<Int>): Answer {
    val loadedPages: Queue<Int> = LinkedList<Int>()

    val listOfAnswers = mutableListOf<Int>()

    var counter = 0

    for (queryPage in listOfQueries) {

        if (loadedPages.isEmpty()) {
//          The first request
            loadedPages.add(queryPage)
            listOfAnswers.add(-2)
        } else if (queryPage in loadedPages) {
//           queryPage is already loaded
            listOfAnswers.add(-1)
        } else if (loadedPages.size < size) {
//           memory is not overloaded, we don't have to replace any item
            loadedPages.add(queryPage)
            listOfAnswers.add(-2)
        } else {
//          memory is overloaded and there is no request item in memory, we have to replace something
            loadedPages.add(queryPage)
            listOfAnswers.add(loadedPages.poll())

            counter++
        }

    }

    return Answer(listOfAnswers, counter)
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