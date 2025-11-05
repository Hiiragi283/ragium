package hiiragi283.ragium.api.data.lang

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.data.advancement.HTAdvancementKey
import hiiragi283.ragium.api.data.advancement.descKey
import hiiragi283.ragium.api.data.advancement.titleKey
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredMatterType
import hiiragi283.ragium.api.registry.toDescriptionKey
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.integration.RagiumMekanismAddon
import hiiragi283.ragium.common.integration.RagiumReplicationAddon
import hiiragi283.ragium.common.integration.food.RagiumDelightAddon
import hiiragi283.ragium.common.integration.food.RagiumKaleidoCookeryAddon
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.variant.HTKitchenKnifeToolVariant
import hiiragi283.ragium.common.variant.HTKnifeToolVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.text.IHasTranslationKey
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.LanguageProvider
import snownee.jade.api.IJadeProvider

abstract class HTLanguageProvider(output: PackOutput, val type: HTLanguageType) :
    LanguageProvider(output, RagiumAPI.MOD_ID, type.name.lowercase()) {
    //    Extension    //

    fun addPatterned() {
        fromVariantTable(RagiumBlocks.ORES)
        fromMaterialTable(RagiumBlocks.MATERIALS)
        fromMapWithRow(HTSimpleLangPattern("%s Coil Block", "%sコイルブロック"), RagiumBlocks.COILS)
        fromMapWithRow(HTSimpleLangPattern("%s LED Block", "%sのLEDブロック"), RagiumBlocks.LED_BLOCKS)
        fromMapWithColumn(HTSimpleLangName("Slab", "ハーフブロック"), RagiumBlocks.SLABS)
        fromMapWithColumn(HTSimpleLangName("Stairs", "階段"), RagiumBlocks.STAIRS)
        fromMapWithColumn(HTSimpleLangName("Wall", "壁"), RagiumBlocks.WALLS)

        fromMaterialTable(RagiumItems.MATERIALS)
        fromMapWithRow(HTSimpleLangPattern("%s Circuit", "%s回路"), RagiumItems.CIRCUITS)
        fromMapWithRow(HTSimpleLangPattern("%s Coil", "%sコイル"), RagiumItems.COILS)
        fromMapWithRow(HTSimpleLangPattern("%s Component", "%s構造体"), RagiumItems.COMPONENTS)

        fromMapWithColumn(RagiumMaterialKeys.AZURE_STEEL, RagiumItems.AZURE_ARMORS)
        fromMapWithColumn(RagiumMaterialKeys.DEEP_STEEL, RagiumItems.DEEP_ARMORS)
        fromVariantTable(RagiumItems.TOOLS)

        addTranslations(HTCrateTier.entries, HTCrateTier::getBlock)
        addTranslations(HTDrumTier.entries, HTDrumTier::getBlock)

        // Delight
        fromMapWithRow(HTKnifeToolVariant, RagiumDelightAddon.KNIFE_MAP)
        // Kaleido
        fromMapWithRow(HTKitchenKnifeToolVariant, RagiumKaleidoCookeryAddon.KNIFE_MAP)
        // Mekanism
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            add(RagiumMekanismAddon.getChemical(essenceType), essenceType.getTranslatedName(type))
        }

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val value: String = data.getTranslatedName(type)
            addFluid(data.molten, value)
            add(RagiumMekanismAddon.getChemical(data), value)
        }

        fromMaterialTable(RagiumMekanismAddon.MATERIAL_ITEMS)
        // Replication
        for ((essence: RagiumEssenceType, matterType: HTDeferredMatterType<IMatterType>) in RagiumReplicationAddon.MATTER_MAP) {
            add("${RagiumConst.REPLICATION}.matter_type.${matterType.name}", essence.getTranslatedName(type))
        }
    }

    private fun <T : HTLangPatternProvider> addTranslations(entries: Iterable<T>, blockGetter: (T) -> HTHasTranslationKey) {
        for (entry: T in entries) {
            add(blockGetter(entry), entry.translate(type, "%s"))
        }
    }

    private fun fromMapWithRow(provider: HTLangPatternProvider, map: Map<out HTMaterialLike, HTHasTranslationKey>) {
        for ((material: HTMaterialLike, translationKey: HTHasTranslationKey) in map) {
            val langName: HTLangName = HTMaterialTranslations.getLangName(material) ?: return
            add(translationKey, provider.translate(type, langName))
        }
    }

    private fun fromMapWithColumn(material: HTMaterialLike, map: Map<out HTVariantKey, HTHasTranslationKey>) {
        val langName: HTLangName = HTMaterialTranslations.getLangName(material) ?: return
        fromMapWithColumn(langName, map)
    }

    private fun fromMapWithColumn(translatedName: HTLangName, map: Map<out HTVariantKey, HTHasTranslationKey>) {
        for ((variant: HTVariantKey, translationKey: HTHasTranslationKey) in map) {
            add(translationKey, variant.translate(type, translatedName))
        }
    }

    private fun fromVariantTable(table: ImmutableTable<out HTVariantKey, HTMaterialKey, out HTHasTranslationKey>) {
        table.forEach { (variant: HTVariantKey, key: HTMaterialKey, translationKey: HTHasTranslationKey) ->
            val langName: HTLangName = HTMaterialTranslations.getLangName(key) ?: return@forEach
            add(translationKey, variant.translate(type, langName))
        }
    }

    private fun fromMaterialTable(table: ImmutableTable<HTMaterialPrefix, HTMaterialKey, out HTHasTranslationKey>) {
        table.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, translationKey: HTHasTranslationKey) ->
            val translatedName: String = HTMaterialTranslations.translate(type, prefix, key) ?: return@forEach
            add(translationKey, translatedName)
        }
    }

    fun add(translatable: HTHasTranslationKey, value: String) {
        add(translatable.translationKey, value)
    }

    fun add(translatable: HTDeferredBlock<*, *>, blockValue: String, itemValue: String = blockValue) {
        val blockKey: String = translatable.translationKey
        add(blockKey, blockValue)
        val itemKey: String = translatable.itemHolder.translationKey
        if (itemKey != blockKey) {
            add(itemKey, itemValue)
        }
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

    // Jade
    fun add(provider: IJadeProvider, value: String) {
        val id: ResourceLocation = provider.uid
        val key = "config.jade.plugin_${id.namespace}.${id.path}"
        add(key, value)
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
}
