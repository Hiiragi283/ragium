package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.common.util.HTItemDropHelper
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.UseOnContext

class HTBottledBeeItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val player: Player = context.player ?: return InteractionResult.FAIL
        val result: InteractionResult = Items.BEE_SPAWN_EGG.useOn(context)
        if (result.indicateItemUse()) {
            HTItemDropHelper.giveStackTo(player, ItemStack(Items.GLASS_BOTTLE))
        }
        return result
    }
}
