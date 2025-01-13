package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getKeyOrThrow
import net.minecraft.core.registries.BuiltInRegistries
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
        /**
         * 新しく登録するブロック向け
         */
        @JvmStatic
        fun ofBlock(id: ResourceLocation): HTBlockContent = object : HTBlockContent {
            override val holder: DeferredHolder<Block, Block> = DeferredHolder.create(Registries.BLOCK, id)
        }

        @JvmStatic
        fun ofBlock(path: String): HTBlockContent = ofBlock(RagiumAPI.id(path))

        /**
         * バニラや他modのブロック向け
         */
        @JvmStatic
        fun fromBlock(block: Supplier<Block>): HTBlockContent = object : HTBlockContent {
            override val key: ResourceKey<Block> by lazy { BuiltInRegistries.BLOCK.getKeyOrThrow(get()) }

            override val holder: DeferredHolder<Block, Block> by lazy { DeferredHolder.create(key) }

            override fun get(): Block = block.get()
        }

        /**
         * バニラや他modのブロック向け
         */
        @JvmStatic
        fun fromFluid(fluid: Supplier<Fluid>): HTFluidContent = object : HTFluidContent {
            override val key: ResourceKey<Fluid> by lazy { BuiltInRegistries.FLUID.getKeyOrThrow(get()) }

            override val holder: DeferredHolder<Fluid, Fluid> by lazy { DeferredHolder.create(key) }

            override fun get(): Fluid = fluid.get()
        }

        /**
         * 新しく登録するアイテム向け
         */
        @JvmStatic
        fun ofItem(id: ResourceLocation): HTItemContent = object : HTItemContent {
            override val holder: DeferredHolder<Item, Item> = DeferredHolder.create(Registries.ITEM, id)

            override val key: ResourceKey<Item> = ResourceKey.create(Registries.ITEM, id)
        }

        @JvmStatic
        fun ofItem(path: String): HTItemContent = ofItem(RagiumAPI.id(path))

        /**
         * バニラや他modのアイテム向け
         */
        @JvmStatic
        fun fromItem(item: Supplier<Item>): HTItemContent = object : HTItemContent {
            override val key: ResourceKey<Item> by lazy { BuiltInRegistries.ITEM.getKeyOrThrow(get()) }

            override val holder: DeferredHolder<Item, Item> by lazy { DeferredHolder.create(key) }

            override fun get(): Item = item.get()
        }

        @JvmStatic
        fun blockHolder(path: String): DeferredHolder<Block, Block> = DeferredHolder.create(Registries.BLOCK, RagiumAPI.id(path))

        @JvmStatic
        fun fluidHolder(path: String): DeferredHolder<Fluid, Fluid> = DeferredHolder.create(Registries.FLUID, RagiumAPI.id(path))

        @JvmStatic
        fun itemHolder(path: String): DeferredHolder<Item, Item> = DeferredHolder.create(Registries.ITEM, RagiumAPI.id(path))
    }

    val holder: DeferredHolder<T, T>

    val key: ResourceKey<T> get() = holder.key!!
    val id: ResourceLocation get() = holder.id

    override fun get(): T = holder.get()

    fun register(register: DeferredRegister<T>, function: Function<ResourceLocation, out T>): DeferredHolder<T, T> =
        register.register(id.path, function)
}
