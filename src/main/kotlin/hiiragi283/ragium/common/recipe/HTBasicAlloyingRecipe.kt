package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTBasicSingleOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTBasicAlloyingRecipe(override val ingredients: List<HTItemIngredient>, result: HTItemResult) :
    HTBasicSingleOutputRecipe(result),
    HTAlloyingRecipe {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = HTRecipeInput.hasMatchingSlots(ingredients, input.items)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()
}
