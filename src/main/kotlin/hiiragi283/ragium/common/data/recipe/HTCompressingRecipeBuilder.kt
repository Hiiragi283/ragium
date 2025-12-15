package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.recipe.HTBasicCompressingRecipe
import net.minecraft.resources.ResourceLocation

class HTCompressingRecipeBuilder(
    private val ingredient: HTItemIngredient,
    private val mold: HTMoldType,
    private val result: HTItemResult,
) : HTRecipeBuilder<HTCompressingRecipeBuilder>(RagiumConst.COMPRESSING) {
    companion object {
        @JvmStatic
        fun block(ingredient: HTItemIngredient, result: HTItemResult): HTCompressingRecipeBuilder =
            HTCompressingRecipeBuilder(ingredient, HTMoldType.STORAGE_BLOCK, result)

        @JvmStatic
        fun gem(ingredient: HTItemIngredient, result: HTItemResult): HTCompressingRecipeBuilder =
            HTCompressingRecipeBuilder(ingredient, HTMoldType.GEM, result)

        @JvmStatic
        fun gear(ingredient: HTItemIngredient, result: HTItemResult): HTCompressingRecipeBuilder =
            HTCompressingRecipeBuilder(ingredient, HTMoldType.GEAR, result)

        @JvmStatic
        fun ingot(ingredient: HTItemIngredient, result: HTItemResult): HTCompressingRecipeBuilder =
            HTCompressingRecipeBuilder(ingredient, HTMoldType.INGOT, result)

        @JvmStatic
        fun plate(ingredient: HTItemIngredient, result: HTItemResult): HTCompressingRecipeBuilder =
            HTCompressingRecipeBuilder(ingredient, HTMoldType.PLATE, result)

        @JvmStatic
        fun rod(ingredient: HTItemIngredient, result: HTItemResult): HTCompressingRecipeBuilder =
            HTCompressingRecipeBuilder(ingredient, HTMoldType.PLATE, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTBasicCompressingRecipe = HTBasicCompressingRecipe(ingredient, mold, result)
}
