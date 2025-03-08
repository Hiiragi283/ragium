package hiiragi283.ragium.common.item.dynamite

import hiiragi283.ragium.api.extension.floatText
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.entity.HTDynamite
import hiiragi283.ragium.common.entity.HTSimpleDynamite
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.item.HTThrowableItem
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class HTSimpleDynamiteItem(properties: Properties) : HTThrowableItem(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        tooltips.add(
            Component
                .translatable(
                    RagiumTranslationKeys.EXPLOSION_POWER,
                    floatText(stack.getOrDefault(RagiumComponentTypes.EXPLOSION_POWER, 2f)).withStyle(ChatFormatting.WHITE),
                ).withStyle(ChatFormatting.GRAY),
        )
    }

    override fun throwProjectile(level: Level, player: Player, stack: ItemStack): Projectile =
        HTDynamite.withItem(level, player, stack, ::HTSimpleDynamite)
}
