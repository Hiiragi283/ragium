package hiiragi283.ragium.api.recipe.manager

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.Level

fun interface HTRecipeFinder<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> {
    fun getRecipeFor(
        manager: RecipeManager,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation?,
    ): RecipeHolder<RECIPE>?
}
