package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

/**
 * [ResourceKey]と[T]クラスの値を持つインターフェース
 * @see HTBlockContent
 * @see HTFluidContent
 * @see HTItemContent
 */
interface HTContent<T : Any> : Supplier<T> {
    companion object {
        @JvmStatic
        fun blockHolder(path: String): DeferredHolder<Block, Block> = DeferredHolder.create(Registries.BLOCK, RagiumAPI.id(path))

        @JvmStatic
        fun fluidHolder(path: String): DeferredHolder<Fluid, Fluid> = DeferredHolder.create(Registries.FLUID, RagiumAPI.id(path))

        @JvmStatic
        fun itemHolder(path: String): DeferredHolder<Item, Item> = DeferredHolder.create(Registries.ITEM, RagiumAPI.id(path))
    }

    val holder: DeferredHolder<T, out T>

    val key: ResourceKey<T> get() = holder.key!!
    val id: ResourceLocation get() = holder.id

    override fun get(): T = holder.get()

    fun register(register: DeferredRegister<T>, function: Function<ResourceLocation, out T>): DeferredHolder<T, T> =
        register.register(id.path, function)
}
