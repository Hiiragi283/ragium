package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.util.RagiumChunkLoader
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * @see [appeng.blockentity.spatial.SpatialAnchorBlockEntity]
 */
class HTDimensionalAnchorBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntity(RagiumBlockEntityTypes.DIM_ANCHOR, pos, state) {
    override fun afterLevelInit(level: Level) {
        super.afterLevelInit(level)
        forceChunk()
    }

    override fun setRemoved() {
        super.setRemoved()
        releaseChunk()
    }

    override fun writeNbt(writer: HTNbtCodec.Writer) {}

    override fun readNbt(reader: HTNbtCodec.Reader) {}

    private fun forceChunk(): Boolean {
        if (this.isRemoved) return false
        val level: ServerLevel = this.level as? ServerLevel ?: return false
        val forced: Boolean = RagiumChunkLoader.forceChunk(level, blockPos, ChunkPos(blockPos))
        if (forced) setChanged()
        return forced
    }

    private fun releaseChunk(): Boolean {
        if (this.isRemoved) return false
        val level: ServerLevel = this.level as? ServerLevel ?: return false
        val released: Boolean = RagiumChunkLoader.releaseChunk(level, blockPos, ChunkPos(blockPos))
        if (released) setChanged()
        return released
    }
}
