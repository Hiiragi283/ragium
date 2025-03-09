package hiiragi283.ragium.common.item.dynamite

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.entity.HTDynamite
import hiiragi283.ragium.common.entity.HTNapalmDynamite
import hiiragi283.ragium.common.item.HTThrowableItem
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class HTNapalmDynamiteItem(properties: Properties) : HTThrowableItem(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        tooltips.add(RagiumAPI.getInstance().createRangeText(stack))
    }

    override fun throwProjectile(level: Level, player: Player, stack: ItemStack): Projectile =
        HTDynamite.withItem(level, player, stack, ::HTNapalmDynamite)
}
