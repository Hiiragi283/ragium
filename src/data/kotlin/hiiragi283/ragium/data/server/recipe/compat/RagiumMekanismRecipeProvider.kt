package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.integration.RagiumMekanismAddon
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.MekanismMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.IMekanismAccess
import mekanism.api.chemical.ChemicalStack
import mekanism.api.datagen.recipe.builder.ChemicalChemicalToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder
import mekanism.api.recipes.ingredients.ChemicalStackIngredient
import mekanism.api.recipes.ingredients.ItemStackIngredient
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import mekanism.common.registries.MekanismItems
import mekanism.common.tags.MekanismTags

object RagiumMekanismRecipeProvider : HTRecipeProvider.Integration(RagiumConst.MEKANISM) {
    override fun buildRecipeInternal() {
        chemicalConversion()

        raginite()
        azure()
        molten()
        deep()
    }

    @JvmStatic
    private fun chemicalConversion() {
        fun toChemical(factory: (ItemStackIngredient, ChemicalStack) -> ItemStackToChemicalRecipeBuilder, prefix: String) {
            for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
                val name: String = essenceType.asMaterialName()
                val tagPrefix: CommonMaterialPrefixes = essenceType.basePrefix
                // Base -> Chemical
                factory(
                    itemHelper.from(tagPrefix, essenceType.parent),
                    essenceType.asStack(10),
                ).build(output, id("$prefix/$name/from_${tagPrefix.asPrefixName()}"))
                // Enriched -> Chemical
                factory(
                    itemHelper.from(MekanismMaterialPrefixes.ENRICHED, essenceType),
                    essenceType.asStack(80),
                ).build(output, id("$prefix/$name/from_enriched"))
            }
        }

        toChemical(ItemStackToChemicalRecipeBuilder::chemicalConversion, "chemical_conversion")
        toChemical(ItemStackToChemicalRecipeBuilder::oxidizing, "oxidizing")

