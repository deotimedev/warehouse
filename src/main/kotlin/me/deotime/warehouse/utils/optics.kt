@file:Suppress("NOTHING_TO_INLINE")
package me.deotime.warehouse.utils

import co.q64.raindrop.optics.Getter
import co.q64.raindrop.optics.Setter


inline operator fun <T, R> T.get(getter: Getter<T, R>) = getter.view(this)
inline operator fun <T, R> T.set(setter: Setter.Simple<T, R>, value: R) = setter.set(this, value)
inline fun <T, R> T.update(setter: Setter.Simple<T, R>, crossinline closure: (R) -> R) = setter.over(this) { closure(it) }