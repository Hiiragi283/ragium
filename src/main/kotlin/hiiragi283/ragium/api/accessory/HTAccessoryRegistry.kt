package hiiragi283.ragium.api.accessory

import hiiragi283.ragium.api.extension.error
import hiiragi283.ragium.api.util.DelegatedLogger
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import org.slf4j.Logger

object HTAccessoryRegistry {
    @JvmStatic
    private val logger: Logger by DelegatedLogger()

    @JvmStatic
    private val equipped: MutableMap<Item, EquippedAction> = mutableMapOf()

    @JvmStatic
    private val unequipped: MutableMap<Item, UnequippedAction> = mutableMapOf()

    @JvmStatic
    val slotTypes: Map<Item, HTAccessorySlotTypes>
        get() = slotTypes1

    @JvmStatic
    private val slotTypes1: MutableMap<Item, HTAccessorySlotTypes> = mutableMapOf()

    @JvmStatic
    fun onEquipped(entity: LivingEntity, stack: ItemStack) {
        equipped[stack.item]?.onEquipped(entity)
    }

    @JvmStatic
    fun onUnequipped(entity: LivingEntity, stack: ItemStack) {
        unequipped[stack.item]?.onUnequipped(entity)
    }

    @JvmStatic
    fun register(item: ItemConvertible, builderAction: Builder.() -> Unit) {
        runCatching {
            val item1: Item = item.asItem()
            Builder().apply(builderAction).apply {
                equipped[item1] = equippedAction
                unequipped[item1] = unequippedAction
                slotType?.let { slotTypes1[item1] = it }
            }
        }.onFailure(logger::error)
    }

    //    EquippedAction    //

    fun interface EquippedAction {
        fun onEquipped(entity: LivingEntity)
    }

    //    UnequippedAction    //

    fun interface UnequippedAction {
        fun onUnequipped(entity: LivingEntity)
    }

    //    Builder    //

    class Builder {
        lateinit var equippedAction: EquippedAction
        lateinit var unequippedAction: UnequippedAction
        var slotType: HTAccessorySlotTypes? = null
    }
}
