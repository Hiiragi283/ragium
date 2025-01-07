package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.unbreakable
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Equipment
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Rarity

class HTDragonicElytraItem(settings: Settings) :
    Item(settings.unbreakable().rarity(Rarity.EPIC)),
    Equipment,
    FabricElytraItem {
    //    Equipment    //

    override fun getSlotType(): EquipmentSlot = EquipmentSlot.CHEST

    override fun getEquipSound(): RegistryEntry<SoundEvent> = SoundEvents.BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON

    //    FabricElytraItem    //

    override fun useCustomElytra(entity: LivingEntity, chestStack: ItemStack, tickElytra: Boolean): Boolean {
        if (tickElytra) {
            doVanillaElytraTick(entity, chestStack)
        }
        return true
    }
}
