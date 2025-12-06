package hiiragi283.ragium.client.integration.emi.type

import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.math.HTBounds
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.multi.HTItemToExtraItemRecipe
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleFluidRecipe
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * @see mekanism.client.recipe_viewer.type.RecipeViewerRecipeType
 */
object RagiumRecipeViewerTypes {
    @JvmField
    val MACHINE_BOUNDS = HTBounds(HTSlotHelper.getSlotPosX(1) - 1, HTSlotHelper.getSlotPosY(0) - 1, 7 * 18, 3 * 18)

    @JvmField
    val MACHINE_UPGRADE: HTFakeRecipeViewerType<EmiStack> = HTFakeRecipeViewerType.create(
        RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY),
        RagiumCommonTranslation.EMI_MACHINE_UPGRADE::translate,
        MACHINE_BOUNDS,
    )

    //    Generators    //

    @JvmStatic
    private fun <RECIPE : Any> generator(block: HTDeferredBlock<*, *>): HTFakeRecipeViewerType<RECIPE> =
        HTFakeRecipeViewerType.create(block, MACHINE_BOUNDS)

    @JvmField
    val THERMAL: HTFakeRecipeViewerType<HTEmiFluidFuelData> = generator(RagiumBlocks.THERMAL_GENERATOR)

    @JvmField
    val COOLANT: HTFakeRecipeViewerType<EmiStack> = generator(RagiumBlocks.COMBUSTION_GENERATOR)

    @JvmField
    val COMBUSTION: HTFakeRecipeViewerType<HTEmiFluidFuelData> = generator(RagiumBlocks.COMBUSTION_GENERATOR)

    @JvmField
    val CULINARY: HTFakeRecipeViewerType<HTEmiFluidFuelData> = generator(RagiumBlocks.CULINARY_GENERATOR)

    //    Machines    //

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> machine(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        vararg variants: HTItemHolderLike,
    ): HTRegistryRecipeViewerType<INPUT, RECIPE> =
        HTRegistryRecipeViewerType(recipeType, variants[0].toStack(), null, MACHINE_BOUNDS, *variants)

    // Basic
    @JvmField
    val ALLOYING: HTRegistryRecipeViewerType<HTMultiRecipeInput, HTShapelessInputsRecipe> =
        machine(RagiumRecipeTypes.ALLOYING, RagiumBlocks.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING: HTRegistryRecipeViewerType<HTDoubleRecipeInput, HTItemWithCatalystRecipe> =
        machine(RagiumRecipeTypes.COMPRESSING, RagiumBlocks.COMPRESSOR)

    @JvmField
    val CRUSHING: HTRegistryRecipeViewerType<SingleRecipeInput, HTItemToExtraItemRecipe> =
        machine(RagiumRecipeTypes.CRUSHING, RagiumBlocks.PULVERIZER, RagiumBlocks.CRUSHER)

    @JvmField
    val CUTTING: HTRegistryRecipeViewerType<SingleRecipeInput, HTItemToExtraItemRecipe> =
        machine(RagiumRecipeTypes.CUTTING, RagiumBlocks.CUTTING_MACHINE)

    @JvmField
    val EXTRACTING: HTRegistryRecipeViewerType<HTDoubleRecipeInput, HTItemWithCatalystRecipe> =
        machine(RagiumRecipeTypes.EXTRACTING, RagiumBlocks.EXTRACTOR)

    // Advanced
    @JvmField
    val MELTING: HTRegistryRecipeViewerType<SingleRecipeInput, HTSingleFluidRecipe> =
        machine(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER)

    @JvmField
    val MIXING: HTRegistryRecipeViewerType<HTMultiRecipeInput, HTComplexRecipe> =
        machine(RagiumRecipeTypes.MIXING, RagiumBlocks.MIXER, RagiumBlocks.ADVANCED_MIXER)

    @JvmField
    val REFINING: HTRegistryRecipeViewerType<HTMultiRecipeInput, HTComplexRecipe> =
        machine(RagiumRecipeTypes.REFINING, RagiumBlocks.REFINERY)

    // Elite
    @JvmField
    val BREWING: HTRegistryRecipeViewerType<HTMultiRecipeInput, HTCombineRecipe> =
        machine(RagiumRecipeTypes.BREWING, RagiumBlocks.BREWERY)

    @JvmField
    val PLANTING: HTRegistryRecipeViewerType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> =
        machine(RagiumRecipeTypes.PLANTING, RagiumBlocks.PLANTER)

    // Ultimate
    @JvmField
    val ENCHANTING: HTRegistryRecipeViewerType<HTMultiRecipeInput, HTCombineRecipe> =
        machine(RagiumRecipeTypes.ENCHANTING, RagiumBlocks.ENCHANTER)

    @JvmField
    val SIMULATING: HTRegistryRecipeViewerType<HTDoubleRecipeInput, HTItemWithCatalystRecipe> =
        machine(RagiumRecipeTypes.SIMULATING, RagiumBlocks.SIMULATOR)
}
