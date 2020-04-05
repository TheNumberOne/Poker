package io.github.thenumberone

fun main() {
    while (true) {
        val line = readLine() ?: return
        val (black, white) = line.split("White:")
        val blackGrade = gradeHand(black)
        val whiteGrade = gradeHand(white)

        println(
            when {
                blackGrade < whiteGrade -> "White wins."
                blackGrade > whiteGrade -> "Black wins."
                else -> "Tie."
            }
        )
    }
}