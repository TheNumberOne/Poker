package io.github.thenumberone

import kotlin.test.Test
import kotlin.test.assertEquals

internal class HandKtTest {
    private val deck = parseHand("2H 3D 5S 9C KD")

    @Test
    fun `The deck was correctly parsed and getCard retrieves the correct values`() {
        assertEquals(parseCard("2H"), deck.getCard(0))
        assertEquals(parseCard("3D"), deck.getCard(1))
        assertEquals(parseCard("5S"), deck.getCard(2))
        assertEquals(parseCard("9C"), deck.getCard(3))
        assertEquals(parseCard("KD"), deck.getCard(4))
    }

    @Test
    fun `Calling withCard should set the appropriate card`() {
        val card = parseCard("AS")
        val withCard = deck.withCard(1, card)
        assertEquals(card, withCard.getCard(1))
    }

    @Test
    fun deck() {
        assertEquals(deck, hand(
            parseCard("2H"),
            parseCard("3D"),
            parseCard("5S"),
            parseCard("9C"),
            parseCard("KD")
        )
        )
    }

    @Test
    fun `Sorting a scrambled version should produce the original (which was already sorted)`() {
        val scrambled = parseHand("2H KD 9C 5S 3D")
        assertEquals(deck, scrambled.sortedHand())
    }


}