package io.github.thenumberone

// A deck is represented by 30 bits. Every 6 bits is a card. The deck is indexed in LSB order
typealias Hand = Int
private const val EMPTY_HAND: Hand = 0
const val CARDS_IN_HAND = 5

fun Hand.withCard(index: Int, card: Card): Hand {
    val pos = index * CARD_BITS
    return (this and (CARD_MASK shl pos).inv()) or (card shl pos)
}

fun Hand.getCard(index: Int): Card {
    return (this ushr (index * CARD_BITS)) and CARD_MASK
}

fun hand(c0: Card, c1: Card, c2: Card, c3: Card, c4: Card): Hand {
    return EMPTY_HAND
        .withCard(0, c0)
        .withCard(1, c1)
        .withCard(2, c2)
        .withCard(3, c3)
        .withCard(4, c4)
}

/**
 * Expects deck in form: 2H 3D 5S 9C KD
 */
fun parseHand(s: String): Hand {
    var hand = EMPTY_HAND
    val cards = s.split(' ').mapNotNull {
        try {
            parseCard(it)
        } catch (e: CardParseException) {
            null
        }
    }
    for ((i, card) in cards.withIndex()) {
        hand = hand.withCard(i, card)
    }
    return hand
}

/**
 * Prints deck in form: 2H 3D 5S 9C KD
 */
fun Hand.handToString(): String {
    return (0 until CARDS_IN_HAND).map { getCard(it) }.joinToString(separator = " ") { it.cardToString() }
}



// Swaps the cards if card i is larger than card j
fun Hand.swapIfGreater(i: Int, j: Int): Hand {
    val c1 = getCard(i)
    val c2 = getCard(j)
    return if (c1 > c2) {
        withCard(j, c1).withCard(i, c2)
    } else {
        this
    }
}

fun Hand.sortedHand(): Hand {
    // generated via the bose nelson algorithm for n = 5
    return swapIfGreater(0, 1)
        .swapIfGreater(3, 4)
        .swapIfGreater(2, 4)
        .swapIfGreater(2, 3)
        .swapIfGreater(0, 3)
        .swapIfGreater(0, 2)
        .swapIfGreater(1, 4)
        .swapIfGreater(1, 3)
        .swapIfGreater(1, 2)
}