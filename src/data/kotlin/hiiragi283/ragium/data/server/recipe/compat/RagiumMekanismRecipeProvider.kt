package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import mekanism.api.IMekanismAccess
import mekanism.api.chemical.ChemicalStack
import mekanism.api.datagen.recipe.builder.ChemicalChemicalToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder
import mekanism.api.recipes.ingredients.ItemStackIngredient
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import mekanism.common.registration.impl.DeferredChemical
import net.minecraft.world.item.Items

object RagiumMekanismRecipeProvider : HTRecipeProvider.Integration(RagiumConst.MEKANISM) {
    override fun buildRecipeInternal() {
        chemicalConversion()

        raginite()
        azure()
        crimson()
        warped()
        eldritch()
        deep()
    }

    private fun chemicalConversion() {
        fun toChemical(factory: (ItemStackIngredient, ChemicalStack) -> ItemStackToChemicalRecipeBuilder, prefix: String) {
            // Dust -> Chemical
            factory(
                itemHelper.from(HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(10),
            ).build(output, id("$prefix/raginite/from_dust"))
            // Enriched -> Chemical
            factory(
                itemHelper.from(RagiumModTags.Items.ENRICHED_RAGINITE),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(80),
            ).build(output, id("$prefix/raginite/from_enriched"))

            // Azure Shard -> Chemical
            factory(
                itemHelper.gemOrDust(RagiumMaterialType.AZURE, 1),
                RagiumMekanismAddon.CHEMICAL_AZURE.asStack(10),
            ).build(output, id("$prefix/azure/from_shard"))
            // Enriched -> Chemical
            factory(
                itemHelper.from(RagiumModTags.Items.ENRICHED_AZURE),
                RagiumMekanismAddon.CHEMICAL_AZURE.asStack(80),
            ).build(output, id("$prefix/azure/from_enriched"))
        }

        toChemical(ItemStackToChemicalRecipeBuilder::chemicalConversion, "chemical_conversion")
        toChemical(ItemStackToChemicalRecipeBuilder::oxidizing, "oxidizing")
    }

    private fun raginite() {
        // Ore -> Dust
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTMaterialVariant.ORE, RagiumMaterialType.RAGINITE),
                RagiumItems.getDust(RagiumMaterialType.RAGINITE).toStack(12),
            ).build(output, id("processing/raginite/from_ore"))
        // Enrich
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTMaterialVariant.DUST, RagiumMaterialType.RAGINITE),
                RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE.toStack(),
            ).build(output, id("enriching/enrich/raginite"))
        // Raginite + Copper -> Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.COPPER, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 40),
                RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_alloy"))
        // Raginite + Gold -> Advanced Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.GOLD, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 80),
                RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/advanced_ragi_alloy"))

        // Raginite + Diamond -> Ragi-Crystal
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.gemOrDust(HTVanillaMaterialType.DIAMOND, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 60),
                RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_crystal"))
        // Ore -> Crystal
        oreToGem(RagiumMaterialType.RAGI_CRYSTAL)

        // Raginite + Apple -> Ragi-Cherry
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(RagiumCommonTags.Items.FOODS_APPLE),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 40),
                RagiumItems.RAGI_CHERRY.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_cherry"))
    }

    private fun azure() {
        // Enrich
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.gemOrDust(RagiumMaterialType.AZURE, 1),
                RagiumMekanismAddon.ITEM_ENRICHED_AZURE.toStack(),
            ).build(output, id("enriching/enrich/azure"))
        // Azure + Amethyst -> Azure Shard
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.gemOrDust(HTVanillaMaterialType.AMETHYST, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 10),
                RagiumItems.getGem(RagiumMaterialType.AZURE).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_shard"))
        // Azure + Iron -> Azure Steel
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.IRON, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 40),
                RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_steel"))
    }

    private fun crimson() {
        oreToGem(RagiumMaterialType.CRIMSON_CRYSTAL)

        fluidCrystallizing(
            RagiumFluidContents.CRIMSON_SAP,
            RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP,
            RagiumMaterialType.CRIMSON_CRYSTAL,
        )
    }

    private fun warped() {
        oreToGem(RagiumMaterialType.WARPED_CRYSTAL)

        fluidCrystallizing(
            RagiumFluidContents.WARPED_SAP,
            RagiumMekanismAddon.CHEMICAL_WARPED_SAP,
            RagiumMaterialType.WARPED_CRYSTAL,
        )
    }

    private fun eldritch() {
        // Crimson + Warped -> Eldritch
        ChemicalChemicalToChemicalRecipeBuilder
            .chemicalInfusing(
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_WARPED_SAP, 1),
                RagiumMekanismAddon.CHEMICAL_ELDRITCH_FLUX.asStack(1),
            ).build(output, id("chemical_infusing/eldritch_flux"))

        fluidCrystallizing(
            RagiumFluidContents.ELDRITCH_FLUX,
            RagiumMekanismAddon.CHEMICAL_ELDRITCH_FLUX,
            RagiumMaterialType.ELDRITCH_PEARL,
        )
    }

    private fun deep() {
        // Ore -> Scrap
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumCommonTags.Items.ORES_DEEP_SCRAP),
                RagiumItems.DEEP_SCRAP.toStack(2),
            ).build(output, id("processing/deep_steel/resonant_debris_to_scrap"))

        // Azure + Netherite Scrap -> Deep Scrap
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Items.NETHERITE_SCRAP),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 80),
                RagiumItems.DEEP_SCRAP.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_scrap"))
        // Azure + Netherite Ingot -> Deep Ingot
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.NETHERITE, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 160),
                RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_steel"))
    }

    //    Extensions    //

    private val itemHelper: IItemStackIngredientCreator = IMekanismAccess.INSTANCE.itemStackIngredientCreator()
    private val fluidHelper: IFluidStackIngredientCreator = IMekanismAccess.INSTANCE.fluidStackIngredientCreator()
    private val chemicalHelper: IChemicalStackIngredientCreator =
        IMekanismAccess.INSTANCE.chemicalStackIngredientCreator()

    private fun IItemStackIngredientCreator.from(
        variant: HTMaterialVariant,
        material: HTMaterialType,
        count: Int = 1,
    ): ItemStackIngredient = from(variant.itemTagKey(material), count)

    private fun IItemStackIngredientCreator.gemOrDust(material: HTMaterialType, count: Int = 1): ItemStackIngredient =
        from(this@RagiumMekanismRecipeProvider.gemOrDust(material), count)

    private fun IItemStackIngredientCreator.ingotOrDust(material: HTMaterialType, count: Int = 1): ItemStackIngredient =
        from(this@RagiumMekanismRecipeProvider.ingotOrDust(material), count)

    private fun oreToGem(material: HTMaterialType) {
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTMaterialVariant.ORE, material),
                RagiumItems.getGem(material).toStack(2),
            ).build(output, id("processing/${material.serializedName}/from_ore"))
    }

    private fun fluidCrystallizing(
        fluid: HTFluidContent<*, *, *>,
        chemical: DeferredChemical<*>,
        material: HTMaterialType,
        toItemAmount: Int = 1000,
    ) {
        // Fluid <-> Chemical
        RotaryRecipeBuilder
            .rotary(
                fluidHelper.from(fluid.commonTag, 1),
                chemicalHelper.fromHolder(chemical, 1),
                chemical.asStack(1),
                fluid.toStack(1),
            ).build(output, id("rotary/${fluid.id.path}"))
        // Chemical -> Item
        ChemicalCrystallizerRecipeBuilder
            .crystallizing(
                chemicalHelper.fromHolder(chemical, toItemAmount),
                RagiumItems.getGem(material).toStack(),
            ).build(output, id("crystallizing/${material.serializedName}"))
    }
}
