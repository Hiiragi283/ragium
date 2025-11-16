package hiiragi283.ragium.common

import hiiragi283.ragium.api.data.map.HTEquipAction
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

data object RagiumAccessory : Accessory {
    override fun tick(stack: ItemStack, reference: SlotReference) {
        val entity: LivingEntity = reference.entity()
        stack.inventoryTick(entity.level(), entity, -1, false)
    }

    override fun onEquip(stack: ItemStack, reference: SlotReference) {
        val equipAction: HTEquipAction = stack.itemHolder.getData(RagiumDataMaps.ARMOR_EQUIP) ?: return
        val entity: Player = reference.entity() as? Player ?: return
        equipAction.onEquip(entity, stack)
    }

    override fun onUnequip(stack: ItemStack, reference: SlotReference) {
        val equipAction: HTEquipAction = stack.itemHolder.getData(RagiumDataMaps.ARMOR_EQUIP) ?: return
        val entity: Player = reference.entity() as? Player ?: return
        equipAction.onUnequip(entity, stack)
    }
}
