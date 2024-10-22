package hiiragi283.ragium.api.recipe

import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.util.Identifier
import net.minecraft.world.World
import java.util.*

class HTRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) : RecipeManager.MatchGetter<I, R> {
    private var id: Identifier? = null

    override fun getFirstMatch(input: I, world: World): Optional<RecipeEntry<R>> = world.recipeManager
        .getFirstMatch(recipeType, input, world, id)
        .flatMap {
            this.id = it.id
            Optional.of(it)
        }.or {
            this.id = null
            Optional.empty()
        }
}
