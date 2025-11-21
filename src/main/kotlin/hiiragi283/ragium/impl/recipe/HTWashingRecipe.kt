package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTWashingRecipe(ingredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, results: List<HTItemResultWithChance>) :
    HTBasicItemWithFluidToChancedItemRecipe(ingredient, fluidIngredient, results) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WASHING.get()
}
