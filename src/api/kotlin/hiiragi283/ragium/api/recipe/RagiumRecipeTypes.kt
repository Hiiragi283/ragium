package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.chance.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.multi.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.multi.HTMultiItemsToItemRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleInputFluidRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleInputRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    // Machine
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTMultiRecipeInput, HTMultiItemsToItemRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<SingleRecipeInput, HTSingleInputRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTMultiRecipeInput, HTMultiItemsToItemRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val FLUID_TRANSFORM: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTFluidTransformRecipe> = create(RagiumConst.FLUID_TRANSFORM)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTSingleInputFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.MIXING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> = create(RagiumConst.PLANTING)

    @JvmField
    val SIMULATING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.SIMULATING)

    @JvmField
    val WASHING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe> = create(RagiumConst.WASHING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(path: String): HTDeferredRecipeType<INPUT, RECIPE> =
        HTDeferredRecipeType(RagiumAPI.id(path))
}
