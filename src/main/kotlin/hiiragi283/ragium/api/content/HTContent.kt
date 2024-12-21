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
            override val delegated: HTContent<Block> = this

            override val key: RegistryKey<Block> = RegistryKey.of(RegistryKeys.BLOCK, id)

            override fun get(): Block = Registries.BLOCK.get(key) ?: error("Unregistered value: $key")
        }

        @JvmStatic
        fun ofBlock(path: String): HTBlockContent = ofBlock(RagiumAPI.id(path))

        @JvmStatic
        fun fromBlock(block: Block): HTBlockContent = object : HTBlockContent {
            override val delegated: HTContent<Block> = this

            override val key: RegistryKey<Block> by lazy { Registries.BLOCK.getKeyOrThrow(block) }

            override fun get(): Block = block
        }

        @JvmStatic
        fun ofFluid(id: Identifier): HTFluidContent = object : HTFluidContent {
            override val delegated: HTFluidContent = this

            override val key: RegistryKey<Fluid> = RegistryKey.of(RegistryKeys.FLUID, id)

            override fun get(): Fluid = Registries.FLUID.get(key) ?: error("Unregistered value: $key")
        }

        @JvmStatic
        fun ofFluid(path: String): HTFluidContent = ofFluid(RagiumAPI.id(path))

        @JvmStatic
        fun fromFluid(fluid: Fluid): HTFluidContent = object : HTFluidContent {
            override val delegated: HTContent<Fluid> = this

            override val key: RegistryKey<Fluid> by lazy { Registries.FLUID.getKeyOrThrow(fluid) }

            override fun get(): Fluid = fluid
        }

        @JvmStatic
        fun ofItem(id: Identifier): HTItemContent = object : HTItemContent {
            override val delegated: HTItemContent = this

            override val key: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, id)

            override fun get(): Item = Registries.ITEM.get(key) ?: error("Unregistered value: $key")
        }

        @JvmStatic
        fun ofItem(path: String): HTItemContent = ofItem(RagiumAPI.id(path))

        @JvmStatic
        fun fromItem(item: Item): HTItemContent = object : HTItemContent {
            override val delegated: HTContent<Item> = this

            override val key: RegistryKey<Item> by lazy { Registries.ITEM.getKeyOrThrow(item) }

            override fun get(): Item = item
        }
    }

    val key: RegistryKey<T>

    val id: Identifier get() = key.value

    //    Delegated    //

    interface Delegated<T : Any> : HTContent<T> {
        val delegated: HTContent<T>

        override val key: RegistryKey<T>
            get() = delegated.key

        override fun get(): T = delegated.get()
    }
}
