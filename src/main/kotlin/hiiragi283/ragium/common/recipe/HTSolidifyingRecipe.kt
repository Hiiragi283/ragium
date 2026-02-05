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

class HTSolidifyingRecipe(
    ingredient: HTFluidIngredient,
    catalyst: HTItemIngredient,
    result: HTItemResult,
    parameters: SubParameters,
) : HTFluidWithItemRecipe(ingredient, catalyst, result, parameters) {
    override fun matches(input: HTItemAndFluidRecipeInput, level: Level): Boolean =
        fluidIngredient.test(input.fluid) && itemIngredient.testOnlyType(input.item)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SOLIDIFYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SOLIDIFYING.get()
}
