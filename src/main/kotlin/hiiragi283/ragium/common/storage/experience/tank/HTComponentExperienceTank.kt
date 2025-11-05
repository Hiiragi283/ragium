package hiiragi283.ragium.common.storage.experience.tank

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

open class HTComponentExperienceTank(
    private val parent: ItemStack,
    private val capacity: Long,
    private val canExtract: Predicate<HTStorageAccess>,
    private val canInsert: Predicate<HTStorageAccess>,
) : HTExperienceTank.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        @JvmStatic
        fun create(
            parent: ItemStack,
            capacity: Long,
            canExtract: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTPredicates.alwaysTrue(),
        ): HTComponentExperienceTank = HTComponentExperienceTank(parent, capacity, canExtract, canInsert)
    }

    protected val component: DataComponentType<Long> get() = RagiumDataComponents.EXPERIENCE

    override fun setAmount(amount: Long) {
        if (amount > 0) {
            parent.set(component, amount)
        } else {
            parent.remove(component)
        }
    }

    override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmount(): Long = parent.get(component) ?: 0

    override fun getCapacity(): Long = capacity
}
