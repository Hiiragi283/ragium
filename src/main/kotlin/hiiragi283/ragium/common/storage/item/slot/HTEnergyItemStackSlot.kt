package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.negate
import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTEnergyCapabilities
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.util.HTEnergyHelper
import java.util.function.Predicate

/**
 * @see mekanism.common.inventory.slot.EnergyInventorySlot
 */
open class HTEnergyItemStackSlot protected constructor(
    protected val battery: HTEnergyBattery,
    canExtract: Predicate<ImmutableItemStack>,
    canInsert: Predicate<ImmutableItemStack>,
    filter: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
) : HTItemStackSlot(
        RagiumConst.ABSOLUTE_MAX_STACK_SIZE,
        canExtract,
        canInsert,
        filter,
        listener,
        x,
        y,
        HTContainerItemSlot.Type.BOTH,
    ) {
    companion object {
        /**
         * スロット内のアイテムから電力を吸い取ります。
         */
        @JvmStatic
        fun fill(
            battery: HTEnergyBattery,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTEnergyItemStackSlot {
            val filter: (ImmutableItemStack) -> Boolean = filter@{ stack: ImmutableItemStack ->
                val battery: HTEnergyBattery = HTEnergyCapabilities.getBattery(stack) ?: return@filter false
                battery.extract(Int.MAX_VALUE, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) > 0
            }
            return HTEnergyItemStackSlot(
                battery,
                filter.negate(),
                filter,
                HTEnergyCapabilities::hasCapability,
                listener,
                x,
                y,
            )
        }

        /**
         * スロット内のアイテムに電力を吐き出します。
         */
        @JvmStatic
        fun drain(
            battery: HTEnergyBattery,
            listener: HTContentListener?,
            x: Int,
            y: Int,
        ): HTEnergyItemStackSlot {
            val filter: (ImmutableItemStack) -> Boolean = filter@{ stack: ImmutableItemStack ->
                val battery: HTEnergyBattery = HTEnergyCapabilities.getBattery(stack) ?: return@filter false
                val stored: Int = battery.getAmount()
                if (stored == 0) {
                    battery.getNeeded() > 0
                } else {
                    battery.insert(stored, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) > 0
                }
            }
            return HTEnergyItemStackSlot(
                battery,
                filter.negate(),
                filter,
                HTEnergyCapabilities::hasCapability,
                listener,
                x,
                y,
            )
        }
    }

    /**
     * スロット内のアイテムから電力をバッテリーに移動します。
     */
    fun fillBattery(): Boolean {
        val stack: ImmutableItemStack = this.getStack() ?: return false
        val itemBattery: HTEnergyBattery = HTEnergyCapabilities.getBattery(stack) ?: return false
        val extracted: Int = HTEnergyHelper.moveEnergy(
            itemBattery,
            this.battery,
            { setStack(stack) },
        ) ?: 0
        return extracted > 0
    }

    /**
     * バッテリーから電力をスロット内のアイテムに移動します。
     */
    fun drainBattery(): Boolean {
        val stack: ImmutableItemStack = this.getStack() ?: return false
        val itemBattery: HTEnergyBattery = HTEnergyCapabilities.getBattery(stack) ?: return false
        val extracted: Int = HTEnergyHelper.moveEnergy(
            this.battery,
            itemBattery,
            { setStack(stack) },
            Int.MAX_VALUE,
        ) ?: 0
        return extracted > 0
    }
}
