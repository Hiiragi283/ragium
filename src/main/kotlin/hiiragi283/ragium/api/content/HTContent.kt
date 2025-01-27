package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.keyOrThrow
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

/**
 * [ResourceKey]と[T]クラスの値を持つインターフェース
 * @see HTBlockContent
 * @see HTItemContent
 */
sealed interface HTContent<T : ItemLike> :
    Supplier<T>,
    ItemLike {
    /**
     * [HTContent]の値を移譲する[DeferredHolder]
     */
    val holder: DeferredHolder<T, out T>

    val key: ResourceKey<T> get() = holder.keyOrThrow
    val id: ResourceLocation get() = holder.id

    override fun get(): T = holder.get()

    override fun asItem(): Item = get().asItem()
}
