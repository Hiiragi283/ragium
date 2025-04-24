package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

interface HTDefinitionRecipe<T : RecipeInput> : Recipe<T> {
    fun getDefinition(): DataResult<HTRecipeDefinition>
}
