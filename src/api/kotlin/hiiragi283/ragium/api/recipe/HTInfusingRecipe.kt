package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTInfusingRecipe : Recipe<SingleRecipeInput> {
    val cost: Float

    override fun getType(): RecipeType<*> = RagiumAPI.getInstance().getInfusingRecipeType()
}
