package hiiragi283.ragium.common.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.wrapOptional
import net.minecraft.resources.ResourceLocation

class HTRockGeneratingRecipeBuilder(
    private val left: HTFluidIngredient,
    private val right: Either<HTItemIngredient, HTFluidIngredient>,
    private val bottom: HTItemIngredient?,
    private val result: HTItemResult,
) : HTRecipeBuilder<HTRockGeneratingRecipeBuilder>(RagiumConst.ROCK_GENERATING) {
    companion object {
        @JvmStatic
        fun create(
            left: HTFluidIngredient,
            right: HTFluidIngredient,
            result: HTItemResult,
            bottom: HTItemIngredient? = null,
        ): HTRockGeneratingRecipeBuilder = HTRockGeneratingRecipeBuilder(left, Either.right(right), bottom, result)

        @JvmStatic
        fun create(
            left: HTFluidIngredient,
            right: HTItemIngredient,
            result: HTItemResult,
            bottom: HTItemIngredient? = null,
        ): HTRockGeneratingRecipeBuilder = HTRockGeneratingRecipeBuilder(left, Either.left(right), bottom, result)
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): HTRockGeneratingRecipe = HTRockGeneratingRecipe(
        left,
        right,
        bottom.wrapOptional(),
        result,
    )
}
