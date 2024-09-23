package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.*
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTItemDisplayBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseBlockEntity(RagiumBlockEntityTypes.ITEM_DISPLAY, pos, state),
    HTDelegatedInventory {
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val hand: Hand = player.activeHand
        val stack: ItemStack = player.getStackInHand(hand)
        val stackIn: ItemStack = getStack(0).copy()
        setStack(0, stack.copy())
        player.setStackInHand(hand, stackIn)
        return ActionResult.success(world.isClient)
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory = HTSidedStorageBuilder(1)
        .set(0, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .buildSided()

    override fun markDirty() {
        super<HTBaseBlockEntity>.markDirty()
    }
}