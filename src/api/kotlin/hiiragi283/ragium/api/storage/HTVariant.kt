package hiiragi283.ragium.api.storage

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.tags.TagKey
import java.util.function.Supplier

interface HTVariant<T : Any> {
    val holder: Holder.Reference<T>
    val components: DataComponentPatch

    val isEmpty: Boolean

    fun isOf(other: T): Boolean = holder.value() == other

    fun isOf(other: Supplier<out T>): Boolean = isOf(other.get())

    fun isOf(other: Holder<T>): Boolean = isOf(other.value())

    fun isIn(tagKey: TagKey<T>): Boolean = holder.`is`(tagKey)
}
