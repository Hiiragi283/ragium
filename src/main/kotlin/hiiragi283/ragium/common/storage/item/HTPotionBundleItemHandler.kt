package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.storage.item.HTComponentItemHandler
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.MutableDataComponentHolder

class HTPotionBundleItemHandler(parent: MutableDataComponentHolder, size: Int) :
    HTComponentItemHandler(parent, size),
    HTMenuCallback {
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = stack.isEmpty || stack.`is`(Items.POTION)

    override fun openMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS)
    }

    override fun closeMenu(player: Player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS)
    }
}
