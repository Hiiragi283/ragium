package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipeCache
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

class HTFakeRecipeCache<I : RecipeInput, R : Recipe<I>> : HTRecipeCache<I, R> {
    override fun getFirstHolder(input: I, level: Level): RecipeHolder<R>? = null
}
