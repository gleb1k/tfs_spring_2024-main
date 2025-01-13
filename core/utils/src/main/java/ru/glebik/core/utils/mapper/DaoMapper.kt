package ru.glebik.core.utils.mapper

interface DaoMapper<Entity, Domain> {
    fun toEntity(domain: Domain): Entity
    fun toDomain(entity: Entity): Domain
}