package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

fun interface HTEquippedArmorCallback {
    companion object {
        @JvmField
        val EVENT: Event<HTEquippedArmorCallback> = EventFactory.createArrayBacked(
            HTEquippedArmorCallback::class.java,
        ) { listeners: Array<HTEquippedArmorCallback> ->
            HTEquippedArmorCallback { entity: LivingEntity, slot: EquipmentSlot, oldStack: ItemStack, newStack: ItemStack ->
                listeners.forEach { it.onEquip(entity, slot, oldStack, newStack) }
            }
        }
    }

    fun onEquip(
        entity: LivingEntity,
        slot: EquipmentSlot,
        oldStack: ItemStack,
        newStack: ItemStack,
    )
}
