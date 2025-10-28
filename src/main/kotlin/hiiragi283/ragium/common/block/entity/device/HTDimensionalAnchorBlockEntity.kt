package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.common.util.RagiumChunkLoader
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * @see appeng.blockentity.spatial.SpatialAnchorBlockEntity
 */
class HTDimensionalAnchorBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(RagiumBlocks.DIM_ANCHOR, pos, state) {
    override fun afterLevelInit(level: Level) {
        super.afterLevelInit(level)
        forceChunk()
    }

    override fun setRemoved() {
        super.setRemoved()
        releaseChunk()
    }

    private fun forceChunk(): Boolean {
        if (!this.isClientSide.isTrue) return false
        if (this.isRemoved) return false
        val level: ServerLevel = this.level as ServerLevel
        val forced: Boolean = RagiumChunkLoader.forceChunk(level, blockPos, ChunkPos(blockPos))
        if (forced) setChanged()
        return forced
    }

    private fun releaseChunk(): Boolean {
        if (!this.isClientSide.isTrue) return false
        if (this.isRemoved) return false
        val level: ServerLevel = this.level as ServerLevel
        val released: Boolean = RagiumChunkLoader.releaseChunk(level, blockPos, ChunkPos(blockPos))
        if (released) setChanged()
        return released
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false
}
