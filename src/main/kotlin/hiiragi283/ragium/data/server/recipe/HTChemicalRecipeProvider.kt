package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTExtractorRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTInfuserRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTChemicalRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerAlkali(output)
        registerBlaze(output)
        registerCreeper(output)
        registerDeepant(output)
        registerEnder(output)
        registerGlow(output)
        registerMagical(output)
        registerPrismarine(output)
        registerSculk(output)
        registerWither(output)

        registerSlag(output)

        registerUranium(output)
    }

    private fun registerAlkali(output: RecipeOutput) {
        // Ash -> Alkali
        HTExtractorRecipeBuilder()
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
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.ALKALI_REAGENT)
            .fluidInput(RagiumVirtualFluids.PLANT_OIL, FluidType.BUCKET_VOLUME * 4)
            .itemOutput(RagiumItems.SOAP)
            .save(output)
    }

    private fun registerBlaze(output: RecipeOutput) {
        // Blaze Rod -? 4x Blaze Powder
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Tags.Items.RODS_BLAZE)
            .itemOutput(Items.BLAZE_POWDER, 4)
            .save(output)

        // Blaze Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.MAGMA_BLOCK, 8)
            .itemOutput(RagiumItems.BLAZE_REAGENT)
            .saveSuffixed(output, "_from_magma")

        HTExtractorRecipeBuilder()
            .itemInput(Items.BLAZE_POWDER)
            .itemOutput(RagiumItems.BLAZE_REAGENT)
            .saveSuffixed(output, "_from_powder")

        // Blaze Reagent -> Blaze Acid
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.BLAZE_REAGENT)
            .waterInput()
            .fluidOutput(RagiumVirtualFluids.SULFURIC_ACID)
            .save(output)
        // Fire charge
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, Items.FIRE_CHARGE, 3)
            .requires(Tags.Items.GUNPOWDERS)
            .requires(ItemTags.COALS)
            .requires(RagiumItems.BLAZE_REAGENT)
            .unlockedBy("has_reagent", has(RagiumItems.BLAZE_REAGENT))
            .savePrefixed(output)
        // Fiery Coal
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.BLAZE_REAGENT, 8)
            .fluidInput(RagiumFluids.CRUDE_OIL)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL)
            .save(output)
    }

    private fun registerCreeper(output: RecipeOutput) {
        // Creeper Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.GUNPOWDERS)
            .itemOutput(RagiumItems.CREEPER_REAGENT)
            .saveSuffixed(output, "_from_powder")
        // Nitration
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.CREEPER_REAGENT)
            .fluidInput(RagiumFluidTags.NON_NITRO_FUEL, FluidType.BUCKET_VOLUME * 8)
            .fluidOutput(RagiumVirtualFluids.NITRO_FUEL, FluidType.BUCKET_VOLUME * 8)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Tags.Items.STRINGS, 4)
            .itemInput(Items.PAPER, 4)
            .itemInput(RagiumItems.CREEPER_REAGENT)
            .itemOutput(RagiumItems.DYNAMITE, 8)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Tags.Items.SANDS, 4)
            .itemInput(RagiumItems.CREEPER_REAGENT)
            .itemOutput(Items.TNT, 4)
            .save(output)
    }

    private fun registerDeepant(output: RecipeOutput) {
        // Deep Reagent
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Tags.Items.COBBLESTONES_DEEPSLATE, 16)
            .itemOutput(RagiumItems.DEEPANT_REAGENT)
            .save(output)

        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.COBBLESTONES_DEEPSLATE, 4)
            .itemOutput(RagiumItems.DEEPANT_REAGENT)
            .save(output)
    }

    private fun registerEnder(output: RecipeOutput) {
        // Ender Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.ENDER_PEARLS)
            .itemOutput(RagiumItems.ENDER_REAGENT)
            .saveSuffixed(output, "_from_pearl")

        HTExtractorRecipeBuilder()
            .itemInput(Items.ENDER_EYE)
            .itemOutput(RagiumItems.ENDER_REAGENT, 2)
            .saveSuffixed(output, "_from_eye")

        HTExtractorRecipeBuilder()
            .itemInput(Items.END_CRYSTAL)
            .itemOutput(RagiumItems.ENDER_REAGENT, 4)
            .saveSuffixed(output, "_from_crystal")
    }

    private fun registerGlow(output: RecipeOutput) {
        // Glowstone -> 4x Glowstone Dust
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Items.GLOWSTONE)
            .itemOutput(Items.GLOWSTONE_DUST, 4)
            .save(output)

        // Glow Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.GLOW_LICHEN, 8)
            .itemOutput(RagiumItems.GLOW_REAGENT)
            .saveSuffixed(output, "_from_lichen")

        HTExtractorRecipeBuilder()
            .itemInput(Items.GLOW_BERRIES, 4)
            .itemOutput(RagiumItems.GLOW_REAGENT)
            .saveSuffixed(output, "_from_berry")

        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .itemOutput(RagiumItems.GLOW_REAGENT)
            .saveSuffixed(output, "_from_dust")

        HTExtractorRecipeBuilder()
            .itemInput(Items.GLOW_INK_SAC)
            .itemOutput(RagiumItems.GLOW_REAGENT, 2)
            .saveSuffixed(output, "_from_ink")

        // CaF2 + H2SO4 -> CaSO4 + 2x HF(aq)
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.GEM, CommonMaterials.FLUORITE)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID)
            .fluidOutput(RagiumVirtualFluids.HYDROFLUORIC_ACID)
            .saveSuffixed(output, "_from_fluorite")

        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.GLOW_REAGENT, 2)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID)
            .fluidOutput(RagiumVirtualFluids.HYDROFLUORIC_ACID)
            .saveSuffixed(output, "_from_reagent")
    }

    private fun registerMagical(output: RecipeOutput) {
        // Magical Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.GEMS_AMETHYST)
            .itemOutput(RagiumItems.MAGNET)
            .saveSuffixed(output, "_from_amethyst")

        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.OBSIDIANS_CRYING)
            .itemOutput(RagiumItems.MAGNET, 4)
            .saveSuffixed(output, "_from_crying")

        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.NETHER_STARS)
            .itemOutput(RagiumItems.MAGNET, 64)
            .saveSuffixed(output, "_from_star")
    }

    private fun registerPrismarine(output: RecipeOutput) {
        // Prismarine Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.GEMS_PRISMARINE)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT)
            .saveSuffixed(output, "_from_crystal")

        HTExtractorRecipeBuilder()
            .itemInput(Items.PRISMARINE_SHARD, 3)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 2)
            .saveSuffixed(output, "_from_shard")

        HTExtractorRecipeBuilder()
            .itemInput(Items.NAUTILUS_SHELL)
            .itemOutput(RagiumItems.PRISMARINE_REAGENT, 4)
            .saveSuffixed(output, "_from_nautilus")

        HTExtractorRecipeBuilder()
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
        ShapedRecipeBuilder
            .shaped(RecipeCategory.BUILDING_BLOCKS, Items.SPONGE)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', RagiumItems.PRISMARINE_REAGENT)
            .define('B', ItemTags.WOOL)
            .unlockedBy("has_reagent", has(RagiumItems.PRISMARINE_REAGENT))
            .savePrefixed(output)
    }

    private fun registerSculk(output: RecipeOutput) {
        // Sculk Reagent
        HTExtractorRecipeBuilder()
            .itemInput(Items.SCULK_VEIN, 8)
            .itemOutput(RagiumItems.SCULK_REAGENT)
            .saveSuffixed(output, "_from_vein")

        HTExtractorRecipeBuilder()
            .itemInput(Items.SCULK)
            .itemOutput(RagiumItems.SCULK_REAGENT)
            .save(output)

        HTExtractorRecipeBuilder()
            .itemInput(Items.SCULK_CATALYST)
            .itemOutput(RagiumItems.SCULK_REAGENT, 4)
            .saveSuffixed(output, "_from_catalyst")

        HTExtractorRecipeBuilder()
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
        HTExtractorRecipeBuilder()
            .itemInput(Items.WITHER_ROSE)
            .itemOutput(RagiumItems.WITHER_REAGENT, 4)
            .saveSuffixed(output, "_from_rose")

        HTExtractorRecipeBuilder()
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

    private fun registerSlag(output: RecipeOutput) {
        // Slag -> Gravel
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(RagiumItemTags.SLAG)
            .itemOutput(Items.GRAVEL)
            .saveSuffixed(output, "_from_slag")
    }

    private fun registerUranium(output: RecipeOutput) {
        // Poisonous Potato + H2SO4 -> Yellow Cake
        HTInfuserRecipeBuilder()
            .itemInput(Items.POISONOUS_POTATO, 8)
            .fluidInput(RagiumVirtualFluids.SULFURIC_ACID, FluidType.BUCKET_VOLUME * 8)
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
