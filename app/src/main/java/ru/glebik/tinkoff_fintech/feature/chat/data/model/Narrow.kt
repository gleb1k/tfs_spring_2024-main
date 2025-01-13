package ru.glebik.tinkoff_fintech.feature.chat.data.model

import kotlinx.serialization.SerialName


class Narrow : ArrayList<NarrowItem>() {
    override fun toString(): String {
        var string = ""

        this.forEachIndexed { index, it ->
            string = if (index != 0)
                "$string, $it"
            else
                "$it"
        }
        return "[$string]"
    }

    companion object {
        const val STREAM = "stream"
        const val TOPIC = "topic"
    }
}


data class NarrowItem(
    @SerialName("operator")
    val `operator`: String,
    @SerialName("operand")
    val operand: String,
) {
    override fun toString(): String = "{\"operator\": \"$operator\", \"operand\": \"$operand\"}"
}