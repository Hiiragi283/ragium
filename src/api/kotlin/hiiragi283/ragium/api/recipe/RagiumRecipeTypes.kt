package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.extra.HTSingleExtraItemRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.recipe.single.HTSingleFluidRecipe
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    // Machine
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTMultiRecipeInput, HTShapelessInputsRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val BREWING: HTDeferredRecipeType<HTMultiRecipeInput, HTCombineRecipe> = create(RagiumConst.BREWING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemWithCatalystRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTSingleExtraItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTDeferredRecipeType<SingleRecipeInput, HTSingleExtraItemRecipe> = create(RagiumConst.CUTTING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTMultiRecipeInput, HTCombineRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemWithCatalystRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTSingleFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.MIXING)

    @JvmField
    val PLANTING: HTDeferredRecipeType<HTMultiRecipeInput, HTPlantingRecipe> = create(RagiumConst.PLANTING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTMultiRecipeInput, HTComplexRecipe> = create(RagiumConst.REFINING)

    @JvmField
    val SIMULATING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemWithCatalystRecipe> = create(RagiumConst.SIMULATING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(path: String): HTDeferredRecipeType<INPUT, RECIPE> =
        HTDeferredRecipeType(RagiumAPI.id(path))
}
