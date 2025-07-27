package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

interface HTInfusingRecipe : Recipe<HTInfusingRecipeInput> {
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun getType(): RecipeType<*> = RagiumAPI.getInstance().getInfusingRecipeType()
}
