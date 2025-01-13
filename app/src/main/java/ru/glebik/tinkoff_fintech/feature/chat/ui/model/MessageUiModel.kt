package ru.glebik.tinkoff_fintech.feature.chat.ui.model

data class MessageUiModel(
    val id: Int,
    val content: String,

    val senderEmail: String?,
    val senderFullName: String?,
    val senderId: Int?,
    val avatarUrl: String?,

    val streamName: String,
    val streamId: Int?,
    val topic: String?,

    val timestamp: Int?,
    val dateAndTime: MessageDateAndTime,

    val reactions: List<ReactionUiModel>,
    val isMeMessage: Boolean,
)

data class MessageDateAndTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
) {
    companion object {
        fun getByTimestamp(timestamp: Int?): MessageDateAndTime {
            if (timestamp == null) return MessageDateAndTime(0, 0, 0, 0, 0)

            val milliseconds = timestamp * 1000L // секунды в миллисекунды
            val dateTime = java.util.Date(milliseconds)
            val calendar = java.util.Calendar.getInstance()
            calendar.time = dateTime

            return MessageDateAndTime(
                year = calendar.get(java.util.Calendar.YEAR),
                month = calendar.get(java.util.Calendar.MONTH) + 1, //тк месяцы начинаются с 0
                day = calendar.get(java.util.Calendar.DAY_OF_MONTH),
                hour = calendar.get(java.util.Calendar.HOUR_OF_DAY),
                minute = calendar.get(java.util.Calendar.MINUTE)
            )
        }
    }
}
