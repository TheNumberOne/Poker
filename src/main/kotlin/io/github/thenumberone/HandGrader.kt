package io.github.thenumberone

typealias HandGrade = Int

const val NULL_HAND_GRADE = 0
const val HIGH_CARD_BIT = 0x100000
const val PAIR_BIT = 0x200000
const val TWO_PAIR_BIT = 0x400000
const val THREE_OF_A_KIND_BIT = 0x800000
const val STRAIGHT_BIT = 0x1000000
const val FLUSH_BIT = 0x2000000
const val FULL_HOUSE_BIT = 0x4000000
const val FOUR_OF_A_KIND_BIT = 0x8000000
const val STRAIGHT_FLUSH_BIT = 0x10000000

const val VALUE_MASK = 0xF

fun gradeHand(hand: String): HandGrade =
    gradeHand(
        parseHand(hand)
    )

fun gradeHand(hand: Hand): HandGrade {
    val h = hand.sortedHand()

    val isStraight = isStraight(h)
    val isFlush = isFlush(h)

    if (isStraight && isFlush) {
        return straightFlush(h)
    } else if (isFlush) {
        return flush(h)
    } else if (isStraight) {
        return straight(h)
    }

    val threeOfAKind = tryThreeOfAKindFourOfAKindAndFullHouse(h)
    if (threeOfAKind != NULL_HAND_GRADE) return threeOfAKind

    val pairs = tryPairs(h)
    if (pairs != NULL_HAND_GRADE) return pairs

    return highCard(h)
}

private fun HandGrade.setHandGradeCardValue(i: Int, value: Value): HandGrade {
    val offset = i * VALUE_BITS
    return (this and (VALUE_MASK shl offset).inv()) or (value shl offset)
}

private fun highCard(hand: Hand): HandGrade {
    val grade = HIGH_CARD_BIT
    return copyCardsFromHandToGrade(hand, grade)
}

private fun copyCardsFromHandToGrade(hand: Hand, grade: Int): Int {
    var ret = grade
    for (i in 0 until CARDS_IN_HAND) {
        ret = ret.setHandGradeCardValue(i, hand.getCard(i).cardValue())
    }
    return ret
}

private fun tryPairs(hand: Hand): HandGrade {
    var i = 1
    while (i < 5 && hand.getCard(i).cardValue() != hand.getCard(i - 1).cardValue()) {
        i++
    }
    if (i >= 5) return NULL_HAND_GRADE

    val firstStart = i - 1
    i += 2
    while (i < 5 && hand.getCard(i).cardValue() != hand.getCard(i - 1).cardValue()) {
        i++
    }

    return if (i >= 5) pair(hand, firstStart)
    else twoPair(hand, firstStart, i - 1)
}

private fun twoPair(hand: Hand, firstStart: Int, secondStart: Int): HandGrade {
    val thirdCardIndex = if (firstStart == 1) 0 else if (secondStart == 2) 4 else 2

    return TWO_PAIR_BIT
        .setHandGradeCardValue(0, hand.getCard(thirdCardIndex).cardValue())
        .setHandGradeCardValue(1, hand.getCard(firstStart).cardValue())
        .setHandGradeCardValue(2, hand.getCard(secondStart).cardValue())
}

private fun pair(hand: Hand, pairStart: Int): HandGrade {
    var grade = PAIR_BIT.setHandGradeCardValue(3, hand.getCard(pairStart).cardValue())
    for (i in 0 until CARDS_IN_HAND) {
        if (i < pairStart) {
            grade = grade.setHandGradeCardValue(i, hand.getCard(i).cardValue())
        } else if (i > pairStart + 1) {
            grade = grade.setHandGradeCardValue(i - 2, hand.getCard(i).cardValue())
        }
    }
    return grade
}

private fun tryThreeOfAKindFourOfAKindAndFullHouse(hand: Hand): HandGrade {
    val middle = hand.getCard(2).cardValue()
    var inRow = 0
    var start = 0
    for (i in 0 until CARDS_IN_HAND) {
        if (hand.getCard(i).cardValue() == middle) {
            inRow += 1
        } else if (inRow < 2) {
            inRow = 0
            start = i + 1
        } else {
            break
        }
    }

    if (inRow == 4) return fourOfAKind(hand, start)
    if (inRow == 3) {
        return if (start == 0 && hand.getCard(3).cardValue() == hand.getCard(4).cardValue() || start == 2 && hand.getCard(0).cardValue() == hand.getCard(1).cardValue()) {
            fullHouse(hand, start)
        } else {
            threeOfAKind(hand, start)
        }
    }
    return NULL_HAND_GRADE
}

private fun threeOfAKind(hand: Hand, start: Int): HandGrade {
    return THREE_OF_A_KIND_BIT.setHandGradeCardValue(0, hand.getCard(start).cardValue())
}

private fun fullHouse(hand: Hand, start: Int): HandGrade {
    return FULL_HOUSE_BIT.setHandGradeCardValue(0, hand.getCard(start).cardValue())
}

private fun fourOfAKind(hand: Hand, start: Int): HandGrade {
    return FOUR_OF_A_KIND_BIT.setHandGradeCardValue(0, hand.getCard(start).cardValue())
}

private fun straight(hand: Hand): HandGrade {
    return STRAIGHT_BIT.setHandGradeCardValue(0, hand.getCard(4).cardValue())
}

private fun flush(hand: Hand): HandGrade {
    return copyCardsFromHandToGrade(
        hand,
        FLUSH_BIT
    )
}

fun straightFlush(hand: Hand): HandGrade {
    return STRAIGHT_FLUSH_BIT.setHandGradeCardValue(0, hand.getCard(4).cardValue())
}

fun isFlush(hand: Hand): Boolean {
    val suit = hand.getCard(0).cardSuit()
    for (i in 1 until CARDS_IN_HAND) {
        if (hand.getCard(i).cardSuit() != suit) return false
    }
    return true
}

fun isStraight(hand: Hand): Boolean {
    for (i in 1 until CARDS_IN_HAND) {
        if (hand.getCard(i).cardValue() != hand.getCard(i - 1).cardValue() + 1) return false
    }

    return true
}
