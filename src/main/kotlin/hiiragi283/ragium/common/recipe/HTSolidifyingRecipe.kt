package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTFluidWithItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

class HTSolidifyingRecipe(
    ingredient: HTFluidIngredient,
    catalyst: HTItemIngredient,
    result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTFluidWithItemRecipe(ingredient, catalyst, result, time, exp) {
    override fun matches(input: HTViewRecipeInput, level: Level): Boolean =
        fluidIngredient.test(input.getFluidView(0)) && itemIngredient.testOnlyType(input.getItemView(0))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()
}
