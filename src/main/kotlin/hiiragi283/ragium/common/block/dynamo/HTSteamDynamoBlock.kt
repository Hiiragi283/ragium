package hiiragi283.ragium.common.block.dynamo

import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.HTDynamoBlock
import hiiragi283.ragium.common.block.RagiumBlockBehaviors
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class HTSteamDynamoBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) : HTDynamoBlock(type, properties) {
    override fun useItemOn(
        itemStack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): InteractionResult = RagiumBlockBehaviors.interactWithTank(itemStack, level, pos, player, hand, hitResult)
}
