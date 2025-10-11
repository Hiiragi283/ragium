package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTFluidItemSlot
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTItemStorageStack
import hiiragi283.ragium.api.storage.item.setItemStack
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

/**
 * @see [mekanism.common.inventory.slot.FluidInventorySlot]
 */
open class HTFluidItemStackSlot(
    protected val tank: HTFluidTank,
    canExtract: Predicate<HTItemStorageStack>,
    canInsert: Predicate<HTItemStorageStack>,
    filter: Predicate<HTItemStorageStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
    slotType: HTContainerItemSlot.Type,
) : HTItemStackSlot(HTItemSlot.ABSOLUTE_MAX_STACK_SIZE, canExtract, canInsert, filter, listener, x, y, slotType),
    HTFluidItemSlot {
    companion object {
        @JvmStatic
        fun fillPredicate(tank: HTFluidTank): Predicate<HTItemStorageStack> = Predicate { stack: HTItemStorageStack ->
            val handler: HTFluidHandler = RagiumCapabilities.FLUID.getSlottedCapability(stack) ?: return@Predicate false
            for (fluidTank: HTFluidTank in handler.getFluidTanks(handler.getFluidSideFor())) {
                val stack: HTFluidStorageStack = fluidTank.getStack()
                if (stack.isEmpty()) continue
                if (tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL).amountAsInt() >= stack.amountAsInt()) continue
                return@Predicate true
            }
            false
        }
    }

    final override fun getFluidTank(): HTFluidTank = tank

    override var isDraining: Boolean = false
    override var isFilling: Boolean = false

    override fun replaceContainer(container: ItemStack) {
        setItemStack(container)
    }

    override fun setStack(stack: HTItemStorageStack) {
        super.setStack(stack)
        isDraining = false
        isFilling = false
    }

    override fun serialize(output: HTValueOutput) {
        super.serialize(output)
        output.putBoolean("draining", isDraining)
        output.putBoolean("filling", isFilling)
    }

    override fun deserialize(input: HTValueInput) {
        this.isDraining = input.getBoolean("draining", false)
        this.isFilling = input.getBoolean("filling", false)
        super.deserialize(input)
    }
}
