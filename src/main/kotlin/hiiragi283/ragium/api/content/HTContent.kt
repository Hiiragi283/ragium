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
        fun <T : Any> of(key: RegistryKey<T>, entryGetter: (RegistryKey<T>) -> T?): HTContent<T> = object : HTContent<T> {
            override val key: RegistryKey<T> = key

            override fun get(): T = entryGetter(key) ?: error("Unregistered value: $key")
        }

        @JvmStatic
        fun ofBlock(id: Identifier): HTContent<Block> = of(RegistryKey.of(RegistryKeys.BLOCK, id), Registries.BLOCK::get)

        @JvmStatic
        fun ofBlock(path: String): HTContent<Block> = ofBlock(RagiumAPI.id(path))

        @JvmStatic
        fun ofFluid(id: Identifier): HTContent<Fluid> = of(RegistryKey.of(RegistryKeys.FLUID, id), Registries.FLUID::get)

        @JvmStatic
        fun ofFluid(path: String): HTContent<Fluid> = ofFluid(RagiumAPI.id(path))

        @JvmStatic
        fun ofItem(id: Identifier): HTContent<Item> = of(RegistryKey.of(RegistryKeys.ITEM, id), Registries.ITEM::get)

        @JvmStatic
        fun ofItem(path: String): HTContent<Item> = ofItem(RagiumAPI.id(path))
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
