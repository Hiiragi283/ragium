package hiiragi283.ragium.api.data.lang

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.data.advancement.HTAdvancementKey
import hiiragi283.ragium.api.extension.toDescriptionKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.integration.delight.HTKnifeToolVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.integration.replication.HTDeferredMatterType
import hiiragi283.ragium.integration.replication.RagiumReplicationAddon
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
        fromTable(RagiumBlocks.ORES)
        fromTable(RagiumBlocks.MATERIALS)
        fromMapWithRow(MiscVariants.COIL_BLOCK, RagiumBlocks.COILS)
        fromMapWithRow(MiscVariants.LED_BLOCK, RagiumBlocks.LED_BLOCKS)
        fromMapWithColumn(MiscMaterials.SLAB, RagiumBlocks.SLABS)
        fromMapWithColumn(MiscMaterials.STAIRS, RagiumBlocks.STAIRS)
        fromMapWithColumn(MiscMaterials.WALL, RagiumBlocks.WALLS)

        fromTable(RagiumItems.MATERIALS)
        fromMapWithRow(HTItemMaterialVariant.CIRCUIT, RagiumItems.CIRCUITS)
        fromMapWithRow(MiscVariants.COIL, RagiumItems.COILS)
        fromMapWithRow(MiscVariants.COMPONENT, RagiumItems.COMPONENTS)

        fromMapWithColumn(RagiumMaterialType.AZURE_STEEL, RagiumItems.AZURE_ARMORS)
        fromMapWithColumn(RagiumMaterialType.DEEP_STEEL, RagiumItems.DEEP_ARMORS)
        fromTable(RagiumItems.TOOLS)

        addVariants<HTGeneratorVariant>()
        addVariants<HTMachineVariant>()
        addVariants<HTDeviceVariant>()
        addVariants<HTDrumVariant>()

        // Delight
        fromMapWithRow(HTKnifeToolVariant, RagiumDelightAddon.KNIFE_MAP)
        // Mekanism
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            add(RagiumMekanismAddon.getChemical(essenceType), essenceType.getTranslatedName(type))
        }

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val value: String = data.getTranslatedName(type)
            addFluid(data.molten, value)
            add(RagiumMekanismAddon.getChemical(data.material), value)
        }

        fromTable(RagiumMekanismAddon.MATERIAL_ITEMS)
        // Replication
        for ((essence: RagiumEssenceType, matterType: HTDeferredMatterType<IMatterType>) in RagiumReplicationAddon.MATTER_MAP) {
            add("${RagiumConst.REPLICATION}.matter_type.${matterType.name}", essence.getTranslatedName(type))
        }
    }

    private inline fun <reified V> addVariants() where V : HTVariantKey.WithBE<*>, V : Enum<V> {
        for (variant: V in enumEntries<V>()) {
            add(variant.blockHolder, variant.translate(type, ""))
        }
    }

    private fun fromMapWithRow(variant: HTVariantKey, map: Map<out HTMaterialType, HTHasTranslationKey>) {
        map.entries.map { (material: HTMaterialType, key: HTHasTranslationKey) -> Triple(variant, material, key) }.let(::fromTriples)
    }

    private fun fromMapWithColumn(material: HTMaterialType, map: Map<out HTVariantKey, HTHasTranslationKey>) {
        map.entries.map { (variant: HTVariantKey, key: HTHasTranslationKey) -> Triple(variant, material, key) }.let(::fromTriples)
    }

    private fun fromTable(table: HTTable<out HTVariantKey, out HTMaterialType, out HTHasTranslationKey>) {
        fromTriples(table.entries)
    }

    private fun fromTriples(triples: Iterable<Triple<HTVariantKey, HTMaterialType, HTHasTranslationKey>>) {
        triples.forEach { (variant: HTVariantKey, material: HTMaterialType, key: HTHasTranslationKey) ->
            if (material is HTMaterialType.Translatable) {
                add(key, material.translate(type, variant))
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

    //    Misc    //

    private enum class MiscVariants(private val enPattern: String, private val jaPattern: String) : HTMaterialVariant {
        // Block
        COIL_BLOCK("%s Coil Block", "%sコイルブロック"),
        LED_BLOCK("%s LED Block", "%sのLEDブロック"),

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

    private enum class MiscMaterials(private val enName: String, private val jpName: String) : HTMaterialType.Translatable {
        SLAB("Slab", "ハーフブロック"),
        STAIRS("Stairs", "階段"),
        WALL("Wall", "壁"),
        ;

        override fun getTranslatedName(type: HTLanguageType): String = when (type) {
            HTLanguageType.EN_US -> enName
            HTLanguageType.JA_JP -> jpName
        }

        override fun materialName(): String = name.lowercase()
    }
}
