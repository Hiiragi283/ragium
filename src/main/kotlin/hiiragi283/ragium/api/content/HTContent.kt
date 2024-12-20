package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.block.Block
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.function.Supplier

/**
 * [RegistryKey] and [RegistryEntry] Holder
 * @see [HTContent.Material]
 * @see [HTContent.Tier]
 */
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
        fun ofFluid(id: Identifier): HTFluidContent = object : HTFluidContent {
            override val delegated: HTFluidContent = this

            override val key: RegistryKey<Fluid> = RegistryKey.of(RegistryKeys.FLUID, id)

            override fun get(): Fluid = Registries.FLUID.get(key) ?: error("Unregistered value: $key")
        }

        @JvmStatic
        fun ofFluid(path: String): HTFluidContent = ofFluid(RagiumAPI.id(path))

        @JvmStatic
        fun ofItem(id: Identifier): HTItemContent = object : HTItemContent {
            override val delegated: HTItemContent = this

            override val key: RegistryKey<Item> = RegistryKey.of(RegistryKeys.ITEM, id)

            override fun get(): Item = Registries.ITEM.get(key) ?: error("Unregistered value: $key")
        }

        @JvmStatic
        fun ofItem(path: String): HTItemContent = ofItem(RagiumAPI.id(path))
    }

    val key: RegistryKey<T>

    val id: Identifier get() = key.value

    //    Delegated    //

    /**
     * Delegated by other [HTContent]
     */
    interface Delegated<T : Any> : HTContent<T> {
        val delegated: HTContent<T>

        override val key: RegistryKey<T>
            get() = delegated.key

        override fun get(): T = delegated.get()
    }

    //    Material    //

    /**
     * a [HTContent] holding [HTMaterialKey] and [HTTagPrefix], and implementing [ItemConvertible]
     */
    interface Material<T : ItemConvertible> :
        Delegated<T>,
        ItemConvertible {
        companion object {
            @JvmStatic
            fun ofWrapped(prefix: HTTagPrefix, key: HTMaterialKey, item: ItemConvertible): Material<Item> = object : Material<Item> {
                override val material: HTMaterialKey = key
                override val tagPrefix: HTTagPrefix = prefix
                override val delegated: HTContent<Item> = object : HTContent<Item> {
                    override val key: RegistryKey<Item> by lazy { Registries.ITEM.getKey(item.asItem()).orElseThrow() }

                    override fun get(): Item = item.asItem()
                }
            }
        }

        val material: HTMaterialKey
        val tagPrefix: HTTagPrefix

        val definition: HTMaterialDefinition
            get() = HTMaterialDefinition(tagPrefix, material)

        val prefixedTagKey: TagKey<Item>
            get() = tagPrefix.createTag(material)

        override fun asItem(): Item = get().asItem()
    }

    //    Tier    //

    /**
     * a [HTContent] holding [HTMachineTier], and implementing [ItemConvertible]
     */
    interface Tier<T : ItemConvertible> :
        Delegated<T>,
        ItemConvertible {
        val tier: HTMachineTier

        override fun asItem(): Item = get().asItem()
    }
}
