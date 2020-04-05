package io.github.thenumberone

import java.lang.Exception

/**
 * Card type.
 * First 2 LSB bits store the suit
 * Next 4 LSB bits store the value of the card (Jack = 11, Queen = 12, King = 13, Ace = 14).
 */
typealias Card = Int
typealias Value = Int
typealias Suit = Int
const val VALUE_OFFSET = 2
const val VALUE_BITS = 4
const val SUIT_MASK = 0x3
const val CARD_BITS = 6
const val CARD_MASK = 0x3F
const val JACK: Value = 11
const val QUEEN: Value = 12
const val KING: Value = 13
const val ACE: Value = 14
const val CLUB: Suit = 0
const val DIAMOND: Suit = 1
const val SPADE: Suit = 2
const val HEART: Suit = 3

fun card(value: Value, suit: Suit): Card {
    return (value shl VALUE_OFFSET) or suit
}

fun Card.cardSuit(): Suit {
    return this and SUIT_MASK
}

fun Card.cardValue(): Value {
    return this ushr VALUE_OFFSET
}

class CardParseException(message: String): Exception(message)

fun parseCardValue(c: Char): Suit = when (c) {
    in '2'..'9' -> c - '0'
    'T' -> 10 // 10s are denoted by T rather than two characters 1 and 0
    'J' -> JACK
    'Q' -> QUEEN
    'K' -> KING
    'A' -> ACE
    else -> throw CardParseException("Character $c is not a valid card value (2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A")
}

fun parseCardSuit(c: Char): Value = when (c) {
    'C' -> CLUB
    'D' -> DIAMOND
    'S' -> SPADE
    'H' -> HEART
    else -> throw CardParseException("Character $c is not a valid card suit (C, D, H, S)")
}

fun Value.cardValueToChar(): Char = when(this) {
    in 2..9 -> '0' + this
    10 -> 'T'
    JACK -> 'J'
    QUEEN -> 'Q'
    KING -> 'K'
    ACE -> 'A'
    else -> error("Invalid card value $this")
}

fun Suit.suitToChar(): Char = when(this) {
    CLUB -> 'C'
    DIAMOND -> 'D'
    HEART -> 'H'
    SPADE -> 'S'
    else -> error("Invalid card suit $this")
}

fun parseCard(s: String): Card {
    if (s.length != 2) throw CardParseException("String $s must have two characters to be a card.")

    return card(
        parseCardValue(
            s[0]
        ), parseCardSuit(s[1])
    )
}

fun Card.cardToString(): String {
    return "${cardValue().cardValueToChar()}${cardSuit().suitToChar()}"
}