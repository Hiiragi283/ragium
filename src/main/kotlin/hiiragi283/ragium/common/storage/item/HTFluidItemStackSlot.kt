package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.extension.tankRange
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.Item
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
) : HTItemStackSlot(Item.ABSOLUTE_MAX_STACK_SIZE, canExtract, canInsert, filter, listener, x, y),
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

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag {
        val nbt: CompoundTag = super<HTFluidItemSlot>.serializeNBT(provider)
        nbt.putBoolean("draining", isDraining)
        nbt.putBoolean("filling", isFilling)
        return nbt
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        this.isDraining = nbt.getBoolean("draining")
        this.isFilling = nbt.getBoolean("filling")
        super.deserializeNBT(provider, nbt)
    }
}
