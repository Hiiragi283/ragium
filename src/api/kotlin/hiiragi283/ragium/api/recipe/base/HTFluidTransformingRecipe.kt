package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

abstract class HTFluidTransformingRecipe(
    override val fluidIngredient: HTFluidIngredient,
    override val itemIngredient: Optional<HTItemIngredient>,
    override val itemResult: Optional<HTItemResult>,
    override val fluidResult: Optional<HTFluidResult>,
) : HTFluidTransformRecipe {
    final override fun getType(): RecipeType<*> = RagiumRecipeTypes.FLUID_TRANSFORM.get()
}
