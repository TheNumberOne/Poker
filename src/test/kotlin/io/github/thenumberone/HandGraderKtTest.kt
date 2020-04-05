package io.github.thenumberone

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class HandGraderKtTest {
    //Black: 2H 3D 5S 9C KD  White: 2C 3H 4S 8C AH
    //Black: 2H 4S 4C 2D 4H  White: 2S 8S AS QS 3S
    //Black: 2H 3D 5S 9C KD  White: 2C 3H 4S 8C KH
    //Black: 2H 3D 5S 9C KD  White: 2D 3H 5C 9S KH
    // White wins. - with high card: Ace
    //White wins. - with flash
    //Black wins. - with high card: 9
    //Tie.
    @Test
    fun `examples given`() {
        assertTrue(gradeHand("2H 3D 5S 9C KD") < gradeHand("2C 3H 4S 8C AH"))
        assertTrue(gradeHand("2H 4S 4C 2D 4H") > gradeHand("2S 8S AS QS 3S"))
        assertTrue(gradeHand("2H 3D 5S 9C KD") > gradeHand("2C 3H 4S 8C KH"))
        assertEquals(gradeHand("2H 3D 5S 9C KD"), gradeHand("2D 3H 5C 9S KH"))
    }

    // https://projecteuler.net/problem=54
    @Test
    fun `project euler small example classifies correctly`() {
        assertNotEquals(0, gradeHand("5H 5C 6S 7S KD") and PAIR_BIT)
        assertNotEquals(0, gradeHand("2C 3S 8S 8D TD") and PAIR_BIT)
        assertNotEquals(0, gradeHand("5D 8C 9S JS AC") and HIGH_CARD_BIT)
        assertNotEquals(0, gradeHand("2C 5C 7D 8S QH") and HIGH_CARD_BIT)
        assertNotEquals(0, gradeHand("2D 9C AS AH AC") and THREE_OF_A_KIND_BIT)
        assertNotEquals(0, gradeHand("3D 6D 7D TD QD") and FLUSH_BIT)
        assertNotEquals(0, gradeHand("4D 6S 9H QH QC") and PAIR_BIT)
        assertNotEquals(0, gradeHand("3D 6D 7H QD QS") and PAIR_BIT)
        assertNotEquals(0, gradeHand("2H 2D 4C 4D 4S") and FULL_HOUSE_BIT)
        assertNotEquals(0, gradeHand("3C 3D 3S 9S 9D") and FULL_HOUSE_BIT)
    }

    @Test
    fun `project euler decides winner correctly`() {
        assertTrue(gradeHand("5H 5C 6S 7S KD") < gradeHand("2C 3S 8S 8D TD"))
        assertTrue(gradeHand("2D 9C AS AH AC") < gradeHand("3D 6D 7D TD QD"))
        assertTrue(gradeHand("5D 8C 9S JS AC") > gradeHand("2C 5C 7D 8S QH"))
        assertTrue(gradeHand("4D 6S 9H QH QC") > gradeHand("3D 6D 7H QD QS"))
        assertTrue(gradeHand("2H 2D 4C 4D 4S") > gradeHand("3C 3D 3S 9S 9D"))
    }

    @Test
    fun `project euler big test case`() {
        val testCases = javaClass.classLoader.getResource("p054_poker.txt")!!.readText().lines().map { line ->
            val firstHalf = line.substring(0, line.length / 2)
            val secondHalf = line.substring(line.length / 2)
            Pair(
                firstHalf,
                secondHalf
            )
        }
        val firstWins = testCases.count { (firstHand, secondHand) ->
            gradeHand(firstHand) > gradeHand(secondHand)
        }
        assertEquals(376, firstWins)
    }
}