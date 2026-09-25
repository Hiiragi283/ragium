package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.fluid.getFluidStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.fluid.FluidResource

/**
 * [Fluid]向けの[HTResourceInputSlot]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class HTFluidInputTank(slot: HTResourceSlot<FluidResource>) :
    HTResourceInputSlot<Fluid, FluidResource, FluidInstance>(slot) {
    override fun getEmptyStack(): FluidStack = FluidStack.EMPTY

    override fun getAmount(instance: FluidInstance): Int = instance.amount()

    override fun asResource(instance: FluidInstance): FluidResource = when (instance) {
        is FluidStack -> FluidResource.of(instance)
        is FluidStackTemplate -> FluidResource.of(instance)
        else -> FluidResource.of(instance.typeHolder())
    }

    override fun getStoredInput(): FluidStack = slot.getFluidStack()

    override fun isEmpty(instance: FluidInstance): Boolean = HTIngredientHelper.isEmpty(instance)
}
