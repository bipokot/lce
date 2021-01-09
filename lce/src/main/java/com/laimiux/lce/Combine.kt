package com.laimiux.lce

/**
 * Combines two [UCT] objects into one.
 */
fun <C, C2> UCT<C>.combine(other: UCT<C2>): UCT<Pair<C, C2>> {
    return combine(other) { first, second ->
        Pair(first, second)
    }
}

/**
 * Combines two [UCT] objects into one.
 */
inline fun <C, C2, T> UCT<C>.combine(
    other: UCT<C2>,
    crossinline combine: (C, C2) -> T
): UCT<T> {
    return flatMapContent { first ->
        other.flatMapContent { second ->
            UCT.content(combine(first, second))
        }
    }
}

/**
 * Combines two [UC] objects into one.
 */
fun <C, C2> UC<C>.combine(other: UC<C2>): UC<Pair<C, C2>> {
    return flatMapContent { first ->
        other.flatMapContent { second ->
            UC.content(Pair(first, second))
        }
    }
}


/**
 * Combines two [UC] objects into one.
 */
inline fun <C, C2, T> UC<C>.combine(
    other: UC<C2>,
    crossinline combine: (C, C2) -> T
): UC<T> {
    return flatMapContent { first ->
        other.flatMapContent { second ->
            UC.content(combine(first, second))
        }
    }
}



/**
 * Merging/combining is hard because?
 * - If first event is loading and second event is error, what do we show? error or loading?
 */

/**
 * It combins two [UCT] instances by:
 * Returning first error instance, otherwise
 * Returning first loading instance, otherwise
 * Combining the content
 */
inline fun <C, C2> UCT<C>.combineErrorsFirst(
    other: UCT<C2>,
): UCT<Pair<C, C2>> {

    // Merge priority:
    // Take first error
    // Take first loading
    // Merge content


    return takeFirstError(other) { first, second ->
        first.combine(second).asUCT()
    }
}





/**
 * Takes first error state out of [this] and [other], otherwise calls [onOther].
 */
inline fun <C, C2, T> UCT<C>.takeFirstError(
    other: UCT<C2>,
    crossinline onOther: (UC<C>, UC<C2>) -> UCT<T>
): UCT<T> {
    return takeErrorOrElse { uc ->
        other.takeErrorOrElse { otherUc ->
            onOther(uc, otherUc)
        }
    }
}

inline fun <L, C, E, T> LCE<L, C, E>.foldContentType(
    crossinline onContent: (Type.Content<C>) -> T,
    crossinline onOther: (LE<L, E>) -> T
): T {
    return foldTypes(
        onContent = onContent,
        onError = onOther,
        onLoading = onOther
    )
}

/**
 * It will return the current [UCT] if it is an error, otherwise it will
 * call [onOther] with a (Unit/Loading)[UC] type which you can map to another [UCT].
 */
inline fun <C, T> UCT<C>.takeErrorOrElse(
    crossinline onOther: (UC<C>) -> UCT<T>
): UCT<T> {
    return foldTypes(
        onError = { it },
        onContent = onOther,
        onLoading = onOther
    )
}
