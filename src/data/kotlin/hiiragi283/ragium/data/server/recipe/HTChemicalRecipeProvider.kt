package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTChemicalRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerAlkali(output)
        registerDeepant(output)
        registerFrozen(output)
        registerGlow(output)
        registerMagical(output)
        registerPrismarine(output)
        registerSculk(output)
        registerWither(output)
    }

    private fun registerAlkali(output: RecipeOutput) {
        // Ash -> Alkali
        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ASH)
            .itemOutput(RagiumItems.ALKALI_REAGENT)
            .saveSuffixed(output, "_from_ash")
        // Calcite -> Alkali
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Items.CALCITE, 4)
            .itemOutput(RagiumItems.ALKALI_REAGENT)
            .saveSuffixed(output, "_from_calcite")

        // Alkali + Seed Oil -> Soap
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(RagiumItems.ALKALI_REAGENT)
            .fluidInput(RagiumVirtualFluids.PLANT_OIL.commonTag, 1000)
            .itemOutput(RagiumItems.SOAP, 8)
            .save(output)
    }

    private fun registerDeepant(output: RecipeOutput) {
        // Deep Reagent
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Tags.Items.COBBLESTONES_DEEPSLATE, 16)
            .itemOutput(RagiumItems.DEEPANT_REAGENT)
            .save(output)

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Tags.Items.COBBLESTONES_DEEPSLATE, 4)
            .itemOutput(RagiumItems.DEEPANT_REAGENT)
            .save(output)
    }

    private fun registerFrozen(output: RecipeOutput) {
        // Snow Block -> 4x Snow Ball
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(Items.SNOW_BLOCK)
            .catalyst(RagiumItems.BALL_PRESS_MOLD)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_block")

        // Water -> Ice
        HTSolidifierRecipeBuilder()
            .waterInput()
            .itemOutput(Items.ICE)
            .save(output)
        // Blue Ice -> 9x Packed Ice
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Items.BLUE_ICE)
            .itemOutput(Items.PACKED_ICE, 9)
            .save(output)
        // Packed Ice -> 9x Ice
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Items.PACKED_ICE)
            .itemOutput(Items.ICE, 9)
            .save(output)

        // Frozen Reagent
        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.PACKED_ICE)
            .itemOutput(RagiumItems.FROZEN_REAGENT)
            .saveSuffixed(output, "_from_packed_ice")

        // Ice
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, Items.ICE)
            .requires(Tags.Items.BUCKETS_WATER)
            .requires(RagiumItems.FROZEN_REAGENT)
            .unlockedBy("has_reagent", has(RagiumItems.FROZEN_REAGENT))
            .savePrefixed(output)
    }

    private fun registerGlow(output: RecipeOutput) {
        // Glowstone -> 4x Glowstone Dust
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Items.GLOWSTONE)
            .itemOutput(Items.GLOWSTONE_DUST, 4)
            .save(output)

        // Glow Reagent
        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.GLOW_LICHEN, 8)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW)
            .saveSuffixed(output, "_from_lichen")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.GLOW_BERRIES, 4)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW)
            .saveSuffixed(output, "_from_berry")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW)
            .saveSuffixed(output, "_from_dust")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.GLOW_INK_SAC)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW, 2)
            .saveSuffixed(output, "_from_ink")

        // CaF2 + H2SO4 -> CaSO4 + 2x HF(aq)
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag)
            .fluidOutput(RagiumVirtualFluids.HYDROFLUORIC_ACID)
            .save(output)
    }

    private fun registerMagical(output: RecipeOutput) {
        // Magical Reagent
        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Tags.Items.GEMS_AMETHYST)
            .itemOutput(RagiumItems.MAGICAL_REAGENT)
            .saveSuffixed(output, "_from_amethyst")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Tags.Items.OBSIDIANS_CRYING)
            .itemOutput(RagiumItems.MAGICAL_REAGENT, 4)
            .saveSuffixed(output, "_from_crying")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Tags.Items.NETHER_STARS)
            .itemOutput(RagiumItems.MAGICAL_REAGENT, 64)
            .saveSuffixed(output, "_from_star")
    }

    private fun registerPrismarine(output: RecipeOutput) {
        // Prismarine Reagent
        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Tags.Items.GEMS_PRISMARINE)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT)
            .saveSuffixed(output, "_from_crystal")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.PRISMARINE_SHARD, 3)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 2)
            .saveSuffixed(output, "_from_shard")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.NAUTILUS_SHELL)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 4)
            .saveSuffixed(output, "_from_nautilus")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.HEART_OF_THE_SEA)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 64)
            .saveSuffixed(output, "_from_heart")

        // Sea Lantern
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.BUILDING_BLOCKS, Items.SEA_LANTERN)
            .requires(Tags.Items.GLASS_BLOCKS)
            .requires(RagiumItems.PRISMARINE_REAGENT)
            .requires(RagiumItems.PRISMARINE_REAGENT)
            .unlockedBy("has_reagent", has(RagiumItems.PRISMARINE_REAGENT))
            .savePrefixed(output)
        // Sponge
        HTShapedRecipeBuilder(Items.SPONGE)
            .hollow8()
            .define('A', RagiumItems.PRISMARINE_REAGENT)
            .define('B', ItemTags.WOOL)
            .save(output)
    }

    private fun registerSculk(output: RecipeOutput) {
        // Sculk Reagent
        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.SCULK_VEIN, 8)
            .itemOutput(RagiumItems.SCULK_REAGENT)
            .saveSuffixed(output, "_from_vein")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.SCULK)
            .itemOutput(RagiumItems.SCULK_REAGENT)
            .save(output)

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.SCULK_CATALYST)
            .itemOutput(RagiumItems.SCULK_REAGENT, 4)
            .saveSuffixed(output, "_from_catalyst")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.SCULK_SHRIEKER)
            .itemOutput(RagiumItems.SCULK_REAGENT, 16)
            .saveSuffixed(output, "_from_shrieker")

        // Echorium
        HTSingleItemRecipeBuilder
            .laser()
            .itemInput(RagiumItems.SCULK_REAGENT, 16)
            .itemOutput(Items.ECHO_SHARD)
            .save(output)

        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .itemInput(Items.ECHO_SHARD, 4)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.ECHORIUM)
            .save(output)
    }

    private fun registerWither(output: RecipeOutput) {
        // Wither Reagent
        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.WITHER_ROSE)
            .itemOutput(RagiumItems.WITHER_REAGENT, 4)
            .saveSuffixed(output, "_from_rose")

        HTFluidOutputRecipeBuilder
            .extractor()
            .itemInput(Items.WITHER_SKELETON_SKULL)
            .itemOutput(RagiumItems.WITHER_REAGENT, 8)
            .saveSuffixed(output, "_from_skull")

        // Defoliant
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.DEFOLIANT)
            .requires(Tags.Items.FERTILIZERS)
            .requires(RagiumItems.WITHER_REAGENT)
            .unlockedBy("has_reagent", has(RagiumItems.WITHER_REAGENT))
            .savePrefixed(output)
    }
}
