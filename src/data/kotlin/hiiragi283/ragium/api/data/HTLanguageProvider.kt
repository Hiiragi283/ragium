package hiiragi283.ragium.api.data

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.descKey
import hiiragi283.ragium.api.extension.titleKey
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.HTMaterialType
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.Util
import net.minecraft.advancements.Advancement
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredHolder
import kotlin.enums.enumEntries

abstract class HTLanguageProvider(output: PackOutput, val type: HTLanguageType) :
    LanguageProvider(output, RagiumAPI.MOD_ID, type.name.lowercase()) {
    //    Extension    //

    inline fun <reified B> addMaterialBlocks(pattern: String) where B : HTBlockHolderLike.Materialized, B : Enum<B> {
        val map: Map<Block, HTMaterialType> = enumEntries<B>().associate { typed: B -> typed.get() to typed.material }
        for ((block: Block, material: HTMaterialType) in map) {
            add(block, material.translate(type, pattern))
        }
    }

    inline fun <reified I> addMaterialItems(pattern: String) where I : HTItemHolderLike.Materialized, I : Enum<I> {
        val map: Map<Item, HTMaterialType> = enumEntries<I>().associate { typed: I -> typed.get() to typed.material }
        for ((item: Item, material: HTMaterialType) in map) {
            add(item, material.translate(type, pattern))
        }
    }

    inline fun <reified I> addItems(material: HTMaterialType) where I : HTItemHolderLike.Typed<out HTVariantKey>, I : Enum<I> {
        val map: Map<Item, HTVariantKey> = enumEntries<I>().associate { typed: I -> typed.get() to typed.variant }
        for ((item: Item, variant: HTVariantKey) in map) {
            add(item, variant.translate(type, material.translate(type)))
        }
    }

    inline fun <reified I> addItems(value: String) where I : HTItemHolderLike.Typed<out HTVariantKey>, I : Enum<I> {
        val map: Map<Item, HTVariantKey> = enumEntries<I>().associate { typed: I -> typed.get() to typed.variant }
        for ((item: Item, variant: HTVariantKey) in map) {
            add(item, variant.translate(type, value))
        }
    }

    fun addAdvancement(key: ResourceKey<Advancement>, title: String, desc: String) {
        add(key.titleKey(), title)
        add(key.descKey(), desc)
    }

    fun addEnchantment(key: ResourceKey<Enchantment>, value: String, desc: String) {
        val translationKey: String = Util.makeDescriptionId("enchantment", key.location())
        add(translationKey, value)
        add("$translationKey.desc", desc)
    }

    fun addFluid(content: HTFluidContent<*, *, *>, value: String) {
        add(content.getType().descriptionId, value)
        addFluidBucket(content, value)
    }

    protected abstract fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String)

    fun addInfo(item: ItemLike, vararg values: String) {
        add(RagiumTranslationKeys.getTooltipKey(ItemStack(item)), values.joinToString(separator = "\n"))
    }

    fun addItemGroup(group: DeferredHolder<CreativeModeTab, CreativeModeTab>, value: String) {
        add(Util.makeDescriptionId("itemGroup", group.id), value)
    }

    fun addMatterType(type: IMatterType, value: String) {
        add("${RagiumConst.REPLICATION}.matter_type.${type.name}", value)
    }

    //    English    //

    abstract class English(output: PackOutput) : HTLanguageProvider(output, HTLanguageType.EN_US) {
        override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.getBucket(), "$value Bucket")
        }
    }

    //    Japanese    //

    abstract class Japanese(output: PackOutput) : HTLanguageProvider(output, HTLanguageType.JA_JP) {
        override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.getBucket(), "${value}入りバケツ")
        }
    }
}
