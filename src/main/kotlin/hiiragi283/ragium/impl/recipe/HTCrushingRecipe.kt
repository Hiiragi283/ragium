package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTChancedItemRecipeBase
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTCrushingRecipe(val ingredient: HTItemIngredient, override val results: List<HTChancedItemRecipe.ChancedResult>) :
    HTChancedItemRecipeBase<SingleRecipeInput>(),
    HTItemToChancedItemRecipe {
    override fun getIngredientCount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    override fun isIncompleteIngredient(): Boolean = ingredient.hasNoMatchingStacks()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
