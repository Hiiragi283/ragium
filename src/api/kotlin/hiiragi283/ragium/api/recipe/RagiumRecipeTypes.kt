package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTFluidWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTDoubleRecipeInput, HTCombineItemToItemRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val INFUSING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToItemRecipe> = create(RagiumConst.INFUSING)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToFluidRecipe> = create(RagiumConst.MELTING)

    @JvmField
    val MIXING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToFluidRecipe> = create(RagiumConst.MIXING)

    @JvmField
    val PRESSING: HTDeferredRecipeType<HTDoubleRecipeInput, HTItemWithCatalystToItemRecipe> = create(RagiumConst.PRESSING)

    @JvmField
    val REFINING: HTDeferredRecipeType<HTSingleFluidRecipeInput, HTRefiningRecipe> = create(RagiumConst.REFINING)

    @JvmField
    val SOLIDIFYING: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTFluidWithCatalystToItemRecipe> = create(RagiumConst.SOLIDIFYING)

    @JvmStatic
    private fun <I : RecipeInput, R : Recipe<I>> create(path: String): HTDeferredRecipeType<I, R> =
        HTDeferredRecipeType.createType(RagiumAPI.id(path))
}
