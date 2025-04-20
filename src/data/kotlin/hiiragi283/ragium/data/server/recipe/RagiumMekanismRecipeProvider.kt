package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import mekanism.api.IMekanismAccess
import mekanism.api.chemical.ChemicalStack
import mekanism.api.datagen.recipe.builder.*
import mekanism.api.recipes.ingredients.ItemStackIngredient
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import mekanism.common.registries.MekanismChemicals
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.neoforged.neoforge.common.Tags

object RagiumMekanismRecipeProvider : HTRecipeProvider.Modded(IntegrationMods.MEK) {
    private val itemHelper: IItemStackIngredientCreator = IMekanismAccess.INSTANCE.itemStackIngredientCreator()
    private val fluidHelper: IFluidStackIngredientCreator = IMekanismAccess.INSTANCE.fluidStackIngredientCreator()
    private val chemicalHelper: IChemicalStackIngredientCreator =
        IMekanismAccess.INSTANCE.chemicalStackIngredientCreator()

    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        chemicalConversion(output)
        enriching(output)
        infusing(output)

        oreProcess(output)
    }

    private fun chemicalConversion(output: RecipeOutput) {
        fun toChemical(factory: (ItemStackIngredient, ChemicalStack) -> ItemStackToChemicalRecipeBuilder, prefix: String) {
            // Dust -> Chemical
            factory(
                itemHelper.from(HTTagPrefixes.DUST.createItemTag(RagiumMaterials.RAGINITE)),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(10),
            ).build(output, RagiumAPI.id("$prefix/raginite/from_dust"))
            // Enriched -> Chemical
            factory(
                itemHelper.from(RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE),
                RagiumMekanismAddon.CHEMICAL_RAGINITE.asStack(80),
            ).build(output, RagiumAPI.id("$prefix/raginite/from_enriched"))
        }

        toChemical(ItemStackToChemicalRecipeBuilder::chemicalConversion, "chemical_conversion")
        toChemical(ItemStackToChemicalRecipeBuilder::oxidizing, "oxidizing")
    }

    private fun enriching(output: RecipeOutput) {
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTTagPrefixes.DUST.createItemTag(RagiumMaterials.RAGINITE)),
                RagiumMekanismAddon.ITEM_ENRICHED_RAGINITE.toStack(),
            ).build(output, RagiumAPI.id("enriching/enrich/raginite"))
    }

    private fun infusing(output: RecipeOutput) {
        // Raginite + Copper -> Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.INGOTS_COPPER),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 40),
                RagiumItems.Ingots.RAGI_ALLOY.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/ragi_alloy"))
        // Raginite + Gold -> Advanced Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.INGOTS_GOLD),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 80),
                RagiumItems.Ingots.ADVANCED_RAGI_ALLOY.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/advanced_ragi_alloy"))
        // Raginite + Diamond -> Ragi-Crystal
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.GEMS_DIAMOND),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_RAGINITE, 60),
                RagiumItems.RawResources.RAGI_CRYSTAL.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/ragi_crystal"))

        // Azure + Iron -> Azure Steel
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Tags.Items.INGOTS_IRON),
                chemicalHelper.fromHolder(RagiumMekanismAddon.CHEMICAL_AZURE, 20),
                RagiumItems.Ingots.AZURE_STEEL.toStack(),
                false,
            ).build(output, RagiumAPI.id("metallurgic_infusing/azure_steel"))
    }

    private fun oreProcess(output: RecipeOutput) {
        // Enriching
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTTagPrefixes.ORE.createItemTag(RagiumMaterials.RAGINITE)),
                RagiumItems.Dusts.RAGINITE.toStack(2),
            ).build(output, RagiumAPI.id("processing/raginite/dust/from_ore"))

        val rawMaterial: ItemStackIngredient = itemHelper.from(HTTagPrefixes.RAW_MATERIAL.createItemTag(RagiumMaterials.RAGINITE), 3)
        val ore: ItemStackIngredient = itemHelper.from(HTTagPrefixes.ORE.createItemTag(RagiumMaterials.RAGINITE))

        ItemStackToItemStackRecipeBuilder
            .enriching(
                rawMaterial,
                RagiumItems.Dusts.RAGINITE.toStack(4),
            ).build(output, RagiumAPI.id("processing/raginite/dust/from_raw_ore"))

        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTTagPrefixes.DIRTY_DUST.createItemTag(RagiumMaterials.RAGINITE)),
                RagiumItems.Dusts.RAGINITE.toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/dust/from_dirty_dust"))
        // Crushing
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTTagPrefixes.CLUMP.createItemTag(RagiumMaterials.RAGINITE)),
                RagiumMekanismAddon.OreResources.DIRTY_DUST.toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/dirty_dust/from_clump"))
        // Purifying
        ItemStackChemicalToItemStackRecipeBuilder
            .purifying(
                itemHelper.from(HTTagPrefixes.SHARD.createItemTag(RagiumMaterials.RAGINITE)),
                chemicalHelper.fromHolder(MekanismChemicals.OXYGEN, 200),
                RagiumMekanismAddon.OreResources.CLUMP.toStack(),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/clump/from_shard"))

        ItemStackChemicalToItemStackRecipeBuilder
            .purifying(
                itemHelper.from(HTTagPrefixes.RAW_MATERIAL.createItemTag(RagiumMaterials.RAGINITE)),
                chemicalHelper.fromHolder(MekanismChemicals.OXYGEN, 200),
                RagiumMekanismAddon.OreResources.CLUMP.toStack(2),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/clump/from_raw_ore"))

        ItemStackChemicalToItemStackRecipeBuilder
            .purifying(
                ore,
                chemicalHelper.fromHolder(MekanismChemicals.OXYGEN, 200),
                RagiumMekanismAddon.OreResources.CLUMP.toStack(3),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/clump/from_ore"))
        // Injecting
        ItemStackChemicalToItemStackRecipeBuilder
            .injecting(
                rawMaterial,
                chemicalHelper.fromHolder(MekanismChemicals.HYDROGEN_CHLORIDE, 200),
                RagiumMekanismAddon.OreResources.SHARD.toStack(8),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/shard/from_raw_ore"))

        ItemStackChemicalToItemStackRecipeBuilder
            .injecting(
                ore,
                chemicalHelper.fromHolder(MekanismChemicals.HYDROGEN_CHLORIDE, 200),
                RagiumMekanismAddon.OreResources.SHARD.toStack(4),
                false,
            ).build(output, RagiumAPI.id("processing/raginite/shard/from_ore"))

        ItemStackChemicalToItemStackRecipeBuilder
            .injecting(
                itemHelper.from(HTTagPrefixes.CRYSTAL.createItemTag(RagiumMaterials.RAGINITE)),
                chemicalHelper.fromHolder(MekanismChemicals.HYDROGEN_CHLORIDE, 200),
                RagiumMekanismAddon.OreResources.SHARD.toStack(),
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
                RagiumMekanismAddon.OreResources.CRYSTAL.toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/crystal/from_slurry"))
    }
}
