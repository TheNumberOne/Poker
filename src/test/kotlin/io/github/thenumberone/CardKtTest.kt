package io.github.thenumberone

import org.example.io.github.thenumberone.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CardKtTest {
    private val c = card(
        3,
        DIAMOND
    )

    @Test
    fun `Create card should work`() {
        // DIAMOND = 1 and is stored in first two LSB bits
        // 3 is stored in the last 4 LSB bits
        assertEquals(0b001101, c)
    }

    @Test
    fun `Create card should work on Ace of Spades`() {
        // SPADE = 2 and is stored in first two LSB bits
        // A = 14 is stored in the last 4 LSB bits
        assertEquals(0b111010,
            card(
                ACE,
                SPADE
            )
        )
    }

    @Test
    fun `The character of hearts should be H`() {
        // HEART = 3 and is stored in first two LSB bits
        // 2 is stored in the last 4 LSB bits
        assertEquals('H', HEART.suitToChar())
    }

    @Test
    fun `cardValue of c should be 3`() {
        assertEquals(3, c.cardValue())
    }

    @Test
    fun `cardSuit of c should be Diamonds`() {
        assertEquals(DIAMOND, c.cardSuit())
    }

    @Test
    fun `cardToString should be 3D`() {
        assertEquals("3D", c.cardToString())
    }

    @Test
    fun `parseCard should work on ace of spaces`() {
        // SPADE = 2 and is stored in first two LSB bits
        // A = 14 is stored in the last 4 LSB bits
        assertEquals(0b111010, parseCard("AS"))
    }

    @Test
    fun `Comparisons of cards should match poker order`() {
        for (value1 in 2..ACE) {
            for (suit1 in CLUB..HEART) {
                val card1 = card(value1, suit1)
                for (value2 in 2..ACE) {
                    for (suit2 in CLUB..HEART) {
                        val card2 = card(value2, suit2)

                        if (value1 < value2) {
                            assert(card1 < card2)
                        } else if (value1 > value2) {
                            assert(card1 > card2)
                        }
                        // we don't test when value1 == value2
                    }
                }
            }
        }
    }
}