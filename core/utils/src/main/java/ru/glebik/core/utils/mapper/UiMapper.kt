package ru.glebik.core.utils.mapper

interface UiMapper<Domain, Ui> {
    fun toDomain(ui: Ui): Domain
    fun toUi(domain: Domain): Ui
}