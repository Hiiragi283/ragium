package hiiragi283.ragium.client.integration.emi.type

import hiiragi283.ragium.api.RagiumAPI
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
import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.client.integration.emi.data.HTEmiBrewingEffect
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleItemRecipe
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

    @JvmStatic
    fun getGenerator(variant: HTGeneratorVariant<*, *>): HTRecipeViewerType<HTEmiFluidFuelData> = when (variant) {
        HTGeneratorVariant.Thermal -> THERMAL
        HTGeneratorVariant.Combustion -> COMBUSTION
        else -> error("Unsupported variant: ${variant.variantName()}")
    }

    //    Machines    //

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> machine(
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        vararg variants: HTMachineVariant,
    ): HTRegistryRecipeViewerType<INPUT, RECIPE> =
        HTRegistryRecipeViewerType(recipeType, variants[0].toStack(), null, MACHINE_BOUNDS, *variants)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> machine(
        id: ResourceLocation,
        recipeType: HTRecipeType<INPUT, RECIPE>,
        variant: HTMachineVariant,
    ): HTRegistryRecipeViewerType<INPUT, RECIPE> =
        HTRegistryRecipeViewerType(id, recipeType, variant.toStack(), null, MACHINE_BOUNDS, variant)

    // Basic
    @JvmField
    val ALLOYING: HTRegistryRecipeViewerType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> =
        machine(RagiumRecipeTypes.ALLOYING, HTMachineVariant.ALLOY_SMELTER)

    @JvmField
    val COMPRESSING: HTRegistryRecipeViewerType<SingleRecipeInput, HTSingleInputRecipe> =
        machine(RagiumRecipeTypes.COMPRESSING, HTMachineVariant.COMPRESSOR)

    @JvmField
    val CRUSHING: HTRegistryRecipeViewerType<SingleRecipeInput, HTItemToChancedItemRecipe> =
        machine(RagiumRecipeTypes.CRUSHING, HTMachineVariant.PULVERIZER, HTMachineVariant.CRUSHER)

    @JvmField
    val CUTTING: HTRegistryRecipeViewerType<SingleRecipeInput, SingleItemRecipe> =
        machine(RagiumAPI.id("cutting"), RagiumRecipeTypes.CUTTING, HTMachineVariant.CUTTING_MACHINE)

    @JvmField
    val EXTRACTING: HTRegistryRecipeViewerType<SingleRecipeInput, HTSingleInputRecipe> =
        machine(RagiumRecipeTypes.EXTRACTING, HTMachineVariant.EXTRACTOR)

    // Advanced
    @JvmField
    val FLUID_TRANSFORM: HTRegistryRecipeViewerType<HTItemWithFluidRecipeInput, HTFluidTransformRecipe> =
        machine(RagiumRecipeTypes.FLUID_TRANSFORM, HTMachineVariant.REFINERY)

    @JvmField
    val MELTING: HTRegistryRecipeViewerType<SingleRecipeInput, HTSingleInputFluidRecipe> =
        machine(RagiumRecipeTypes.MELTING, HTMachineVariant.MELTER)

    @JvmField
    val WASHING: HTRegistryRecipeViewerType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> =
        machine(RagiumRecipeTypes.WASHING, HTMachineVariant.WASHER)

    // Elite
    @JvmField
    val BREWING: HTFakeRecipeViewerType<HTEmiBrewingEffect> = HTFakeRecipeViewerType.create(
        HTMachineVariant.BREWERY,
        HTBounds(
            0,
            0,
            4 * 18,
            1 * 18,
        ),
    )

    @JvmField
    val PLANTING: HTRegistryRecipeViewerType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> =
        machine(RagiumRecipeTypes.PLANTING, HTMachineVariant.PLANTER)

    @JvmField
    val SIMULATING: HTRegistryRecipeViewerType<HTMultiItemRecipeInput, HTItemWithCatalystToItemRecipe> =
        machine(RagiumRecipeTypes.SIMULATING, HTMachineVariant.SIMULATOR)
}
