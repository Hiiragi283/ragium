package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.common.block.entity.HTItemDisplayBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class HTItemDisplayBlock(settings: Settings) :
    HTBlockWithEntity(
        settings
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .solidBlock(Blocks::never)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never),
    ),
    InventoryProvider {
    override fun isTransparent(state: BlockState, world: BlockView, pos: BlockPos): Boolean = true

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTItemDisplayBlockEntity(pos, state)

    //    InventoryProvider    //

    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory? =
        (world.getBlockEntity(pos) as? HTItemDisplayBlockEntity)?.asInventory()
}
