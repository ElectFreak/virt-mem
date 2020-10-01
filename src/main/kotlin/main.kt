import java.util.Queue
import java.util.LinkedList

const val size = 3

fun main(args: Array<String>) {
//    val input = randomInput()

    val input = "18 9 10 18 13 17 9 10 3 15 3 16 12 17 2 2 3 2 2 2 15 4 3 1 18 7 14 1 14 1 3 15 7 2 16 7 4 14 11 16 14 17 5 16 7 12 15 2 13 15 11 1 2 3 1 5 15 17 13 6 1 8 12 12 8 12 18 15 8 16 9 18 18 11 9 15 7 15 6"
        .split(" ").map {it.toInt()}
    println(input.joinToString(" "))
    println(fifo(input).joinToString(" "))
}

fun randomInput(): Array<Int> {
//    random sequence of requests with random size

    val size = (15..20).random()
    return Array<Int>(size) {
        (1..5).random()
    }
}


// Algorithms

fun fifo(listOfQueries: List<Int>): MutableList<Int> {
    val listOfPages: Queue<Int> = LinkedList<Int>()

    val listOfAnswers = mutableListOf<Int>()

    for (queryPage in listOfQueries) {

        if (listOfPages.isEmpty()) {
//          The first request
            listOfPages.add(queryPage)
            listOfAnswers.add(-2)
        } else if (queryPage in listOfPages) {
//           queryPage is already loaded
            listOfAnswers.add(-1)
        } else if (listOfPages.size < size) {
//           memory is not overloaded, we don't have to replace any item
            listOfPages.add(queryPage)
            listOfAnswers.add(-2)
        } else {
//          memory is overloaded and there is no request item in memory, we have to replace something
            listOfPages.add(queryPage)
            listOfAnswers.add(listOfPages.poll())
        }

    }

    return listOfAnswers

}

