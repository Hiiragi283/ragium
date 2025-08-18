package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.util.RagiumChunkLoader
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState

/**
 * @see [appeng.blockentity.spatial.SpatialAnchorBlockEntity]
 */
class HTDimensionalAnchorBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(HTDeviceVariant.DIM_ANCHOR, pos, state) {
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
        if (forced) onContentsChanged()
        return forced
    }

    private fun releaseChunk(): Boolean {
        if (this.isRemoved) return false
        val level: ServerLevel = this.level as? ServerLevel ?: return false
        val released: Boolean = RagiumChunkLoader.releaseChunk(level, blockPos, ChunkPos(blockPos))
        if (released) onContentsChanged()
        return released
    }

    override fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState = TriState.FALSE

    override val containerData: ContainerData = SimpleContainerData(2)

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null
}
