package hiiragi283.ragium.common.storage.experience

import hiiragi283.ragium.api.function.clamp
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorageItem
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * [HTExperienceStorage]に基づいたコンポーネント向けの実装
 */
open class HTComponentExperienceStorage(private val parent: ItemStack, private val capacity: Long) :
    HTExperienceStorage.Basic(),
    IExperienceStorageItem,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    protected val component: DataComponentType<Long> get() = RagiumDataComponents.EXPERIENCE

    override fun setAmount(amount: Long) {
        setAmountUnchecked(amount, true)
    }

    fun setAmountUnchecked(amount: Long, validate: Boolean = false) {
        if (amount == 0L) {
            if (parent.getOrDefault(component, 0) == 0L) return
            parent.remove(component)
        } else if (!validate || amount > 0) {
            parent.set(component, min(amount, getCapacity()))
        } else {
            error("Invalid amount for storage: $amount")
        }
        onContentsChanged()
    }

    override fun getAmount(): Long = parent.getOrDefault(component, 0).clamp(0..capacity)

    override fun getCapacity(): Long = capacity

    override fun getContainer(): ItemStack = parent
}
