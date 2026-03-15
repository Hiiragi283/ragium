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
    override fun test(input: HTItemAndFluidRecipeInput): Boolean {
        val (item: ItemStack, fluid: FluidStack) = input
        return getPredicate().fold(
            { it.test(item) && fluid.isEmpty },
            { it.test(fluid) && item.isEmpty },
            { itemPre: Predicate<ItemStack>, fluidPre: Predicate<FluidStack> -> itemPre.test(item) && fluidPre.test(fluid) },
        )
    }

    fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>>

    fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount>

    //    Serializable    //

    interface Serializable :
        HTItemOrFluidRecipe,
        HTSerializableRecipe<HTItemAndFluidRecipeInput>
}
