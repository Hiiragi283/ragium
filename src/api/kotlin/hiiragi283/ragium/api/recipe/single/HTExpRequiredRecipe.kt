package hiiragi283.ragium.api.recipe.single

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeType

interface HTExpRequiredRecipe : HTSingleItemRecipe {
    fun getRequiredExpFluid(): Int
    
    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ENCHANTING.get()
}
