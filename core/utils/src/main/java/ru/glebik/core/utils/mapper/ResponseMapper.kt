package ru.glebik.core.utils.mapper

interface ResponseMapper<Response, Domain> {
    fun toDomain(response: Response): Domain
}
