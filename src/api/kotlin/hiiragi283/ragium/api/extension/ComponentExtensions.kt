package hiiragi283.ragium.api.extension

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.common.MutableDataComponentHolder
import java.util.function.Supplier

//    DataComponentHolder    //

fun <T : Any> MutableDataComponentHolder.compute(type: DataComponentType<T>, updater: (T?) -> T?): T? = set(type, updater(get(type)))

fun <T : Any, U : Any> MutableDataComponentHolder.compute(type: DataComponentType<T>, context: U, updater: (T?, U) -> T?): T? =
    set(type, updater(get(type), context))

fun <T : Any> MutableDataComponentHolder.setOrRemove(type: DataComponentType<T>, value: T, validator: (T) -> Boolean): T? = when {
    validator(value) -> set(type, value)
    else -> remove(type)
}

// Supplier
fun <T : Any> MutableDataComponentHolder.compute(type: Supplier<out DataComponentType<T>>, updater: (T?) -> T?): T? =
    set(type, updater(get(type)))

fun <T : Any, U : Any> MutableDataComponentHolder.compute(
    type: Supplier<out DataComponentType<T>>,
    context: U,
    updater: (T?, U) -> T?,
): T? = set(type, updater(get(type), context))

fun <T : Any> MutableDataComponentHolder.setOrRemove(type: Supplier<out DataComponentType<T>>, value: T, filter: (T) -> Boolean): T? =
    when {
        filter(value) -> set(type, value)
        else -> remove(type)
    }

//    DataComponentPatch    //

inline fun buildComponentPatch(builderAction: DataComponentPatch.Builder.() -> Unit): DataComponentPatch =
    DataComponentPatch.builder().apply(builderAction).build()
