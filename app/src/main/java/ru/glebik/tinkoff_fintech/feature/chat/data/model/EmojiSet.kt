package ru.glebik.tinkoff_fintech.feature.chat.data.model

// Class Emoji with Name and Code as String
data class EmojiNCS(
    val name: String,
    val code: String,
) {
    companion object {
        fun getCodeString(code: String): String {
            //иногда приходят странные эмоджи, например вот такие 2fkls-sdfm-sdffd1
            runCatching {
                code.toInt(16)
            }.fold(
                onSuccess = { return String(Character.toChars(it)) },
                onFailure = { return BAD_EMOJI_CODE }
            )
        }

        const val BAD_EMOJI_CODE = "null"
    }
}

val emojis
    get() = emojiSetNCS.map { EmojiNCS(it.name, EmojiNCS.getCodeString(it.code)) }

private val emojiSetNCS = listOf(
// Smileys & Emotion
    EmojiNCS("grinning", "1f600"),
    EmojiNCS("smiley", "1f603"),
    EmojiNCS("big_smile", "1f604"),
    EmojiNCS("grinning_face_with_smiling_eyes", "1f601"),
    EmojiNCS("laughing", "1f606"),
    EmojiNCS("sweat_smile", "1f605"),
    EmojiNCS("rolling_on_the_floor_laughing", "1f923"),
    EmojiNCS("joy", "1f602"),
    EmojiNCS("smile", "1f642"),
    EmojiNCS("upside_down", "1f643"),
    EmojiNCS("wink", "1f609"),
    EmojiNCS("blush", "1f60a"),
    EmojiNCS("innocent", "1f607"),
    EmojiNCS("heart_eyes", "1f60d"),
    EmojiNCS("heart_kiss", "1f618"),
    EmojiNCS("kiss", "1f617"),
    EmojiNCS("smiling_face", "263a"),
    EmojiNCS("kiss_with_blush", "1f61a"),
    EmojiNCS("kiss_smiling_eyes", "1f619"),
    EmojiNCS("yum", "1f60b"),
    EmojiNCS("stuck_out_tongue", "1f61b"),
    EmojiNCS("stuck_out_tongue_wink", "1f61c"),
    EmojiNCS("stuck_out_tongue_closed_eyes", "1f61d"),
    EmojiNCS("money_face", "1f911"),
    EmojiNCS("hug", "1f917"),
    EmojiNCS("thinking", "1f914"),
    EmojiNCS("silence", "1f910"),
    EmojiNCS("neutral", "1f610"),
    EmojiNCS("expressionless", "1f611"),
    EmojiNCS("speechless", "1f636"),
    EmojiNCS("smirk", "1f60f"),
    EmojiNCS("unamused", "1f612"),
    EmojiNCS("rolling_eyes", "1f644"),
    EmojiNCS("grimacing", "1f62c"),
    EmojiNCS("lying", "1f925"),
    EmojiNCS("relieved", "1f60c"),
    EmojiNCS("pensive", "1f614"),
    EmojiNCS("sleepy", "1f62a"),
    EmojiNCS("drooling", "1f924"),
    EmojiNCS("sleeping", "1f634"),
    EmojiNCS("cant_talk", "1f637"),
    EmojiNCS("sick", "1f912"),
    EmojiNCS("hurt", "1f915"),
    EmojiNCS("nauseated", "1f922"),
    EmojiNCS("sneezing", "1f927"),
    EmojiNCS("dizzy", "1f635"),
    EmojiNCS("cowboy", "1f920"),
    EmojiNCS("sunglasses", "1f60e"),
    EmojiNCS("nerd", "1f913"),
    EmojiNCS("oh_no", "1f615"),
    EmojiNCS("worried", "1f61f"),
    EmojiNCS("frown", "1f641"),
    EmojiNCS("open_mouth", "1f62e"),
    EmojiNCS("hushed", "1f62f"),
    EmojiNCS("astonished", "1f632"),
    EmojiNCS("flushed", "1f633"),
    EmojiNCS("frowning", "1f626"),
    EmojiNCS("anguished", "1f627"),
    EmojiNCS("fear", "1f628"),
    EmojiNCS("cold_sweat", "1f630"),
    EmojiNCS("exhausted", "1f625"),
    EmojiNCS("cry", "1f622"),
    EmojiNCS("sob", "1f62d"),
    EmojiNCS("scream", "1f631"),
    EmojiNCS("confounded", "1f616"),
    EmojiNCS("persevere", "1f623"),
    EmojiNCS("disappointed", "1f61e"),
    EmojiNCS("sweat", "1f613"),
    EmojiNCS("weary", "1f629"),
    EmojiNCS("anguish", "1f62b"),
    EmojiNCS("triumph", "1f624"),
    EmojiNCS("rage", "1f621"),
    EmojiNCS("angry", "1f620"),
    EmojiNCS("smiling_devil", "1f608"),
    EmojiNCS("devil", "1f47f"),
    EmojiNCS("skull", "1f480"),
    EmojiNCS("poop", "1f4a9"),
    EmojiNCS("clown", "1f921"),
    EmojiNCS("ogre", "1f479"),
    EmojiNCS("goblin", "1f47a"),
    EmojiNCS("ghost", "1f47b"),
    EmojiNCS("alien", "1f47d"),
    EmojiNCS("space_invader", "1f47e"),
    EmojiNCS("robot", "1f916"),
    EmojiNCS("smiley_cat", "1f63a"),
    EmojiNCS("smile_cat", "1f638"),
    EmojiNCS("joy_cat", "1f639"),
    EmojiNCS("heart_eyes_cat", "1f63b"),
    EmojiNCS("smirk_cat", "1f63c"),
    EmojiNCS("kissing_cat", "1f63d"),
    EmojiNCS("scream_cat", "1f640"),
    EmojiNCS("crying_cat", "1f63f"),
    EmojiNCS("angry_cat", "1f63e"),
    EmojiNCS("see_no_evil", "1f648"),
    EmojiNCS("hear_no_evil", "1f649"),
    EmojiNCS("speak_no_evil", "1f64a"),
    EmojiNCS("lipstick_kiss", "1f48b"),
)