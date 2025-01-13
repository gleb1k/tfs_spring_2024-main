package ru.glebik.tinkoff_fintech.feature.channels.data.model

class SubscriptionsParams : ArrayList<SubscriptionsParamItem>() {
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
        fun getWithOneParam(name: String, description: String): SubscriptionsParams {
            val subscriptionsParams = SubscriptionsParams()
            subscriptionsParams.add(SubscriptionsParamItem(name, description))
            return subscriptionsParams
        }
    }
}

data class SubscriptionsParamItem(
    val name: String,
    val description: String = "",
) {
    override fun toString(): String {
        return "{\"name\": \"$name\", \"description\": \"$description\"}"
    }
}
