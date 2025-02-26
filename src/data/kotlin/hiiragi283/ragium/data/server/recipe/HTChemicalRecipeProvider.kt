package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
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
        registerGlow(output)
        registerMagical(output)
        registerPrismarine(output)
        registerWither(output)
    }

    private fun registerGlow(output: RecipeOutput) {
        // Glowstone -> 4x Glowstone Dust
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Items.GLOWSTONE)
            .itemOutput(Items.GLOWSTONE_DUST, 4)
            .save(output)

        // Glow Reagent
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.GLOW_LICHEN, 8)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW)
            .saveSuffixed(output, "_from_lichen")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.GLOW_BERRIES, 4)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW)
            .saveSuffixed(output, "_from_berry")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW)
            .saveSuffixed(output, "_from_dust")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.GLOW_INK_SAC)
            .fluidOutput(RagiumVirtualFluids.LIQUID_GLOW, 2)
            .saveSuffixed(output, "_from_ink")

        // CaF2 + H2SO4 -> CaSO4 + 2x HF(aq)
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag)
            .fluidOutput(RagiumVirtualFluids.HYDROFLUORIC_ACID)
            .save(output)
    }

    private fun registerMagical(output: RecipeOutput) {
        // Magical Reagent
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.GEMS_AMETHYST)
            .itemOutput(RagiumItems.MAGICAL_REAGENT)
            .saveSuffixed(output, "_from_amethyst")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.OBSIDIANS_CRYING)
            .itemOutput(RagiumItems.MAGICAL_REAGENT, 4)
            .saveSuffixed(output, "_from_crying")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.NETHER_STARS)
            .itemOutput(RagiumItems.MAGICAL_REAGENT, 64)
            .saveSuffixed(output, "_from_star")
    }

    private fun registerPrismarine(output: RecipeOutput) {
        // Prismarine Reagent
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.GEMS_PRISMARINE)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT)
            .saveSuffixed(output, "_from_crystal")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.PRISMARINE_SHARD, 3)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 2)
            .saveSuffixed(output, "_from_shard")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.NAUTILUS_SHELL)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 4)
            .saveSuffixed(output, "_from_nautilus")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
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

    private fun registerWither(output: RecipeOutput) {
        // Wither Reagent
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.WITHER_ROSE)
            .itemOutput(RagiumItems.WITHER_REAGENT, 4)
            .saveSuffixed(output, "_from_rose")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
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
