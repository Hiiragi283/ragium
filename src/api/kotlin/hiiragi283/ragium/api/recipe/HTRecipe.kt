package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level
import java.util.function.Predicate

/**
 * Ragiumで使用する[Recipe]の拡張インターフェース
 * @see [mekanism.api.recipes.MekanismRecipe]
 */
interface HTRecipe<INPUT : RecipeInput> :
    Recipe<INPUT>,
    Predicate<INPUT> {
    override fun test(input: INPUT): Boolean

    override fun matches(input: INPUT, level: Level): Boolean = test(input)

    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun isSpecial(): Boolean = true

    abstract override fun isIncomplete(): Boolean
}
