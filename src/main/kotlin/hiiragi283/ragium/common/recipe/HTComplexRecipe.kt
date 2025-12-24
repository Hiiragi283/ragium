package hiiragi283.ragium.common.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.recipe.HTFluidOutputRecipe
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

abstract class HTComplexRecipe(
    val ingredient: Either<HTItemIngredient, HTFluidIngredient>,
    val result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp),
    HTFluidOutputRecipe {
    final override fun matches(input: HTRecipeInput, level: Level): Boolean =
        ingredient.map({ input.testItem(0, it) }, { input.testFluid(0, it) })

    final override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        result.getLeft()?.getStackOrNull(provider)

    final override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        result.getRight()?.getStackOrNull(provider)
}
