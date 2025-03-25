package hiiragi283.ragium.common.item

import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTEnderBundleItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        // SEを再生する
        level.playSound(null, player.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS)
        // GUiを開く
        player.openMenu(
            SimpleMenuProvider(
                { containerId: Int, inventory: Inventory, playerIn: Player ->
                    ChestMenu.threeRows(containerId, inventory, playerIn.enderChestInventory)
                },
                Component.translatable("container.enderchest"),
            ),
        )
        player.awardStat(Stats.OPEN_ENDERCHEST)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }
}
