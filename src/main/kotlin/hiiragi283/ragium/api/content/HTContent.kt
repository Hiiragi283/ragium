package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getKeyOrThrow
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.Supplier

interface HTContent<T : Any> : Supplier<T> {
    companion object {
        @JvmStatic
        fun ofBlock(id: Identifier): HTBlockContent = object : HTBlockContent {
            override val key: RegistryKey<Block> = RegistryKey.of(RegistryKeys.BLOCK, id)
        }

        @JvmStatic
        fun ofBlock(path: String): HTBlockContent = ofBlock(RagiumAPI.id(path))

        @JvmStatic
        fun fromBlock(block: Block): HTBlockContent = object : HTBlockContent {
            override val key: RegistryKey<Block> by lazy { Registries.BLOCK.getKeyOrThrow(block) }

            override fun get(): Block = block
        }

        @JvmStatic
        fun fromFluid(fluid: Fluid): HTFluidContent = object : HTFluidContent {
            override val key: RegistryKey<Fluid> by lazy { Registries.FLUID.getKeyOrThrow(fluid) }

            override fun get(): Fluid = fluid
        }

        @JvmStatic
        fun ofItem(id: Identifier): HTItemContent = object : HTItemContent {
            override val key: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, id)
        }

        @JvmStatic
        fun ofItem(path: String): HTItemContent = ofItem(RagiumAPI.id(path))

        @JvmStatic
        fun fromItem(item: Item): HTItemContent = object : HTItemContent {
            override val key: RegistryKey<Item> by lazy { Registries.ITEM.getKeyOrThrow(item) }

            override fun get(): Item = item
        }

        @JvmStatic
        fun blockKey(path: String): RegistryKey<Block> = RegistryKey.of(RegistryKeys.BLOCK, RagiumAPI.id(path))

        @JvmStatic
        fun fluidKey(path: String): RegistryKey<Fluid> = RegistryKey.of(RegistryKeys.FLUID, RagiumAPI.id(path))

        @JvmStatic
        fun itemKey(path: String): RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, RagiumAPI.id(path))
    }

    val key: RegistryKey<T>

    val id: Identifier get() = key.value
}
