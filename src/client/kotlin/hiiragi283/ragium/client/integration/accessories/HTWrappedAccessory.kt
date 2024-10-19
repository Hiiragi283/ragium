package hiiragi283.ragium.client.integration.accessories

import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.item.ItemStack

object HTWrappedAccessory : Accessory {
    override fun onEquip(stack: ItemStack, reference: SlotReference) {
        HTAccessoryRegistry.onEquipped(reference.entity(), stack)
    }

    override fun onUnequip(stack: ItemStack, reference: SlotReference) {
        HTAccessoryRegistry.onUnequipped(reference.entity(), stack)
    }
}
