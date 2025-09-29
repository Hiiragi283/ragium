package hiiragi283.ragium.api.data.lang

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.data.advancement.HTAdvancementKey
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.toColumnTableBy
import hiiragi283.ragium.api.extension.toDescriptionKey
import hiiragi283.ragium.api.extension.toRowTableBy
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTMoltenCrystalData
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.integration.delight.HTKnifeToolVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.text.IHasTranslationKey
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.LanguageProvider
import kotlin.enums.enumEntries

abstract class HTLanguageProvider(output: PackOutput, val type: HTLanguageType) :
    LanguageProvider(output, RagiumAPI.MOD_ID, type.name.lowercase()) {
    //    Extension    //

    fun addPatterned() {
        addBlocks(RagiumBlocks.ORES)
        addBlocks(RagiumBlocks.MATERIALS)
        addBlocks(RagiumBlocks.COILS.toRowTableBy(MiscVariants.COIL_BLOCK))

        addItems(RagiumItems.MATERIALS)
        addItems(RagiumItems.CIRCUITS.toRowTableBy(HTItemMaterialVariant.CIRCUIT))
        addItems(RagiumItems.COILS.toRowTableBy(MiscVariants.COIL))
        addItems(RagiumItems.COMPONENTS.toRowTableBy(MiscVariants.COMPONENT))

        addItems(RagiumItems.AZURE_ARMORS.toColumnTableBy(RagiumMaterialType.AZURE_STEEL))
        addItems(RagiumItems.DEEP_ARMORS.toColumnTableBy(RagiumMaterialType.DEEP_STEEL))
        addItems(RagiumItems.TOOLS)

        addVariants<HTGeneratorVariant>()
        addVariants<HTMachineVariant>()
        addVariants<HTDeviceVariant>()
        addVariants<HTDrumVariant>()

        // Delight
        for ((material: RagiumMaterialType, knife) in RagiumDelightAddon.KNIFE_MAP) {
            add(knife, HTKnifeToolVariant.translate(type, material.getTranslatedName(type)))
        }
        // Mekanism
        for (data: HTMoltenCrystalData in HTMoltenCrystalData.entries) {
            val value: String = data.getTranslatedName(type)
            addFluid(data.molten, value)
            add(RagiumMekanismAddon.getChemical(data.material), value)
        }

        addItems(RagiumMekanismAddon.MATERIAL_ITEMS)
    }

    private inline fun <reified V> addVariants() where V : HTVariantKey.WithBE<*>, V : Enum<V> {
        for (variant: V in enumEntries<V>()) {
            addBlock(variant.blockHolder, variant.translate(type, ""))
        }
    }

    private fun addBlocks(table: HTTable<out HTVariantKey, out HTMaterialType, out HTDeferredBlock<*, *>>) {
        table.forEach { (variant: HTVariantKey, material: HTMaterialType, block: HTDeferredBlock<*, *>) ->
            if (material is HTMaterialType.Translatable) {
                add(block, material.translate(type, variant))
            }
        }
    }

    private fun addItems(table: HTTable<out HTVariantKey, out HTMaterialType, out HTDeferredItem<*>>) {
        table.forEach { (variant: HTVariantKey, material: HTMaterialType, item: HTDeferredItem<*>) ->
            if (material is HTMaterialType.Translatable) {
                add(item, material.translate(type, variant))
            }
        }
    }

    fun add(translatable: HTHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    fun addAdvancement(key: HTAdvancementKey, title: String, desc: String) {
        add(key.titleKey, title)
        add(key.descKey, desc)
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
        add(
            RagiumTranslation.getTooltipKey(ItemStack(item)),
            values.joinToString(separator = "\n"),
        )
    }

    fun addItemGroup(group: HTHolderLike, value: String) {
        add(group.getId().toDescriptionKey("itemGroup"), value)
    }

    fun addMatterType(type: IMatterType, value: String) {
        add("${RagiumConst.REPLICATION}.matter_type.${type.name}", value)
    }

    // Mekanism
    fun add(translatable: IHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    //    English    //

    abstract class English(output: PackOutput) : HTLanguageProvider(output, HTLanguageType.EN_US) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.getBucket(), "$value Bucket")
        }
    }

    //    Japanese    //

    abstract class Japanese(output: PackOutput) : HTLanguageProvider(output, HTLanguageType.JA_JP) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *>, value: String) {
            add(content.getBucket(), "${value}入りバケツ")
        }
    }

    //    MiscVariants    //

    private enum class MiscVariants(private val enPattern: String, private val jaPattern: String) : HTMaterialVariant {
        // Block
        COIL_BLOCK("%s Coil Block", "%sコイルブロック"),

        // Item
        COIL("%s Coil", "%sコイル"),
        COMPONENT("%s Component", "%s構造体"),
        ;

        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        }.replace("%s", value)

        override fun getSerializedName(): String = name.lowercase()
    }
}
