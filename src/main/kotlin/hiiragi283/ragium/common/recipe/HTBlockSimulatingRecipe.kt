package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderSet
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import org.apache.commons.lang3.math.Fraction
import java.util.*

class HTBlockSimulatingRecipe(
    ingredient: Optional<HTItemIngredient>,
    catalyst: HolderSet<Block>,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTSimulatingRecipe<HolderSet<Block>>(ingredient, catalyst, result, time, exp) {
    override fun testCatalyst(input: Input, level: Level): Boolean = level.getBlockState(input.pos.below()).`is`(catalyst)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMULATING_BLOCK
}
