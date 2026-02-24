package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

abstract class HTBasicItemOrFluidRecipe(
    val ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
    val result: Ior<HTItemResult, HTFluidResult>,
    final override val time: Int,
) : HTItemOrFluidRecipe.Serializable {
    final override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> =
        ingredient.mapLeft { Predicate(it::test) }.mapRight { Predicate(it::test) }

    final override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<Int, Int> =
        ingredient.mapLeft(HTItemIngredient::amount).mapRight(HTFluidIngredient::amount)

    final override fun assembleFluid(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        result.getRight()?.getStackResult(registries)?.value() ?: FluidStack.EMPTY

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackResult(registries)?.value() ?: ItemStack.EMPTY
}
