package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTItemDisplayBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntityBase(RagiumBlockEntityTypes.ITEM_DISPLAY, pos, state) {
    private val inventory: SidedInventory = HTStorageBuilder(1)
        .set(0, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .buildInventory()

    override fun asInventory(): SidedInventory = inventory

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val hand: Hand = player.activeHand
        val stack: ItemStack = player.getStackInHand(hand)
        val stackIn: ItemStack = inventory.getStack(0).copy()
        inventory.setStack(0, stack.copy())
        player.setStackInHand(hand, stackIn)
        return ActionResult.success(world.isClient)
    }
}
