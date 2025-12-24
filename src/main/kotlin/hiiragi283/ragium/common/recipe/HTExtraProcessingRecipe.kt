package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTExtraOutputRecipe
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

abstract class HTExtraProcessingRecipe(
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
    val extra: Optional<HTItemResult>,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp),
    HTExtraOutputRecipe {
    final override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testItem(0, ingredient)

    final override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        result.getStackOrNull(provider)

    final override fun assembleExtra(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        extra.flatMap { it.getOptionalResult(provider) }.getOrNull()
}
