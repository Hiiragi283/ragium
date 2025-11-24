package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.material.HTChargeType
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class HTChargeItem(private val chargeType: HTChargeType, properties: Properties) :
    Item(properties),
    ProjectileItem {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        level.playSound(
            null,
            player.x,
            player.y,
            player.z,
            chargeType.getShootSound(),
            SoundSource.PLAYERS,
            0.5f,
            0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f),
        )
        if (!level.isClientSide) {
            val charge: ThrowableItemProjectile = chargeType.createCharge(level, player)
            charge.item = stack
            charge.shootFromRotation(player, player.xRot, player.yRot, 0.0f, 1.5f, 1.0f)
            level.addFreshEntity(charge)
        }
        player.awardStat(Stats.ITEM_USED.get(this))
        stack.consume(1, player)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    override fun asProjectile(
        level: Level,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): Projectile {
        val charge: ThrowableItemProjectile = chargeType.createCharge(level, pos.x(), pos.y(), pos.z())
        charge.item = stack
        return charge
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        if (flag.hasShiftDown()) {
            tooltips.add(chargeType.getTranslation().translate())
        } else {
            RagiumTranslation.TOOLTIP_CHARGE_POWER
                .translateColored(
                    ChatFormatting.BLUE,
                    ChatFormatting.GRAY,
                    stack.getOrDefault(RagiumDataComponents.CHARGE_POWER, 4f),
                ).let(tooltips::add)
            RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW).let(tooltips::add)
        }
    }
}
