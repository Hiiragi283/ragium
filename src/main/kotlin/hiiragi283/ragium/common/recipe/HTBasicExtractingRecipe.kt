package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.fluid.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.recipe.base.HTBasicComplexOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer

class HTBasicExtractingRecipe(val ingredient: HTItemIngredient, results: HTComplexResult) :
    HTBasicComplexOutputRecipe(results),
    HTExtractingRecipe {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING

    override fun test(stack: ImmutableItemStack): Boolean = ingredient.test(stack)

    override fun getRequiredCount(): Int = ingredient.getRequiredAmount()
}
