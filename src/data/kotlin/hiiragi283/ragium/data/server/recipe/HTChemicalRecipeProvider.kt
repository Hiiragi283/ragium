package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTAlloyFurnaceRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSolidifierRecipeBuilder
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTChemicalRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        water(output)

        nitrogen(output)
        oxygen(output)
        fluorine(output)

        alkali(output)
        aluminum(output)
        glass(output)
        sulfur(output)

        slag(output)
        uranium(output)
    }

    //    Water    //

    private fun water(output: RecipeOutput) {
        // Snow Block -> 4x Snow Ball
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(Items.SNOW_BLOCK)
            .catalyst(RagiumItems.BALL_PRESS_MOLD)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_block")
        // Water -> Snowball
        HTSolidifierRecipeBuilder(lookup)
            .waterInput(250)
            .catalyst(RagiumItems.BALL_PRESS_MOLD)
            .itemOutput(Items.SNOWBALL)
            .save(output)

        // Water -> Ice
        HTSolidifierRecipeBuilder(lookup)
            .waterInput()
            .catalyst(RagiumItems.BLOCK_PRESS_MOLD)
            .itemOutput(Items.ICE)
            .save(output)
        // Blue Ice -> 9x Packed Ice
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Items.BLUE_ICE)
            .itemOutput(Items.PACKED_ICE, 9)
            .save(output)
        // Packed Ice -> 9x Ice
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Items.PACKED_ICE)
            .itemOutput(Items.ICE, 9)
            .save(output)
    }

    //    Nitrogen    //

    private fun nitrogen(output: RecipeOutput) {
        // 2x KNO3 + H2SO4 -> 2x HNO3 + K2SO4
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.SALTPETER)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 500)
            .itemOutput(RagiumItems.SLAG)
            .fluidOutput(RagiumVirtualFluids.NITRIC_ACID, 500)
            .save(output, RagiumAPI.id("nitric_acid"))

        // 3x H2SO4 + HNO3 -> Mixture Acid
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 300)
            .fluidInput(RagiumVirtualFluids.NITRIC_ACID.commonTag, 100)
            .fluidOutput(RagiumVirtualFluids.MIXTURE_ACID, 400)
            .save(output)
        // Nitration
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumFluidTags.NON_NITRO_FUEL, 800)
            .fluidInput(RagiumVirtualFluids.MIXTURE_ACID.commonTag, 100)
            .fluidOutput(RagiumVirtualFluids.NITRO_FUEL, 800)
            .save(output)

        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(RagiumItemTags.PAPER)
            .fluidInput(RagiumVirtualFluids.NITROGLYCERIN.commonTag, 125)
            .itemOutput(RagiumItems.DYNAMITE)
            .save(output)

        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .itemInput(Tags.Items.SANDS)
            .fluidInput(RagiumVirtualFluids.AROMATIC_COMPOUND.commonTag, 250)
            .fluidInput(RagiumVirtualFluids.MIXTURE_ACID.commonTag, 250)
            .itemOutput(Items.TNT)
            .save(output)
    }

    //    Oxygen    //

    private fun oxygen(output: RecipeOutput) {
        // Air -> N2 + O2
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumVirtualFluids.AIR.commonTag, 50)
            .fluidOutput(RagiumVirtualFluids.OXYGEN, 10)
            .fluidOutput(RagiumVirtualFluids.NITROGEN, 40)
            .save(output)
        // Rocket Fuel
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumVirtualFluids.HYDROGEN.commonTag, 200)
            .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag, 100)
            .fluidOutput(RagiumVirtualFluids.ROCKET_FUEL, 300)
            .save(output)
    }

    //    Fluorine    //

    private fun fluorine(output: RecipeOutput) {
        // CaF2 + H2SO4 -> CaSO4 + 2x HF(aq)
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag)
            .fluidOutput(RagiumVirtualFluids.HYDROFLUORIC_ACID)
            .save(output)
    }

    //    Alkali    //

    private fun alkali(output: RecipeOutput) {
        // Calcite -> Calcite Dust
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Items.CALCITE)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.CALCITE))
            .save(output)

        // Ash + Water -> Alkali Solution
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ASH)
            .waterInput(250)
            .fluidOutput(RagiumVirtualFluids.ALKALI_SOLUTION, 250)
            .saveSuffixed(output, "_from_ash")
        // Calcite + Water -> Alkali Solution
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.CALCITE)
            .waterInput(500)
            .fluidOutput(RagiumVirtualFluids.ALKALI_SOLUTION, 500)
            .saveSuffixed(output, "_from_calcite")

        // Ash + Seed Oil -> Soap
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ASH)
            .fluidInput(RagiumVirtualFluids.PLANT_OIL.commonTag, 250)
            .itemOutput(RagiumItems.SOAP, 2)
            .fluidOutput(RagiumVirtualFluids.GLYCEROL, 250)
            .saveSuffixed(output, "_from_ash")
        // Alkali Solution + Seed Oil -> Soap
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumVirtualFluids.ALKALI_SOLUTION.commonTag, 125)
            .fluidInput(RagiumVirtualFluids.PLANT_OIL.commonTag, 125)
            .itemOutput(RagiumItems.SOAP)
            .fluidOutput(RagiumVirtualFluids.GLYCEROL, 125)
            .saveSuffixed(output, "_from_alkali")
    }

    //    Aluminum    //

    private fun aluminum(output: RecipeOutput) {
        // Bauxite + Lapis solution -> Alumina Solution
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
            .fluidInput(RagiumVirtualFluids.ALKALI_SOLUTION.commonTag, 400)
            .fluidOutput(RagiumVirtualFluids.ALUMINA_SOLUTION, 400)
            .save(output)
        // Alumina Solution -> Alumina
        HTSolidifierRecipeBuilder(lookup)
            .fluidInput(RagiumVirtualFluids.ALUMINA_SOLUTION.commonTag)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.ALUMINA))
            .save(output)
        // Alumina + 4x Coal -> Aluminum Ingot
        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .itemInput(HTTagPrefix.GEM, VanillaMaterials.COAL, 4)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM))
            .saveSuffixed(output, "_with_coal")

        // Al + HF + Alkali -> Na3AlF6
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINUM)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 6000)
            .fluidInput(RagiumVirtualFluids.ALKALI_SOLUTION.commonTag, 3000)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.GEM, CommonMaterials.CRYOLITE))
            .save(output)
        // Alumina + Cryolite -> 3x Aluminum Ingot
        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM), 3)
            .saveSuffixed(output, "_with_cryolite")
    }

    //    Glass    //

    private fun glass(output: RecipeOutput) {
        // XX -> Molten Glass
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.GLASS_BLOCKS)
            .fluidOutput(RagiumFluids.GLASS)
            .saveSuffixed(output, "_from_glass")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.SANDS)
            .fluidOutput(RagiumFluids.GLASS)
            .saveSuffixed(output, "_from_sand")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.SANDSTONE_BLOCKS)
            .fluidOutput(RagiumFluids.GLASS, 4000)
            .saveSuffixed(output, "_from_sandstone")
        // Molten Glass -> Glass
        HTSolidifierRecipeBuilder(lookup)
            .fluidInput(RagiumFluids.GLASS.commonTag)
            .catalyst(RagiumItemTags.MOLDS_BLOCK)
            .itemOutput(Items.GLASS)
            .save(output)

        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.CALCITE)
            .fluidInput(RagiumFluids.GLASS.commonTag)
            .itemOutput(Items.GLASS, 2)
            .save(output)
        // Custom Glasses
        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.QUARTZ, 4)
            .itemOutput(RagiumBlocks.QUARTZ_GLASS)
            .save(output)

        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(RagiumItemTags.GLASS_BLOCKS_QUARTZ)
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .itemOutput(RagiumBlocks.SOUL_GLASS)
            .save(output)

        HTAlloyFurnaceRecipeBuilder(lookup)
            .itemInput(RagiumItemTags.GLASS_BLOCKS_QUARTZ)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.OBSIDIAN, 4)
            .itemOutput(RagiumBlocks.OBSIDIAN_GLASS)
            .save(output)
    }

    //    Sulfur    //

    private fun sulfur(output: RecipeOutput) {
        // S + O2 -> SO2
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.SULFUR)
            .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag)
            .fluidOutput(RagiumVirtualFluids.SULFUR_DIOXIDE)
            .save(output)
        // SO2 + O -> SO3
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumVirtualFluids.SULFUR_DIOXIDE.commonTag)
            .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag, 500)
            .fluidOutput(RagiumVirtualFluids.SULFUR_TRIOXIDE)
            .save(output)
        // SO3 + H2O -> H2SO4
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumVirtualFluids.SULFUR_TRIOXIDE.commonTag)
            .waterInput()
            .fluidOutput(RagiumVirtualFluids.SULFURIC_ACID)
            .save(output)
    }

    private fun slag(output: RecipeOutput) {
        // Slag -> Gravel
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(RagiumItemTags.SLAG)
            .itemOutput(Items.GRAVEL)
            .saveSuffixed(output, "_from_slag")
    }

    //    Uranium    //

    private fun uranium(output: RecipeOutput) {
        // Poisonous Potato + H2SO4 -> Yellow Cake
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Items.POISONOUS_POTATO, 8)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 8000)
            .itemOutput(RagiumItems.YELLOW_CAKE)
            .save(output)
        // Cutting Yellow Cake
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.YELLOW_CAKE_PIECE, 8)
            .requires(RagiumItems.YELLOW_CAKE)
            .unlockedBy("has_cake", has(RagiumItems.YELLOW_CAKE))
            .savePrefixed(output)
        // Catastrophic Fuel
    }
}
