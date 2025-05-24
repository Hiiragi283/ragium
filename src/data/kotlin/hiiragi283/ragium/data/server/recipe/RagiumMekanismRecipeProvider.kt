package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.IMekanismAccess
import mekanism.api.chemical.ChemicalStack
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder
import mekanism.api.recipes.ingredients.ItemStackIngredient
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import net.neoforged.neoforge.common.Tags

object RagiumMekanismRecipeProvider : HTRecipeProvider() {
    private val itemHelper: IItemStackIngredientCreator = IMekanismAccess.INSTANCE.itemStackIngredientCreator()
    private val fluidHelper: IFluidStackIngredientCreator = IMekanismAccess.INSTANCE.fluidStackIngredientCreator()
    private val chemicalHelper: IChemicalStackIngredientCreator =
        IMekanismAccess.INSTANCE.chemicalStackIngredientCreator()

    override fun buildRecipeInternal() {
        chemicalConversion()
        crystallizing()
        enriching()
        infusing()
        rotary()

        // oreProcess()
    }

    private fun chemicalConversion() {
        fun toChemical(factory: (ItemStackIngredient, ChemicalStack) -> ItemStackToChemicalRecipeBuilder, prefix: String) {
            // Dust -> Chemical
            factory(
                itemHelper.from(RagiumItemTags.DUSTS_RAGINITE),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(10),
            ).build(output, RagiumAPI.id("$prefix/raginite/from_dust"))
            // Enriched -> Chemical
            factory(
                itemHelper.from(RagiumItemTags.ENRICHED_RAGINITE),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(80),
            ).build(output, RagiumAPI.id("$prefix/raginite/from_enriched"))

            // Azure Shard -> Chemical
            factory(
                itemHelper.from(RagiumItems.AZURE_SHARD),
                RagiumMekanismAddon.CHEMICAL_AZURE.asStack(10),
            ).build(output, RagiumAPI.id("$prefix/azure/from_shard"))
            // Enriched -> Chemical
            factory(
                itemHelper.from(RagiumItemTags.ENRICHED_AZURE),
                RagiumMekanismAddon.CHEMICAL_AZURE.asStack(80),
            ).build(output, RagiumAPI.id("$prefix/azure/from_enriched"))
        }

        toChemical(ItemStackToChemicalRecipeBuilder::chemicalConversion, "chemical_conversion")
        toChemical(ItemStackToChemicalRecipeBuilder::oxidizing, "oxidizing")
    }

    private fun crystallizing() {
        ChemicalCrystallizerRecipeBuilder
            .crystallizing(
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP, 1000),
                RagiumItems.CRIMSON_CRYSTAL.toStack(),
            ).build(output, RagiumAPI.id("crystallizing/crimson_crystal"))

