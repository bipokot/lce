package com.laimiux.lce

/**
 * CE stands for Content / Error. A type that represents either content state of type [C] or
 * error state of type [E].
 */
interface CE<out C, out E> {
    companion object {
        fun <T> content(content: T): CE<T, Nothing> = Type.Content(content)

        fun error(error: Throwable): CE<Nothing, Throwable> = Type.Error(error)
        fun <T> error(error: T): CE<Nothing, T> = Type.Error(error)
    }

    fun isContent(): Boolean
    fun isError(): Boolean

    fun contentOrNull(): C?
    fun errorOrNull(): E?

    fun asLceType(): Type<Any?, C, E>
}
