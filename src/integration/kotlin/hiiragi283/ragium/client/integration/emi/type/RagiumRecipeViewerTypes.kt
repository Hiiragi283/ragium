package hiiragi283.ragium.client.integration.emi.type

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.HTSingleInputFluidRecipe
import hiiragi283.ragium.api.recipe.HTSingleInputRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.client.integration.emi.data.HTEmiBrewingEffect
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeViewerTypes {
    @JvmField
    val MACHINE_BOUNDS = HTBounds(HTSlotHelper.getSlotPosX(1) - 1, HTSlotHelper.getSlotPosY(0) - 1, 7 * 18, 3 * 18)

    //    Generators    //

    @JvmStatic
    private fun generator(block: HTDeferredBlock<*, *>): HTFakeRecipeViewerType<HTEmiFluidFuelData> =
        HTFakeRecipeViewerType.create(block, MACHINE_BOUNDS)

    @JvmField
    val THERMAL: HTFakeRecipeViewerType<HTEmiFluidFuelData> = generator(RagiumBlocks.THERMAL_GENERATOR)

    @JvmField
    val COMBUSTION: HTFakeRecipeViewerType<HTEmiFluidFuelData> = generator(RagiumBlocks.COMBUSTION_GENERATOR)

    //    Machines    //

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> machine(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        vararg variants: HTItemHolderLike,
    ): HTRegistryRecipeViewerType<INPUT, RECIPE> =
        HTRegistryRecipeViewerType(recipeType, variants[0].toStack(), null, MACHINE_BOUNDS, *variants)

    // Basic
    @JvmField
    val ALLOYING: HTRegistryRecipeViewerType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> =
        machine(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING: HTRegistryRecipeViewerType<SingleRecipeInput, HTSingleInputRecipe> =
        machine(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTRegistryRecipeViewerType<SingleRecipeInput, HTItemToChancedItemRecipe> =
        machine(RagiumRecipeTypes.CRUSHING, RagiumBlocks.PULVERIZER, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTRegistryRecipeViewerType<SingleRecipeInput, HTItemToChancedItemRecipe> =
        machine(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val EXTRACTING: HTRegistryRecipeViewerType<SingleRecipeInput, HTSingleInputRecipe> =
        machine(RagiumRecipeTypes.EXTRACTING, RagiumBlocks.EXTRACTOR)

    // Advanced
    @JvmField
    val FLUID_TRANSFORM: HTRegistryRecipeViewerType<HTItemWithFluidRecipeInput, HTFluidTransformRecipe> =
        machine(RagiumRecipeTypes.FLUID_TRANSFORM, RagiumBlocks.REFINERY)

    @JvmField
    val MELTING: HTRegistryRecipeViewerType<SingleRecipeInput, HTSingleInputFluidRecipe> =
        machine(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val WASHING: HTRegistryRecipeViewerType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> =
        machine(RagiumRecipeTypes.WASHING, RagiumBlocks.WASHER)

    // Elite
    @JvmField
    val BREWING: HTFakeRecipeViewerType<HTEmiBrewingEffect> = HTFakeRecipeViewerType.create(
        RagiumBlocks.BREWERY,
        HTBounds(
            0,
            0,
            4 * 18,
            1 * 18,
        ),
    )

    @JvmField
    val PLANTING: HTRegistryRecipeViewerType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> =
        machine(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)

    @JvmField
    val SIMULATING: HTRegistryRecipeViewerType<HTMultiItemRecipeInput, HTItemWithCatalystToItemRecipe> =
        machine(RagiumRecipeTypes.SIMULATING, RagiumBlocks.SIMULATOR)
}
