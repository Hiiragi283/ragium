package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTFluidWithItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

class HTBathingRecipe(
    fluidIngredient: HTFluidIngredient,
    itemIngredient: HTItemIngredient,
    result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTFluidWithItemRecipe(fluidIngredient, itemIngredient, result, time, exp) {
    override fun matches(input: HTItemAndFluidRecipeInput, level: Level): Boolean =
        fluidIngredient.test(input.fluid) && itemIngredient.test(input.item)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BATHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BATHING.get()
}
