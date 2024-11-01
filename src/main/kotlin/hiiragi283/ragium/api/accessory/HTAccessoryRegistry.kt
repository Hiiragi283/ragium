package hiiragi283.ragium.api.accessory

import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack

object HTAccessoryRegistry {
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
        val item1: Item = item.asItem()
        Builder().apply(builderAction).apply {
            equipped[item1] = equippedAction
            unequipped[item1] = unequippedAction
            slotType?.let { slotTypes1[item1] = it }
        }
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
