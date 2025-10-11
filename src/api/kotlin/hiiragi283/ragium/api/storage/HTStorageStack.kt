package hiiragi283.ragium.api.storage

import com.google.common.base.Predicates
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import java.util.function.Predicate

interface HTStorageStack<T : Any> :
    DataComponentHolder,
    HTHolderLike {
    companion object {
        @JvmStatic
        fun <STACK : HTStorageStack<*>> alwaysTrue(): Predicate<STACK> = Predicates.alwaysTrue<STACK>()
    }

    fun isEmpty(): Boolean

    fun value(): T

    fun holder(): Holder<T>

    fun amountAsLong(): Long = amountAsInt().toLong()

    fun amountAsInt(): Int

    fun copy(): HTStorageStack<T>

    fun copyWithAmount(amount: Int): HTStorageStack<T>

    fun componentsPatch(): DataComponentPatch

    fun hoverName(): Component

    override fun getId(): ResourceLocation = holder().idOrThrow
}
