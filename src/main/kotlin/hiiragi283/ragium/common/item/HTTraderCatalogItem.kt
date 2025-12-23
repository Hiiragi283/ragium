package hiiragi283.ragium.common.item

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.npc.WanderingTrader
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTTraderCatalogItem(properties: Properties) : Item(properties.stacksTo(1)) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        val trader: WanderingTrader = EntityType.WANDERING_TRADER.create(level) ?: return InteractionResultHolder.fail(stack)
        return InteractionResultHolder(trader.interact(player, usedHand), stack)
    }
}
