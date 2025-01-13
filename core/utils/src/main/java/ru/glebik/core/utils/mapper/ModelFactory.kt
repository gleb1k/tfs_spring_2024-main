package ru.glebik.core.utils.mapper

interface ModelFactory<T1, T2, R> {
    operator fun invoke(model1: T1, model2: T2): R
}