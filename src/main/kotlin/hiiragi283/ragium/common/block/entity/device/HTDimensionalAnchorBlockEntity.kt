package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.common.block.entity.ExtendedBlockEntity
import hiiragi283.ragium.common.util.RagiumChunkLoader
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * @see appeng.blockentity.spatial.SpatialAnchorBlockEntity
 */
class HTDimensionalAnchorBlockEntity(pos: BlockPos, state: BlockState) :
    ExtendedBlockEntity(RagiumBlockEntityTypes.DIM_ANCHOR, pos, state) {
    override fun onUpdateLevel(level: Level, pos: BlockPos) {
        super.onUpdateLevel(level, pos)
        forceChunk(level, pos)
    }

    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        releaseChunk(level, pos)
    }

    private fun forceChunk(level: Level, pos: BlockPos): Boolean {
        val serverLevel: ServerLevel = level as? ServerLevel ?: return false
        val forced: Boolean = RagiumChunkLoader.forceChunk(serverLevel, pos, ChunkPos(pos))
        if (forced) setChanged()
        return forced
    }

    private fun releaseChunk(level: Level, pos: BlockPos): Boolean {
        val serverLevel: ServerLevel = level as? ServerLevel ?: return false
        val released: Boolean = RagiumChunkLoader.releaseChunk(serverLevel, pos, ChunkPos(pos))
        if (released) setChanged()
        return released
    }
}
