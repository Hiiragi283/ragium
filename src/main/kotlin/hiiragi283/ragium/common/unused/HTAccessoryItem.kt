package hiiragi283.ragium.common.unused

import hiiragi283.ragium.api.extension.itemSettings
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Equipment
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

open class HTAccessoryItem(private val slot: EquipmentSlot, settings: Settings = itemSettings()) :
    Item(settings.maxCount(1)),
    Equipment {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> = equipAndSwap(this, world, user, hand)

    override fun getSlotType(): EquipmentSlot = slot
}
