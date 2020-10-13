import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MainKtTest {

    @org.junit.jupiter.api.Test
    fun applyAlgo() {
        val listOfQueries = listOf(16, 14, 7, 11, 10, 4, 16, 8, 20, 2, 8, 1, 18, 7, 8)
        assertAll(
            { assertEquals(Pair(0, listOf(-2, -2, -2)), applyAlgo(listOf(1, 2, 3), 5, ::fifo)) },
            { assertEquals(9, applyAlgo(listOfQueries, 5, ::fifo).first) },
            { assertEquals(8, applyAlgo(listOfQueries, 5, ::lru).first) },
            { assertEquals(6, applyAlgo(listOfQueries, 5, ::opt).first) }
        )
    }

    @org.junit.jupiter.api.Test
    fun fifo() {
        assertAll(
            { assertEquals(4, fifo(listOf(1, 2, 3), mutableListOf(4, 5, 6)).first) },
            { assertEquals(mutableListOf(4, 5, 6), fifo(listOf(3), mutableListOf(4, 5, 6)).second) },
        )
    }

    @org.junit.jupiter.api.Test
    fun lru() {
        assertAll(
                { assertEquals(4, lru(listOf(1, 2, 3), mutableListOf(4, 5, 6)).first) },
                { assertEquals(mutableListOf(4, 5, 6), lru(listOf(3), mutableListOf(4, 5, 6)).second) },
                { assertEquals(mutableListOf(4, 6, 3), lru(listOf(3), mutableListOf(4, 3, 6)).second) },
            )
    }

    @org.junit.jupiter.api.Test
    fun opt() {
        assertAll(
            { assertEquals(4, opt(listOf(1, 2, 3), mutableListOf(4, 5, 6)).first) },
            { assertEquals(mutableListOf(4, 5, 6), opt(listOf(3), mutableListOf(4, 5, 6)).second) },
            { assertEquals(6, opt(listOf(9, 4, 5, 3, 6), mutableListOf(4, 3, 6)).first) },
        )
    }
}