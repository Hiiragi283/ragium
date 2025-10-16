package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemUtils

class HTCompressedItem(properties: Properties) : Item(properties) {
    override fun onDestroyed(itemEntity: ItemEntity, damageSource: DamageSource) {
        ItemUtils.onContainerDestroyed(
            itemEntity,
            RagiumCapabilities.ITEM.getCapabilitySlots(itemEntity.item).map(HTItemSlot::getItemStack),
        )
    }
}
