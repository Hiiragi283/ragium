package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.extension.tankRange
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import java.util.function.Predicate

/**
 * @see [mekanism.common.inventory.slot.FluidInventorySlot]
 */
open class HTFluidItemStackSlot(
    protected val tank: HTFluidTank,
    canExtract: Predicate<ItemStack>,
    canInsert: Predicate<ItemStack>,
    filter: Predicate<ItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
    slotType: HTContainerItemSlot.Type,
) : HTItemStackSlot(HTItemSlot.ABSOLUTE_MAX_STACK_SIZE, canExtract, canInsert, filter, listener, x, y, slotType),
    HTFluidItemSlot {
    companion object {
        @JvmStatic
        fun fillPredicate(tank: HTFluidTank): Predicate<ItemStack> = Predicate { stack: ItemStack ->
            val handler: IFluidHandlerItem = HTMultiCapability.FLUID.getCapability(stack) ?: return@Predicate false
            for (i: Int in handler.tankRange) {
                val stackIn: FluidStack = handler.getFluidInTank(i)
                if (!stack.isEmpty && tank.insert(stackIn, true, HTStorageAccess.INTERNAl).amount < stackIn.amount) {
                    return@Predicate true
                }
            }
            false
        }
    }

    final override fun getFluidTank(): HTFluidTank = tank

    override var isDraining: Boolean = false
    override var isFilling: Boolean = false

    override fun setStack(stack: ItemStack) {
        super.setStack(stack)
        isDraining = false
        isFilling = false
    }

    override fun serialize(output: HTValueOutput) {
        super<HTFluidItemSlot>.serialize(output)
        output.putBoolean("draining", isDraining)
        output.putBoolean("filling", isFilling)
    }

    override fun deserialize(input: HTValueInput) {
        this.isDraining = input.getBoolean("draining", false)
        this.isFilling = input.getBoolean("filling", false)
        super.deserialize(input)
    }
}
