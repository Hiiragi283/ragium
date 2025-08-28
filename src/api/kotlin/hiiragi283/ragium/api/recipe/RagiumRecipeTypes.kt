package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypes {
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val COMPRESSING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe> = create(RagiumConst.COMPRESSING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTItemToChancedItemRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val ENCHANTING: HTDeferredRecipeType<HTMultiItemRecipeInput, HTCombineItemToItemRecipe> = create(RagiumConst.ENCHANTING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToItemRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val FLUID_TRANSFORM: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTFluidTransformRecipe> = create(RagiumConst.FLUID_TRANSFORM)

    @JvmField
    val MELTING: HTDeferredRecipeType<SingleRecipeInput, HTItemToFluidRecipe> = create(RagiumConst.MELTING)

    @JvmStatic
    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> create(path: String): HTDeferredRecipeType<INPUT, RECIPE> =
        HTDeferredRecipeType.createType(RagiumAPI.id(path))
}
