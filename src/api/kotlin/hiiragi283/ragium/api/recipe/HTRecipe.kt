package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level
import java.util.function.Predicate

/**
 * @see [mekanism.api.recipes.MekanismRecipe]
 */
interface HTRecipe<I : RecipeInput> :
    Recipe<I>,
    Predicate<I> {
    override fun test(input: I): Boolean

    override fun matches(input: I, level: Level): Boolean = test(input)

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun isSpecial(): Boolean = true

    abstract override fun isIncomplete(): Boolean
}
