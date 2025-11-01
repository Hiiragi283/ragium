package hiiragi283.ragium.common.storage.energy.battery

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

/**
 * @see mekanism.common.attachments.containers.energy.ComponentBackedEnergyContainer
 */
open class HTComponentEnergyBattery(
    private val parent: ItemStack,
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
) : HTEnergyBattery.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        @JvmStatic
        fun create(
            parent: ItemStack,
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
        ): HTComponentEnergyBattery = HTComponentEnergyBattery(parent, capacity, canExtract, canInsert)
    }

    protected val component: DataComponentType<Int> get() = RagiumDataComponents.ENERGY

    override fun setAmount(amount: Int) {
        if (amount > 0) {
            parent.set(component, amount)
        } else {
            parent.remove(component)
        }
    }

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmount(): Int = parent.getOrDefault(component, 0)

    override fun getCapacity(): Int = capacity
}
