package com.laimiux.lce

/**
 * LE (Loading / Error) type is used to support operations such as [foldContentType].
 */
// TODO: should probably hide this type, maybe it's not even needed.
interface LE<out L, out E> {
    companion object {
//        fun loading(): LE<Unit, Nothing> = Type.Loading()
//        fun loading(unit: Unit): LE<Unit, Nothing> = Type.Loading(unit)
//        fun <T> loading(loading: T): LE<T, Nothing> = Type.Loading(loading)
    }

    fun isLoading(): Boolean
    fun isError(): Boolean


    fun errorOrNull(): E?
    fun loadingOrNull(): L?

    fun asLceType(): Type<L, Nothing, E>
}
