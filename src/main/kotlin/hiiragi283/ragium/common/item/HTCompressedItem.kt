package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.capability.HTItemCapabilities
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemUtils

class HTCompressedItem(properties: Properties) : Item(properties) {
    override fun onDestroyed(itemEntity: ItemEntity, damageSource: DamageSource) {
        ItemUtils.onContainerDestroyed(
            itemEntity,
            HTItemCapabilities
                .getItemSlots(itemEntity.item)
                .mapNotNull(HTItemSlot::getStack)
                .map(ImmutableItemStack::unwrap),
        )
    }
}
