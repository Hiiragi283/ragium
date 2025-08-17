package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.storage.HTContentListener
import net.minecraft.nbt.CompoundTag
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.Optional

interface HTFluidTank :
    IFluidTank,
    INBTSerializable<CompoundTag>,
    HTContentListener {
    fun isEmpty(): Boolean

    fun getSpace(): Int

    fun setFluid(stack: FluidStack)

    fun fill(stack: FluidStack, simulate: Boolean): Int = fill(stack, toAction(simulate))

    fun drain(stack: FluidStack, simulate: Boolean): FluidStack = drain(stack, toAction(simulate))

    fun drain(max: Int, simulate: Boolean): FluidStack = drain(max, toAction(simulate))

    private fun toAction(simulate: Boolean): IFluidHandler.FluidAction = when (simulate) {
        true -> IFluidHandler.FluidAction.SIMULATE
        false -> IFluidHandler.FluidAction.EXECUTE
    }

    fun canFill(resource: FluidStack, forceFull: Boolean): Boolean {
        val filled: Int = fill(resource, IFluidHandler.FluidAction.SIMULATE)
        return if (forceFull) filled == resource.amount else filled > 0
    }

    fun canDrain(maxDrain: Int, forceMax: Boolean): Boolean {
        val drained: FluidStack = drain(fluid.copyWithAmount(maxDrain), IFluidHandler.FluidAction.SIMULATE)
        return if (forceMax) drained.amount == maxDrain else !drained.isEmpty
    }

    fun drain(ingredient: Optional<HTFluidIngredient>, simulate: Boolean): FluidStack =
        ingredient.map { ingredient1: HTFluidIngredient -> drain(ingredient1, simulate) }.orElse(FluidStack.EMPTY)

    fun drain(ingredient: HTFluidIngredient, simulate: Boolean): FluidStack = drain(ingredient.getRequiredAmount(fluid), toAction(simulate))
}