        ChemicalCrystallizerRecipeBuilder
            .crystallizing(
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_WARPED_SAP, 1000),
                RagiumItems.WARPED_CRYSTAL.toStack(),
            ).build(output, RagiumAPI.id("crystallizing/warped_crystal"))
    }

    private fun enriching() {
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumItemTags.DUSTS_RAGINITE),
                RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE.toStack(),
            ).build(output, RagiumAPI.id("enriching/enrich/raginite"))

        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumItems.AZURE_SHARD),
                RagiumMekanismAddon.ITEM_ENRICHED_AZURE.toStack(),
            ).build(output, RagiumAPI.id("enriching/enrich/azure"))

        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumItemTags.ORES_RAGINITE),
                RagiumItems.RAGINITE_DUST.toStack(12),
            ).build(output, RagiumAPI.id("processing/raginite/from_ore"))

        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumItemTags.ORES_RAGI_CRYSTAL),
                RagiumItems.RAGI_CRYSTAL.toStack(2),
            ).build(output, RagiumAPI.id("processing/ragi_crystal/from_ore"))
    }

    private fun infusing() {
        // Raginite + Copper -> Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.INGOTS_COPPER),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 40),
                RagiumItems.RAGI_ALLOY_INGOT.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/ragi_alloy"))
        // Raginite + Gold -> Advanced Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.INGOTS_GOLD),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 80),
                RagiumItems.ADVANCED_RAGI_ALLOY_INGOT.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/advanced_ragi_alloy"))
        // Raginite + Diamond -> Ragi-Crystal
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.GEMS_DIAMOND),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 60),
                RagiumItems.RAGI_CRYSTAL.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/ragi_crystal"))

        // Azure + Iron -> Azure Steel
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.INGOTS_IRON),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 40),
                RagiumItems.AZURE_STEEL_INGOT.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/azure_steel"))
    }

    private fun rotary() {
        RotaryRecipeBuilder
            .rotary(
                fluidHelper.from(RagiumFluidContents.CRIMSON_SAP.commonTag, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP, 1),
                RagiumMekanismAddon.CHEMICAL_CRIMSON_SAP.asStack(1),
                RagiumFluidContents.CRIMSON_SAP.toStack(1),
            ).build(output, RagiumAPI.id("rotary/crimson_sap"))

        RotaryRecipeBuilder
            .rotary(
                fluidHelper.from(RagiumFluidContents.WARPED_SAP.commonTag, 1),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_WARPED_SAP, 1),
                RagiumMekanismAddon.CHEMICAL_WARPED_SAP.asStack(1),
                RagiumFluidContents.WARPED_SAP.toStack(1),
            ).build(output, RagiumAPI.id("rotary/warped_sap"))
    }

    /*private fun oreProcess() {
        // Enriching
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumItemTags.ORES_RAGINITE),
                RagiumItems.RAGINITE_DUST.toStack(12),
            ).build(output, RagiumAPI.id("processing/raginite/dust/from_ore"))

        val rawMaterial: ItemStackIngredient = itemHelper.from(RagiumItemTags.RAW_MATERIALS_RAGINITE, 3)
        val ore: ItemStackIngredient = itemHelper.from(RagiumItemTags.ORES_RAGINITE)

        ItemStackToItemStackRecipeBuilder
            .enriching(
                rawMaterial,
                RagiumItems.RAGINITE_DUST.toStack(4),
            ).build(output, RagiumAPI.id("processing/raginite/dust/from_raw_ore"))

        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumItemTags.DIRTY_DUSTS_RAGINITE),
                RagiumItems.RAGINITE_DUST.toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/dust/from_dirty_dust"))
        // Crushing
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumItemTags.CLUMPS_RAGINITE),
                RagiumMekanismAddon.ITEM_DIRTY_RAGINITE_DUST.toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/dirty_dust/from_clump"))
        // Purifying
        ItemStackChemicalToItemStackRecipeBuilder
            .purifying(
                itemHelper.from(RagiumItemTags.SHARDS_RAGINITE),
                chemicalHelper.fromHolder(MekanismChemicals.OXYGEN, 200),
                RagiumMekanismAddon.ITEM_RAGINITE_CLUMP.toStack(),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/clump/from_shard"))

        ItemStackChemicalToItemStackRecipeBuilder
            .purifying(
                itemHelper.from(RagiumItemTags.RAW_MATERIALS_RAGINITE),
                chemicalHelper.fromHolder(MekanismChemicals.OXYGEN, 200),
                RagiumMekanismAddon.ITEM_RAGINITE_CLUMP.toStack(2),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/clump/from_raw_ore"))

        ItemStackChemicalToItemStackRecipeBuilder
            .purifying(
                ore,
                chemicalHelper.fromHolder(MekanismChemicals.OXYGEN, 200),
                RagiumMekanismAddon.ITEM_RAGINITE_CLUMP.toStack(3),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/clump/from_ore"))
        // Injecting
        ItemStackChemicalToItemStackRecipeBuilder
            .injecting(
                rawMaterial,
                chemicalHelper.fromHolder(MekanismChemicals.HYDROGEN_CHLORIDE, 200),
                RagiumMekanismAddon.ITEM_RAGINITE_SHARD.toStack(8),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/shard/from_raw_ore"))

        ItemStackChemicalToItemStackRecipeBuilder
            .injecting(
                ore,
                chemicalHelper.fromHolder(MekanismChemicals.HYDROGEN_CHLORIDE, 200),
                RagiumMekanismAddon.ITEM_RAGINITE_SHARD.toStack(4),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/shard/from_ore"))

        ItemStackChemicalToItemStackRecipeBuilder
            .injecting(
                itemHelper.from(RagiumItemTags.CRYSTALS_RAGINITE),
                chemicalHelper.fromHolder(MekanismChemicals.HYDROGEN_CHLORIDE, 200),
                RagiumMekanismAddon.ITEM_RAGINITE_SHARD.toStack(),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/shard/from_crystal"))
        // Slurry
        ChemicalDissolutionRecipeBuilder
            .dissolution(
                rawMaterial,
                chemicalHelper.fromHolder(MekanismChemicals.SULFURIC_ACID, 100),
                ChemicalStack(RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY, 2000),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/slurry/dirty/from_raw_ore"))

        ChemicalDissolutionRecipeBuilder
            .dissolution(
                ore,
                chemicalHelper.fromHolder(MekanismChemicals.SULFURIC_ACID, 100),
                ChemicalStack(RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY, 1000),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/slurry/dirty/from_ore"))

        FluidChemicalToChemicalRecipeBuilder
            .washing(
                fluidHelper.from(Tags.Fluids.WATER, 5),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY, 1),
                ChemicalStack(RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.cleanSlurry, 1),
            ).build(output, RagiumAPI.id("processing/raginite/slurry/clean"))

        ChemicalCrystallizerRecipeBuilder
            .crystallizing(
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE_SLURRY.cleanSlurry, 200),
                RagiumMekanismAddon.ITEM_RAGINITE_CRYSTAL.toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/crystal/from_slurry"))
    }*/
}
