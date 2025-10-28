package hiiragi283.ragium.common.storage.experience

import hiiragi283.ragium.api.extension.setOrRemove
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.experience.HTExperienceStorage
import hiiragi283.ragium.api.storage.experience.IExperienceStorageItem
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack

/**
 * [HTExperienceStorage]に基づいたコンポーネント向けの実装
 */
open class HTComponentExperienceStorage(private val parent: ItemStack, private val capacity: Long) :
    HTExperienceStorage.Basic(),
    IExperienceStorageItem,
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    protected val component: DataComponentType<Long> get() = RagiumDataComponents.EXPERIENCE

    override fun setAmount(amount: Long, action: HTStorageAction) {
        val fixedAmount: Long = Mth.clamp(amount, 0, capacity)
        if (action.execute) {
            parent.setOrRemove(component, fixedAmount) { it <= 0 }
        }
    }

    override fun getAmount(): Long = Mth.clamp(parent.getOrDefault(component, 0), 0, capacity)

    override fun getCapacity(): Long = capacity

    override fun getContainer(): ItemStack = parent
}
