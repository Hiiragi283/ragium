package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.base.HTBasicItemToChancedItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCrushingRecipe(ingredient: HTItemIngredient, results: List<HTItemResultWithChance>) :
    HTBasicItemToChancedItemRecipe(ingredient, results) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
