package hiiragi283.ragium.api.accessory

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack

object HTAccessoryRegistry {
    @JvmStatic
    private val equipped: MutableMap<ItemConvertible, EquippedAction> = mutableMapOf()

    @JvmStatic
    private val unequipped: MutableMap<ItemConvertible, UnequippedAction> = mutableMapOf()

    @JvmStatic
    val slotTypes: Map<ItemConvertible, HTAccessorySlotTypes>
        get() = slotTypes1

    @JvmStatic
    private val slotTypes1: MutableMap<ItemConvertible, HTAccessorySlotTypes> = mutableMapOf()

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
        Builder().apply(builderAction).apply {
            equipped[item] = equippedAction
            unequipped[item] = unequippedAction
            slotTypes1[item] = slotType
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
        lateinit var slotType: HTAccessorySlotTypes
    }
}
