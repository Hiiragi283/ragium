package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.base.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.base.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.base.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.base.HTPressingRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

object RagiumRecipeTypesNew {
    @JvmField
    val ALLOYING: HTDeferredRecipeType<HTDoubleRecipeInput, HTAlloyingRecipe> = create(RagiumConst.ALLOYING)

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTCrushingRecipe> = create(RagiumConst.CRUSHING)

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<SingleRecipeInput, HTExtractingRecipe> = create(RagiumConst.EXTRACTING)

    @JvmField
    val PRESSING: HTDeferredRecipeType<HTDoubleRecipeInput, HTPressingRecipe> = create(RagiumConst.PRESSING)

    @JvmStatic
    private fun <I : RecipeInput, R : Recipe<I>> create(path: String): HTDeferredRecipeType<I, R> =
        HTDeferredRecipeType.createType(RagiumAPI.id(path))
}
