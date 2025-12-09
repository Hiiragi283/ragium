package hiiragi283.ragium.client.integration.emi.category

import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.extra.HTSingleExtraItemRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleFluidRecipe
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike

object RagiumEmiRecipeCategories {
    @JvmField
    val MACHINE_BOUNDS = HTBounds(0, 0, 7 * 18, 3 * 18)

    @JvmField
    val MACHINE_UPGRADE: HTEmiRecipeCategory = HTEmiRecipeCategory.create(
        MACHINE_BOUNDS,
        RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY),
        RagiumCommonTranslation.EMI_MACHINE_UPGRADE::translate,
    )

    //    Generators    //

    @JvmField
    val GENERATOR_BOUNDS = HTBounds(0, 0, 7 * 18, 1 * 18)

    @JvmStatic
    private fun <ITEM> generator(item: ITEM): HTEmiRecipeCategory where ITEM : HTItemHolderLike, ITEM : HTHasText =
        HTEmiRecipeCategory.create(GENERATOR_BOUNDS, item)

    // Basic
    @JvmField
    val THERMAL: HTEmiRecipeCategory = generator(RagiumBlocks.THERMAL_GENERATOR)

    // Advanced
    @JvmField
    val CULINARY: HTEmiRecipeCategory = generator(RagiumBlocks.CULINARY_GENERATOR)

    @JvmField
    val MAGMATIC: HTEmiRecipeCategory = generator(RagiumBlocks.MAGMATIC_GENERATOR)

    // Elite
    @JvmField
    val BIOMASS: HTEmiRecipeCategory =
        HTEmiRecipeCategory.create(HTBounds(0, 0, 5 * 18, 1 * 18), RagiumFluidContents.CRUDE_BIO.bucket)

    @JvmField
    val COOLANT: HTEmiRecipeCategory = generator(RagiumBlocks.COMBUSTION_GENERATOR)

    @JvmField
    val COMBUSTION: HTEmiRecipeCategory = generator(RagiumBlocks.COMBUSTION_GENERATOR)

    //    Machines    //

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> machine(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        vararg workStations: ItemLike,
    ): HTRegistryEmiRecipeCategory<INPUT, RECIPE> = HTRegistryEmiRecipeCategory(MACHINE_BOUNDS, recipeType, *workStations)

    // Basic
    @JvmField
    val ALLOYING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTShapelessInputsRecipe> =
        machine(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTItemWithCatalystRecipe> =
        machine(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTSingleExtraItemRecipe> =
        machine(RagiumRecipeTypes.CRUSHING, RagiumBlocks.PULVERIZER, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTSingleExtraItemRecipe> =
        machine(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val EXTRACTING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTItemWithCatalystRecipe> =
        machine(RagiumRecipeTypes.EXTRACTING, RagiumBlocks.EXTRACTOR)

    // Advanced
    @JvmField
    val MELTING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTSingleFluidRecipe> =
        machine(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val MIXING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTComplexRecipe> =
        machine(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER, RagiumBlocks.ADVANCED_MIXER)

    @JvmField
    val REFINING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTComplexRecipe> =
        machine(RagiumRecipeTypes.REFINING, RagiumBlocks.REFINERY)

    // Elite
    @JvmField
    val BREWING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTCombineRecipe> =
        machine(RagiumRecipeTypes.BREWING, RagiumBlocks.BREWERY)

    @JvmField
    val PLANTING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTPlantingRecipe> =
        machine(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)

    // Ultimate
    @JvmField
    val ENCHANTING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTCombineRecipe> =
        machine(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)

    @JvmField
    val SIMULATING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTItemWithCatalystRecipe> =
        machine(RagiumRecipeTypes.SIMULATING, RagiumBlocks.SIMULATOR)

    //    Device    //

    @JvmField
    val ROCK_GENERATING: HTRegistryEmiRecipeCategory<HTRecipeInput, HTRockGeneratingRecipe> =
        machine(RagiumRecipeTypes.ROCK_GENERATING, RagiumBlocks.STONE_COLLECTOR)
}
