package ru.glebik.core.widget.view.recycler

interface DiffListItem {
    fun areItemsSame(other: DiffListItem): Boolean
    fun areContentsSame(other: DiffListItem): Boolean
}