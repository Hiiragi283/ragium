package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

object HTManualGrinderBlock : HTBlockWithEntity(blockSettings(Blocks.BRICKS)), InventoryProvider {
    init {
        defaultState = stateManager.defaultState.with(RagiumBlockProperties.LEVEL_7, 0)
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int = state.get(RagiumBlockProperties.LEVEL_7)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(RagiumBlockProperties.LEVEL_7)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTManualGrinderBlockEntity(pos, state)

    //    InventoryProvider    //

    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory? =
        (world.getBlockEntity(pos) as? HTManualGrinderBlockEntity)?.asInventory()
}
