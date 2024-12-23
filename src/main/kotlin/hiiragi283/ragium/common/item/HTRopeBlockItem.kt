package hiiragi283.ragium.common.item

import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class HTRopeBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack: ItemStack = user.getStackInHand(hand)
        val copiedStack: ItemStack = stack.copy()
        val count: Int = stack.count
        /*throwEntity(world, user) { world1: World, player1: PlayerEntity ->
            HTDynamiteEntity(world1, player1)
                .apply {
                    setItem(stack)
                    this.stack.count = count
                }.setAction { entity: HTDynamiteEntity, result: HitResult ->
                    val world: World = entity.world
                    result.onBlockHit { blockResult: BlockHitResult ->
                        var pos: BlockPos = blockResult.sidedPos
                        val ropeState: BlockState = RagiumBlocks.ROPE.get().defaultState
                        if (ropeState.canPlaceAt(world, pos)) {
                            while (world.getBlockState(pos).isAir) {
                                world.setBlockState(pos, ropeState)
                                entity.stack.decrement(1)
                                pos = pos.down()
                            }
                            entity.owner?.let { owner: Entity -> dropStackAt(owner, entity.stack) }
                        } else {
                            dropStackAt(user, copiedStack)
                        }
                    }
                }
        }*/
        user.incrementStat(Stats.USED.getOrCreateStat(this))
        stack.decrementUnlessCreative(count, user)
        return TypedActionResult.success(stack, world.isClient())
    }
}
