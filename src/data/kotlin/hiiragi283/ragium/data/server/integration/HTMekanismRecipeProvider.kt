package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import hiiragi283.ragium.integration.RagiumMekIntegration
import mekanism.api.datagen.recipe.builder.*
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess
import mekanism.common.registries.MekanismChemicals
import mekanism.common.registries.MekanismItems
import mekanism.common.tags.MekanismTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.neoforged.neoforge.common.Tags

object HTMekanismRecipeProvider : RagiumRecipeProvider.ModChild("mekanism") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Yellow Cake -> Yellow Cake Uranium
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(RagiumItems.YELLOW_CAKE)
            .itemOutput(MekanismItems.YELLOW_CAKE_URANIUM, 8)
            .save(output)
        // Yellow Cake Uranium -> Yellow Cake
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(MekanismItems.YELLOW_CAKE_URANIUM, 8)
            .itemOutput(RagiumItems.YELLOW_CAKE)
            .save(output)

        // Nutrition Paste -> Meat Ingot
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(MekanismTags.Fluids.NUTRITIONAL_PASTE, 150)
            .itemOutput(RagiumItems.MEAT_INGOT)
            .save(output)

        registerRaginite(output)
    }

    private fun registerRaginite(output: RecipeOutput) {
        val fluidAccess: IFluidStackIngredientCreator = IngredientCreatorAccess.fluid()
        val itemAccess: IItemStackIngredientCreator = IngredientCreatorAccess.item()
        val chemicalAccess: IChemicalStackIngredientCreator = IngredientCreatorAccess.chemicalStack()

        // Raw + H2SO4 -> Dirty Slurry
        fun toSlurry(input: HTTagPrefix, countIn: Int, countOut: Long) {
            ChemicalDissolutionRecipeBuilder
                .dissolution(
                    itemAccess.from(input.createTag(RagiumMaterials.RAGINITE), countIn),
                    chemicalAccess.from(MekanismChemicals.SULFURIC_ACID, 100),
                    RagiumMekIntegration.RAGINITE_SLURRY.dirtySlurry.getStack(countOut),
                    true,
                ).build(output, RagiumAPI.id("processing/raginite/slurry/from_${input.serializedName}"))
        }
        toSlurry(HTTagPrefix.RAW_MATERIAL, 3, 2000)
        toSlurry(HTTagPrefix.ORE, 1, 1000)

        // Water + Dirty Slurry -> Clean Slurry
        FluidChemicalToChemicalRecipeBuilder
            .washing(
                fluidAccess.from(Tags.Fluids.WATER, 5),
                chemicalAccess.from(RagiumMekIntegration.RAGINITE_SLURRY.dirtySlurry, 1),
                RagiumMekIntegration.RAGINITE_SLURRY.cleanSlurry.getStack(1),
            ).build(output, RagiumAPI.id("processing/raginite/slurry/clean"))

        // Clean Slurry -> Crystal
        ChemicalCrystallizerRecipeBuilder
            .crystallizing(
                chemicalAccess.from(RagiumMekIntegration.RAGINITE_SLURRY.cleanSlurry, 200),
                RagiumItems.getMaterialItem(HTTagPrefix.CRYSTAL, RagiumMaterials.RAGINITE).toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/crystal/from_clean"))

        // XX + HCl -> Shard
        fun toShard(input: HTTagPrefix, countIn: Int, countOut: Int) {
            ItemStackChemicalToItemStackRecipeBuilder
                .injecting(
                    itemAccess.from(input.createTag(RagiumMaterials.RAGINITE), countIn),
                    chemicalAccess.from(MekanismChemicals.HYDROGEN_CHLORIDE, 1),
                    RagiumItems.getMaterialItem(HTTagPrefix.SHARD, RagiumMaterials.RAGINITE).toStack(countOut),
                    true,
                ).build(output, RagiumAPI.id("processing/raginite/shard/from_${input.serializedName}"))
        }
        toShard(HTTagPrefix.RAW_MATERIAL, 3, 8)
        toShard(HTTagPrefix.ORE, 1, 4)
        toShard(HTTagPrefix.CRYSTAL, 1, 1)

        // XX + O2 -> Clump
        fun toClump(input: HTTagPrefix, count: Int) {
            ItemStackChemicalToItemStackRecipeBuilder
                .purifying(
                    itemAccess.from(input.createTag(RagiumMaterials.RAGINITE)),
                    chemicalAccess.from(MekanismChemicals.OXYGEN, 1),
                    RagiumItems.getMaterialItem(HTTagPrefix.CLUMP, RagiumMaterials.RAGINITE).toStack(count),
                    true,
                ).build(output, RagiumAPI.id("processing/raginite/clump/from_${input.serializedName}"))
        }
        toClump(HTTagPrefix.RAW_MATERIAL, 2)
        toClump(HTTagPrefix.ORE, 3)
        toClump(HTTagPrefix.SHARD, 1)

        // Clump -> Dirty
        ItemStackToItemStackRecipeBuilder
            .crushing(
                itemAccess.from(HTTagPrefix.CLUMP.createTag(RagiumMaterials.RAGINITE)),
                RagiumItems.getMaterialItem(HTTagPrefix.DIRTY_DUST, RagiumMaterials.RAGINITE).toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/dirty/from_clump"))
        // Dirty -> Dust
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemAccess.from(HTTagPrefix.DIRTY_DUST.createTag(RagiumMaterials.RAGINITE)),
                RagiumItems.getMaterialItem(HTTagPrefix.DUST, RagiumMaterials.RAGINITE).toStack(),
            ).build(output, RagiumAPI.id("processing/raginite/dust/from_dirty_dust"))
    }
}
