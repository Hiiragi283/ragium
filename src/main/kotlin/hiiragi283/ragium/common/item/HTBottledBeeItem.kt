package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.dropStackAt
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class HTBottledBeeItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult = Items.BEE_SPAWN_EGG.useOn(context).apply {
        if (this.indicateItemUse()) {
            val player: Player = context.player ?: return@apply
            dropStackAt(player, Items.GLASS_BOTTLE)
        }
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> =
        Items.BEE_SPAWN_EGG.use(level, player, usedHand).apply {
            if (this.result.indicateItemUse()) {
                dropStackAt(player, Items.GLASS_BOTTLE)
            }
        }
}
