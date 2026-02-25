package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

typealias ItemAmount = Int
typealias FluidAmount = Int

interface HTItemOrFluidRecipe :
    HTProcessingRecipe<HTItemAndFluidRecipeInput>,
    HTFluidRecipe<HTItemAndFluidRecipeInput> {
    override fun test(input: HTItemAndFluidRecipeInput): Boolean = getPredicate().map(
        { it.test(input.item) },
        { it.test(input.fluid) },
        { matchItem: Boolean, matchFluid: Boolean -> matchItem && matchFluid },
    )

    fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>>

    fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount>

    //    Serializable    //

    interface Serializable :
        HTItemOrFluidRecipe,
        HTSerializableRecipe<HTItemAndFluidRecipeInput>
}
