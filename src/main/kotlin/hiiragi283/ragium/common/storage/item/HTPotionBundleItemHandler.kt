package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTMenuCallback
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class HTPotionBundleItemHandler(parent: ItemStack, size: Int) :
    HTComponentItemHandler(parent, size),
    HTMenuCallback {
    companion object {
        @JvmStatic
        fun filterPotion(stack: ItemStack): Boolean = stack.isEmpty || stack.`is`(Items.POTION)
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = filterPotion(stack)

    override fun openMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS)
    }

    override fun closeMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS)
    }
}
