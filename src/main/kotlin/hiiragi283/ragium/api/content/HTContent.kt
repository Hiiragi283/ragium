package hiiragi283.ragium.api.content

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

/**
 * [ResourceKey]と[T]クラスの値を持つインターフェース
 * @see HTBlockContent
 * @see HTItemContent
 */
interface HTContent<T : Any> : Supplier<T> {
    companion object {
        @JvmStatic
        fun <T : Any> wrapHolder(holder: DeferredHolder<T, out T>): HTContent<T> = object : HTContent<T> {
            override val holder: DeferredHolder<T, out T> = holder
        }
    }

    val holder: DeferredHolder<T, out T>

    val key: ResourceKey<T> get() = holder.key!!
    val id: ResourceLocation get() = holder.id

    override fun get(): T = holder.get()
}
