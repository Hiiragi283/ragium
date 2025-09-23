package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMultiItemToPotionRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTBrewingRecipe(override val ingredients: List<HTItemIngredient>, override val potion: PotionContents) : HTMultiItemToPotionRecipe {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BREWING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BREWING.get()
}
