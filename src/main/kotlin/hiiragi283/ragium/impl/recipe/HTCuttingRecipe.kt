package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCuttingRecipe(ingredient: HTItemIngredient, results: List<HTItemResultWithChance>) :
    HTItemToChancedItemRecipeBase(ingredient, results) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CUTTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CUTTING.get()
}
