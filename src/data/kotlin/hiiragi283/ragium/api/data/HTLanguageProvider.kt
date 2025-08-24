package hiiragi283.ragium.api.data

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.descKey
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.titleKey
import hiiragi283.ragium.api.extension.toDescriptionKey
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.integration.delight.HTKnifeToolVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTDeviceVariant
import hiiragi283.ragium.util.variant.HTDrumVariant
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import hiiragi283.ragium.util.variant.HTMachineVariant
import mekanism.common.registration.impl.DeferredChemical
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
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier
import kotlin.enums.enumEntries

abstract class HTLanguageProvider(output: PackOutput, val type: HTLanguageType) :
    LanguageProvider(output, RagiumAPI.MOD_ID, type.name.lowercase()) {
    //    Extension    //

    fun addPatterned() {
        addBlocks(RagiumBlocks.ORES)
        addBlocks(RagiumBlocks.MATERIALS)

        RagiumItems.MATERIALS.forEach { (variant: HTMaterialVariant, material: HTMaterialType, item: DeferredItem<*>) ->
            addItem(item, material.translate(type, variant))
        }

        addItems(RagiumItems.ARMORS)
        addItems(RagiumItems.TOOLS)

        addVariants<HTGeneratorVariant>()
        addVariants<HTMachineVariant>()
        addVariants<HTDeviceVariant>()
        addVariants<HTDrumVariant>()

        for ((material: RagiumMaterialType, knife) in RagiumDelightAddon.KNIFE_MAP) {
            addItem(knife, HTKnifeToolVariant.translate(type, material.getTranslatedName(type)))
        }
    }

    private fun addBlocks(table: HTTable<HTMaterialVariant.BlockTag, HTMaterialType, out Supplier<out Block>>) {
        table.forEach { (variant: HTMaterialVariant.BlockTag, material: HTMaterialType, block: Supplier<out Block>) ->
            addBlock(block, material.translate(type, variant))
        }
    }

    private fun addItems(table: HTTable<out HTVariantKey, HTMaterialType, out Supplier<out Item>>) {
        table.forEach { (variant: HTVariantKey, material: HTMaterialType, item: Supplier<out Item>) ->
            addItem(item, variant.translate(type, material.getTranslatedName(type)))
        }
    }

    private inline fun <reified V> addVariants() where V : HTVariantKey.WithBE<*>, V : Enum<V> {
        for (variant: V in enumEntries<V>()) {
            addBlock(variant.blockHolder, variant.translate(type, ""))
        }
    }

    fun addAdvancement(key: ResourceKey<Advancement>, title: String, desc: String) {
        add(key.titleKey(), title)
        add(key.descKey(), desc)
    }

    fun addEnchantment(key: ResourceKey<Enchantment>, value: String, desc: String) {
        add(key.toDescriptionKey("enchantment"), value)
        add(key.toDescriptionKey("enchantment", "desc"), desc)
    }

    fun addFluid(content: HTFluidContent<*, *, *>, value: String) {
        add(content.getType().descriptionId, value)
        addFluidBucket(content, value)
        add(content.commonTag, value)
    }

    protected abstract fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String)

    fun addInfo(item: ItemLike, vararg values: String) {
        add(RagiumTranslationKeys.getTooltipKey(ItemStack(item)), values.joinToString(separator = "\n"))
    }

    fun addItemGroup(group: DeferredHolder<CreativeModeTab, CreativeModeTab>, value: String) {
        add(group.id.toDescriptionKey("itemGroup"), value)
    }

    fun addMatterType(type: IMatterType, value: String) {
        add("${RagiumConst.REPLICATION}.matter_type.${type.name}", value)
    }

    fun addChemical(chemical: DeferredChemical<*>, value: String) {
        add(chemical.translationKey, value)
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
