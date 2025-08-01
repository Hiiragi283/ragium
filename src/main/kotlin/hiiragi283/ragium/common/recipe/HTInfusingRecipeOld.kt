package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTInfusingRecipeOld(val ingredient: Ingredient, val result: HTItemResult, val cost: Float) : HTUniversalRecipe {
    override fun matches(input: HTUniversalRecipeInput, level: Level): Boolean = ingredient.test(input.getItem(0))

    override fun getType(): RecipeType<*> = TODO()

    override fun getSerializer(): RecipeSerializer<*> = TODO()
}
