package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.item.HTDescriptionBlockItem
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HTExpBerriesItem(block: HTExpBerriesBushBlock, properties: Properties) :
    HTDescriptionBlockItem<HTExpBerriesBushBlock>(block, properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        val amount: Int = RagiumConfig.COMMON.expBerriesValue.asInt
        when {
            player.isShiftKeyDown -> giveExp(player, stack, amount * stack.count, stack.count)
            else -> giveExp(player, stack, amount, 1)
        }
        level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    private fun giveExp(
        player: Player,
        stack: ItemStack,
        amount: Int,
        count: Int,
    ) {
        player.giveExperiencePoints(amount)
        stack.consume(count, player)
    }

    override fun getDescriptionId(): String = this.orCreateDescriptionId
}
