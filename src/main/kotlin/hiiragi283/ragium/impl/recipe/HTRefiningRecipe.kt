package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.base.HTFluidTransformRecipeBase
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.Optional

class HTRefiningRecipe(
    override val fluidIngredient: HTFluidIngredient,
    override val itemIngredient: Optional<HTItemIngredient>,
    override val itemResult: Optional<HTItemResult>,
    override val fluidResult: Optional<HTFluidResult>,
) : HTFluidTransformRecipeBase() {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.FLUID_TRANSFORM
}
