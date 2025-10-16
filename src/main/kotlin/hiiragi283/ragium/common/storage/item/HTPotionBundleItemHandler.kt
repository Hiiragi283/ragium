package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.isOf
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class HTPotionBundleItemHandler(parent: ItemStack, size: Long) :
    HTComponentItemHandler(parent, size),
    HTMenuCallback {
    companion object {
        @JvmStatic
        fun filterPotion(stack: ImmutableItemStack): Boolean = stack.isEmpty() || stack.isOf(Items.POTION)
    }

    override fun createSlot(slot: Int): HTItemSlot = object : ComponentSlot(parent, size, slot) {
        override fun isValid(stack: ImmutableItemStack): Boolean = filterPotion(stack)
    }

    override fun openMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS)
    }

    override fun closeMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS)
    }
}
