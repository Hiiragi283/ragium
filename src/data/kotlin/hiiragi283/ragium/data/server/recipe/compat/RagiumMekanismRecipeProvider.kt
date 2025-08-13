package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
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
                itemHelper.from(RagiumCommonTags.Items.DUSTS_RAGINITE),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(10),
            ).build(output, id("$prefix/raginite/from_dust"))
            // Enriched -> Chemical
            factory(
                itemHelper.from(RagiumCommonTags.Items.ENRICHED_RAGINITE),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(80),
            ).build(output, id("$prefix/raginite/from_enriched"))

            // Azure Shard -> Chemical
            factory(
                gemOrDust(RagiumConst.AZURE, 1),
                RagiumMekanismAddon.CHEMICAL_AZURE.asStack(10),
            ).build(output, id("$prefix/azure/from_shard"))
            // Enriched -> Chemical
            factory(
                itemHelper.from(RagiumCommonTags.Items.ENRICHED_AZURE),
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
                itemHelper.from(RagiumCommonTags.Items.ORES_RAGINITE),
                RagiumItems.Dusts.RAGINITE.toStack(12),
            ).build(output, id("processing/raginite/from_ore"))
        // Enrich
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumCommonTags.Items.DUSTS_RAGINITE),
                RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE.toStack(),
            ).build(output, id("enriching/enrich/raginite"))
        // Raginite + Copper -> Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                ingotOrDust("copper", 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 40),
                RagiumItems.Ingots.RAGI_ALLOY.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_alloy"))
        // Raginite + Gold -> Advanced Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                ingotOrDust("gold", 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 80),
                RagiumItems.Ingots.ADVANCED_RAGI_ALLOY.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/advanced_ragi_alloy"))

        // Raginite + Diamond -> Ragi-Crystal
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                gemOrDust("diamond", 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 60),
                RagiumItems.Gems.RAGI_CRYSTAL.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_crystal"))
        // Ore -> Crystal
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumCommonTags.Items.ORES_RAGI_CRYSTAL),
                RagiumItems.Gems.RAGI_CRYSTAL.toStack(2),
            ).build(output, id("processing/ragi_crystal/from_ore"))
    }

    private fun azure() {
        // Enrich
        ItemStackToItemStackRecipeBuilder
            .enriching(
                gemOrDust(RagiumConst.AZURE, 1),
                RagiumMekanismAddon.ITEM_ENRICHED_AZURE.toStack(),
            ).build(output, id("enriching/enrich/azure"))
        // Azure + Amethyst -> Azure Shard
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                ingotOrDust("amethyst", 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 10),
                RagiumItems.Gems.AZURE_SHARD.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_shard"))
        // Azure + Iron -> Azure Steel
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                gemOrDust("iron", 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 40),
                RagiumItems.Ingots.AZURE_STEEL.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_steel"))
    }

    private fun crimson() {
        fluidCrystallizing(
            RagiumFluidContents.CRIMSON_SAP,
            RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP,
            RagiumItems.Gems.CRIMSON_CRYSTAL,
        )
    }

    private fun warped() {
        fluidCrystallizing(
            RagiumFluidContents.WARPED_SAP,
            RagiumMekanismAddon.CHEMICAL_WARPED_SAP,
            RagiumItems.Gems.WARPED_CRYSTAL,
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
            RagiumItems.Gems.ELDRITCH_PEARL,
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
                ingotOrDust("netherite", 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 160),
                RagiumItems.Ingots.DEEP_STEEL.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_steel"))
    }

    //    Extensions    //

    private val itemHelper: IItemStackIngredientCreator = IMekanismAccess.INSTANCE.itemStackIngredientCreator()
    private val fluidHelper: IFluidStackIngredientCreator = IMekanismAccess.INSTANCE.fluidStackIngredientCreator()
    private val chemicalHelper: IChemicalStackIngredientCreator =
        IMekanismAccess.INSTANCE.chemicalStackIngredientCreator()

    private fun gemOrDust(name: String, count: Int): ItemStackIngredient = itemHelper.from(gemOrDust(name), count)

    private fun ingotOrDust(name: String, count: Int): ItemStackIngredient = itemHelper.from(ingotOrDust(name), count)

    private fun fluidCrystallizing(
        fluid: HTFluidContent<*, *, *>,
        chemical: DeferredChemical<*>,
        item: HTItemHolderLike,
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
                item.toStack(),
            ).build(output, id("crystallizing/${item.id.path}"))
    }
}
