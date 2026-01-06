package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

abstract class HTSimulatingRecipe<T : Any>(
    val ingredient: Optional<HTItemIngredient>,
    val catalyst: T,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTComplexResultRecipe<HTSimulatingRecipe.Input>(result, time, exp) {
    final override fun matches(input: Input, level: Level): Boolean {
        val bool1: Boolean = testCatalyst(input, level)
        val bool2: Boolean = ingredient.map { it.test(input.item) }.orElse(input.item.isEmpty)
        return bool1 && bool2
    }

    protected abstract fun testCatalyst(input: Input, level: Level): Boolean

    final override fun getType(): RecipeType<*> = RagiumRecipeTypes.SIMULATING.get()

    @JvmRecord
    data class Input(val item: ItemStack, val pos: BlockPos) : RecipeInput {
        override fun getItem(index: Int): ItemStack = item

        override fun size(): Int = 1

        override fun isEmpty(): Boolean = false
    }
}
