package hiiragi283.ragium.common.recipe.manager

import hiiragi283.ragium.api.recipe.HTRecipeCache
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

class VanillaRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val recipeType: RecipeType<RECIPE>) :
    HTRecipeCache<INPUT, RECIPE> {
    private var lastRecipe: ResourceLocation? = null

    override fun getFirstHolder(input: INPUT, level: Level): RecipeHolder<RECIPE>? = level.recipeManager
        .getRecipeFor(recipeType, input, level, lastRecipe)
        .getOrNull()
        .also { holder: RecipeHolder<RECIPE>? -> lastRecipe = holder?.id }
}