        for (essenceType: RagiumEssenceType in RagiumEssenceType.entries) {
            // Base -> Enriched
            ItemStackToItemStackRecipeBuilder
                .enriching(
                    itemHelper.from(essenceType.basePrefix, essenceType.parent),
                    RagiumMekanismAddon.getEnrichedStack(essenceType),
                ).build(output, id("enriching/enrich/${essenceType.asMaterialName()}"))
        }
    }

    @JvmStatic
    private fun raginite() {
        // Ore -> Dust
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(CommonMaterialPrefixes.ORE, RagiumMaterialKeys.RAGINITE),
                RagiumItems.getDust(RagiumMaterialKeys.RAGINITE).toStack(12),
            ).build(output, id("processing/raginite/from_ore"))

        // Raginite + Copper -> Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(VanillaMaterialKeys.COPPER, 1),
                chemicalHelper.from(RagiumEssenceType.RAGIUM, 40),
                RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_alloy"))
        // Raginite + Gold -> Advanced Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(VanillaMaterialKeys.GOLD, 1),
                chemicalHelper.from(RagiumEssenceType.RAGIUM, 80),
                RagiumItems.getIngot(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/advanced_ragi_alloy"))

        // Raginite + Diamond -> Ragi-Crystal
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.gemOrDust(VanillaMaterialKeys.DIAMOND, 1),
                chemicalHelper.from(RagiumEssenceType.RAGIUM, 80),
                RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_crystal"))
        // Ore -> Crystal
        oreToGem(RagiumMaterialKeys.RAGI_CRYSTAL)

        // Raginite + Apple -> Ragi-Cherry
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(RagiumCommonTags.Items.FOODS_APPLE),
                chemicalHelper.from(RagiumEssenceType.RAGIUM, 40),
                RagiumItems.RAGI_CHERRY.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/${RagiumConst.RAGI_CHERRY}"))
    }

    @JvmStatic
    private fun azure() {
        // Azure + Amethyst -> Azure Shard
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.gemOrDust(VanillaMaterialKeys.AMETHYST, 1),
                chemicalHelper.from(RagiumEssenceType.AZURE, 10),
                RagiumItems.getGem(RagiumMaterialKeys.AZURE).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_shard"))
        // Azure + Iron -> Azure Steel
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(VanillaMaterialKeys.IRON, 1),
                chemicalHelper.from(RagiumEssenceType.AZURE, 40),
                RagiumItems.getIngot(RagiumMaterialKeys.AZURE_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_steel"))

        // Ethene + Catalyst -> HDPE
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromItem(RagiumItems.POLYMER_CATALYST),
                fluidCreator.fromTagKey(MekanismTags.Fluids.ETHENE, 100),
            ).addResult(resultHelper.item(MekanismItems.HDPE_PELLET))
            .save(output)
    }

    @JvmStatic
    private fun molten() {
        oreToGem(RagiumMaterialKeys.CRIMSON_CRYSTAL)
        oreToGem(RagiumMaterialKeys.WARPED_CRYSTAL)

        // Crimson + Warped -> Eldritch
        ChemicalChemicalToChemicalRecipeBuilder
            .chemicalInfusing(
                chemicalHelper.from(RagiumMaterialKeys.CRIMSON_CRYSTAL, 1),
                chemicalHelper.from(RagiumMaterialKeys.WARPED_CRYSTAL, 1),
                RagiumMaterialKeys.ELDRITCH_PEARL.asStack(1),
            ).build(output, id("chemical_infusing/eldritch_flux"))

        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val molten: HTFluidContent<*, *, *> = data.molten
            // Fluid <-> Chemical
            RotaryRecipeBuilder
                .rotary(
                    fluidHelper.from(molten.commonTag, 1),
                    chemicalHelper.from(data, 1),
                    data.asStack(1),
                    molten.toStack(1),
                ).build(output, id("rotary/${molten.getPath()}"))
            // Chemical -> Item
            ChemicalCrystallizerRecipeBuilder
                .crystallizing(
                    chemicalHelper.from(data, RagiumConst.MOLTEN_TO_GEM),
                    RagiumItems.getGem(data).toStack(),
                ).build(output, id("crystallizing/${data.asMaterialName()}"))
        }
    }

    @JvmStatic
    private fun deep() {
        // Ore -> Scrap
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumCommonTags.Items.ORES_DEEP_SCRAP),
                RagiumItems.getScrap(RagiumMaterialKeys.DEEP_STEEL).toStack(2),
            ).build(output, id("processing/deep_steel/resonant_debris_to_scrap"))

        // Azure + Netherite Scrap -> Deep Scrap
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE),
                chemicalHelper.from(RagiumEssenceType.AZURE, 80),
                RagiumItems.getScrap(RagiumMaterialKeys.DEEP_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_scrap"))
        // Azure + Netherite Ingot -> Deep Ingot
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(VanillaMaterialKeys.NETHERITE, 1),
                chemicalHelper.from(RagiumEssenceType.AZURE, 320),
                RagiumItems.getIngot(RagiumMaterialKeys.DEEP_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_steel"))

        // Deep + Azure Steel Ingot -> Deep Ingot
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(RagiumMaterialKeys.AZURE_STEEL, 1),
                chemicalHelper.from(RagiumEssenceType.DEEP, 160),
                RagiumItems.getIngot(RagiumMaterialKeys.DEEP_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_steel_alt"))
    }

    //    Extensions    //

    @JvmStatic
    private val itemHelper: IItemStackIngredientCreator = IMekanismAccess.INSTANCE.itemStackIngredientCreator()

    @JvmStatic
    private val fluidHelper: IFluidStackIngredientCreator = IMekanismAccess.INSTANCE.fluidStackIngredientCreator()

    @JvmStatic
    private val chemicalHelper: IChemicalStackIngredientCreator =
        IMekanismAccess.INSTANCE.chemicalStackIngredientCreator()

    @JvmStatic
    private fun HTMaterialLike.asStack(size: Long): ChemicalStack = RagiumMekanismAddon.getChemical(this).asStack(size)

    @JvmStatic
    private fun IItemStackIngredientCreator.from(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): ItemStackIngredient =
        from(prefix.itemTagKey(material), count)

    @JvmStatic
    private fun IItemStackIngredientCreator.gemOrDust(material: HTMaterialLike, count: Int = 1): ItemStackIngredient =
        from(this@RagiumMekanismRecipeProvider.gemOrDust(material), count)

    @JvmStatic
    private fun IItemStackIngredientCreator.ingotOrDust(material: HTMaterialLike, count: Int = 1): ItemStackIngredient =
        from(this@RagiumMekanismRecipeProvider.ingotOrDust(material), count)

    @JvmStatic
    private fun IChemicalStackIngredientCreator.from(material: HTMaterialLike, amount: Number): ChemicalStackIngredient =
        fromHolder(RagiumMekanismAddon.getChemical(material), amount.toLong())

    @JvmStatic
    private fun oreToGem(material: HTMaterialLike) {
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(CommonMaterialPrefixes.ORE, material),
                RagiumItems.getGem(material).toStack(2),
            ).build(output, id("processing/${material.asMaterialName()}/from_ore"))
    }
}
