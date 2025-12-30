package hiiragi283.ragium.common.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

abstract class HTComplexRecipe(
    val ingredient: Either<HTItemIngredient, HTFluidIngredient>,
    val result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe(time, exp) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getRight()?.getStackOrEmpty(provider) ?: FluidStack.EMPTY

    final override fun matches(input: HTRecipeInput, level: Level): Boolean =
        ingredient.map({ input.testItem(0, it) }, { input.testFluid(0, it) })

    final override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackOrEmpty(registries) ?: ItemStack.EMPTY
}
