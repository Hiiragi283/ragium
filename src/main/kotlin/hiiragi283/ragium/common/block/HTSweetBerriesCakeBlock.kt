package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.extension.blockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CakeBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTSweetBerriesCakeBlock : CakeBlock(blockSettings(Blocks.CAKE)) {
    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult,
    ): ItemActionResult = ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
}
