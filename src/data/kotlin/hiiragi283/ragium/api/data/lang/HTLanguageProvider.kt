package hiiragi283.ragium.api.data.lang

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.data.advancement.HTAdvancementKey
import hiiragi283.ragium.api.data.advancement.descKey
import hiiragi283.ragium.api.data.advancement.titleKey
import hiiragi283.ragium.api.function.identity
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredMatterType
import hiiragi283.ragium.api.registry.toDescriptionKey
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.text.HTSmithingTranslation
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumChemicals
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumIntegrationItems
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMatterTypes
import mekanism.api.text.IHasTranslationKey
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.data.LanguageProvider
import snownee.jade.api.IJadeProvider

abstract class HTLanguageProvider(output: PackOutput, val type: HTLanguageType) :
    LanguageProvider(output, RagiumAPI.MOD_ID, type.name.lowercase()) {
    //    Extension    //

    @JvmRecord
    private data class LangPattern(private val enPattern: String, private val jaPattern: String) : HTLangPatternProvider {
        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        }.replace("%s", value)
    }

    fun addPatterned() {
        fromVariantTable(RagiumBlocks.ORES, HTMaterialTranslations::getLangName)
        fromMaterialTable(RagiumBlocks.MATERIALS)
        fromVariantTable(RagiumBlocks.GLASSES, HTMaterialTranslations::getLangName)
        fromMaterialMap(LangPattern("%s Bars", "%sの格子"), RagiumBlocks.METAL_BARS)
        fromMaterialMap(LangPattern("%s Coil Block", "%sコイルブロック"), RagiumBlocks.COILS)
        fromMaterialMap(LangPattern("%s LED Block", "%sのLEDブロック"), RagiumBlocks.LED_BLOCKS)
        fromLangMap(LangPattern("%s Slab", "%sのハーフブロック"), RagiumBlocks.SLABS)
        fromLangMap(LangPattern("%s Stairs", "%sの階段"), RagiumBlocks.STAIRS)
        fromLangMap(LangPattern("%s Wall", "%sの壁"), RagiumBlocks.WALLS)

        fromMaterialTable(RagiumItems.MATERIALS)
        fromMaterialMap(LangPattern("%s Coil", "%sコイル"), RagiumItems.COILS)
        fromMaterialMap(LangPattern("%s Component", "%s構造体"), RagiumItems.COMPONENTS)

        fromVariantTable(RagiumItems.ARMORS, HTMaterialTranslations::getLangName)
        fromVariantTable(RagiumItems.TOOLS, HTMaterialTranslations::getLangName)

        val charge = LangPattern("%s Charge", "%s チャージ")
        fromLangMap(charge, RagiumItems.CHARGES)
        fromLangMap(charge, RagiumEntityTypes.CHARGES)

        fromMaterialMap(LangPattern("%s Upgrade", "%s強化"), RagiumItems.SMITHING_TEMPLATES)
        addTemplate(RagiumMaterialKeys.AZURE_STEEL, VanillaMaterialKeys.IRON)
        addTemplate(RagiumMaterialKeys.DEEP_STEEL, VanillaMaterialKeys.DIAMOND)
        addTemplate(RagiumMaterialKeys.NIGHT_METAL, VanillaMaterialKeys.GOLD)

        fromLangMap(LangPattern("%s Mold", "%sの鋳型"), RagiumItems.MOLDS)

        fromVariantTable(RagiumItems.MACHINE_UPGRADES, identity())
        // Translation
        addTranslations(HTBaseTier.entries, identity())

        addTranslations(HTCrateTier.entries, HTCrateTier::getBlock)
        addTranslations(HTDrumTier.entries, HTDrumTier::getBlock)

        val minecart = LangPattern("Minecart with %s", "%s付きトロッコ")
        fromLangMap(minecart, RagiumItems.DRUM_MINECARTS)
        fromLangMap(minecart, RagiumEntityTypes.DRUMS)

        translations()

        // Integration
        fromMaterialTable(RagiumIntegrationItems.MATERIALS)
        fromVariantTable(RagiumIntegrationItems.TOOLS, HTMaterialTranslations::getLangName)

        // Mekanism
        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            add(RagiumChemicals.getChemical(essenceType), essenceType.getTranslatedName(type))
        }

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val value: String = data.getTranslatedName(type)
            addFluid(data.molten, value)
            add(RagiumChemicals.getChemical(data), value)
        }
        // Replication
        for ((essence: RagiumEssenceType, matterType: HTDeferredMatterType<IMatterType>) in RagiumMatterTypes.MATTER_TYPES) {
            add("${RagiumConst.REPLICATION}.matter_type.${matterType.name}", essence.getTranslatedName(type))
        }
    }

    private fun translations() {
        // API - Constants
        add(RagiumTranslation.RAGIUM, "Ragium")

        add(RagiumTranslation.TRUE, "True")
        add(RagiumTranslation.FALSE, "False")

        add(RagiumTranslation.EMPTY_ENTRY, "Not Yet Translated")
        // API - GUI
        add(RagiumTranslation.CAPACITY, $$"Capacity: %1$s")
        add(RagiumTranslation.CAPACITY_MB, $$"Capacity: %1$s mB")
        add(RagiumTranslation.CAPACITY_FE, $$"Capacity: %1$s FE")

        add(RagiumTranslation.STORED, $$"%1$s: %2$s")
        add(RagiumTranslation.STORED_MB, $$"%1$s: %2$s mB")
        add(RagiumTranslation.STORED_FE, $$"%1$s FE")

        add(RagiumTranslation.FRACTION, $$"%1$s / %2$s")
        add(RagiumTranslation.PERCENTAGE, $$"%1$s %")
        add(RagiumTranslation.TICK, $$"%1$s ticks")

        add(RagiumTranslation.PER_MB, $$"%1$s / mb")
        add(RagiumTranslation.PER_TICK, $$"%1$s / ticks")
    }

    // Collection
    private fun <T : HTLangName> addTranslations(entries: Iterable<T>, blockGetter: (T) -> HTHasTranslationKey) {
        for (entry: T in entries) {
            add(blockGetter(entry), entry.getTranslatedName(type))
        }
    }

    private fun fromLangMap(provider: HTLangPatternProvider, map: Map<out HTLangName, HTHasTranslationKey>) {
        for ((langName: HTLangName, translationKey: HTHasTranslationKey) in map) {
            add(translationKey, provider.translate(type, langName))
        }
    }

    private fun fromMaterialMap(provider: HTLangPatternProvider, map: Map<out HTMaterialLike, HTHasTranslationKey>) {
        for ((material: HTMaterialLike, translationKey: HTHasTranslationKey) in map) {
            val langName: HTLangName = HTMaterialTranslations.getLangName(material) ?: return
            add(translationKey, provider.translate(type, langName))
        }
    }

    private fun <C : Any> fromVariantTable(
        table: ImmutableTable<out HTLangPatternProvider, C, out HTHasTranslationKey>,
        transform: (C) -> HTLangName?,
    ) {
        table.forEach { (provider: HTLangPatternProvider, column: C, translationKey: HTHasTranslationKey) ->
            val langName: HTLangName = transform(column) ?: return@forEach
            add(translationKey, provider.translate(type, langName))
        }
    }

    private fun fromMaterialTable(table: ImmutableTable<out HTPrefixLike, HTMaterialKey, out HTHasTranslationKey>) {
        table.forEach { (prefix: HTPrefixLike, key: HTMaterialKey, translationKey: HTHasTranslationKey) ->
            val translatedName: String = HTMaterialTranslations.translate(type, prefix, key) ?: return@forEach
            add(translationKey, translatedName)
        }
    }

    // HTHasTranslationKey
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

    // Registry
    fun addAdvancement(key: HTAdvancementKey, title: String, desc: String) {
        add(key.titleKey, title)
        add(key.descKey, desc)
    }

    fun addEnchantment(key: ResourceKey<Enchantment>, value: String, desc: String) {
        add(key.toDescriptionKey("enchantment"), value)
        add(key.toDescriptionKey("enchantment", "desc"), desc)
    }

    fun addFluid(content: HTFluidContent<*, *, *, *, *>, value: String) {
        add(content.type, value)
        addFluidBucket(content, value)
        add(content.commonTag, value)
    }

    protected abstract fun addFluidBucket(content: HTFluidContent<*, *, *, *, *>, value: String)

    fun addTemplate(material: HTMaterialLike, before: HTMaterialLike) {
        val translation = HTSmithingTranslation(RagiumAPI.MOD_ID, material)
        val langName: HTLangName = HTMaterialTranslations.getLangName(material) ?: return
        val beforeName: HTLangName = HTMaterialTranslations.getLangName(before) ?: return
        addTemplate(translation, langName.getTranslatedName(type), beforeName.getTranslatedName(type))
    }

    protected abstract fun addTemplate(translation: HTSmithingTranslation, material: String, before: String)

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
        final override fun addFluidBucket(content: HTFluidContent<*, *, *, *, *>, value: String) {
            add(content.bucket, "$value Bucket")
        }

        final override fun addTemplate(translation: HTSmithingTranslation, material: String, before: String) {
            add(translation.appliesTo, "$material Equipment")
            add(translation.ingredients, "$material Ingot")
            add(translation.upgradeDescription, "$material Upgrade")
            add(translation.baseSlotDescription, "Add ${before.lowercase()} armor, weapon, ot tool")
            add(translation.additionsSlotDescription, "Add $material Ingot")
        }
    }

    //    Japanese    //

    abstract class Japanese(output: PackOutput) : HTLanguageProvider(output, HTLanguageType.JA_JP) {
        final override fun addFluidBucket(content: HTFluidContent<*, *, *, *, *>, value: String) {
            add(content.bucket, "${value}入りバケツ")
        }

        final override fun addTemplate(translation: HTSmithingTranslation, material: String, before: String) {
            add(translation.appliesTo, "${material}の装備品")
            add(translation.ingredients, "${material}インゴット")
            add(translation.upgradeDescription, "${material}強化")
            add(translation.baseSlotDescription, "${before}製の防具，武器，道具を置いてください")
            add(translation.additionsSlotDescription, "${material}インゴットを置いてください")
        }
    }
}
