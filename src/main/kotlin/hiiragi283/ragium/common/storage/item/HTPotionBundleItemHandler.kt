package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.isOf
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.setItemStack
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.MutableDataComponentHolder

class HTPotionBundleItemHandler(parent: ItemStack, size: Long) :
    HTComponentItemHandler(parent, size),
    HTMenuCallback {
    companion object {
        @JvmStatic
        fun filterPotion(stack: ImmutableItemStack): Boolean = stack.isEmpty() || stack.isOf(Items.POTION)
    }

    override fun createSlot(slot: Int): HTItemSlot = PotionSlot(parent, size, slot)

    override fun openMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS)
    }

    override fun closeMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS)
    }

    private class PotionSlot(parent: MutableDataComponentHolder, size: Int, slot: Int) : ComponentSlot(parent, size, slot) {
        override fun createContainerSlot(): Slot = HTContainerItemSlot(
            this,
            HTSlotHelper.getSlotPosX(slot % 9),
            HTSlotHelper.getSlotPosY(slot / 9),
            this::setItemStack,
            this::isStackValidForInsert,
            HTContainerItemSlot.Type.BOTH,
        )

        override fun isValid(stack: ImmutableItemStack): Boolean = filterPotion(stack)
    }
}
