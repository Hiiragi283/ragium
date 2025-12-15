package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.entity.HTAbstractBlockEntity
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.network.PacketDistributor

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 * @see mekanism.common.tile.base.TileEntityUpdateable
 */
abstract class ExtendedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTAbstractBlockEntity {
    //    Save & Read    //

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        RagiumPlatform.INSTANCE.createValueOutput(registries, tag).let(::writeValue)
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        RagiumPlatform.INSTANCE.createValueInput(registries, tag).let(::readValue)
    }

    final override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = getReducedUpdateTag(registries)

    final override fun handleUpdateTag(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(tag, provider)
        handleUpdateTag(RagiumPlatform.INSTANCE.createValueInput(provider, tag))
        requestModelDataUpdate()
    }

    final override fun onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket, provider: HolderLookup.Provider) {
        val tag: CompoundTag = pkt.tag
        if (!tag.isEmpty) handleUpdateTag(tag, provider)
    }

    fun sendUpdatePacket(level: ServerLevel) {
        if (isRemoved) return
        val payload: HTUpdateBlockEntityPacket = HTUpdateBlockEntityPacket.create(this) ?: return
        PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos(blockPos), payload)
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        super.setBlockState(blockState)
        onUpdatedState(blockState)
    }

    final override fun setLevel(level: Level) {
        super.setLevel(level)
        onUpdateLevel(level, blockPos)
    }

    final override fun setRemoved() {
        super.setRemoved()
        val level: Level = this.level ?: return
        onRemove(level, blockPos)
    }

    //    HTContentListener    //

    protected fun setOnlySave() {
        setChanged(false)
    }

    override fun setChanged() {
        setChanged(true)
    }

    private var lastSaveTime: Long = 0

    /**
     * @see mekanism.common.tile.base.TileEntityUpdateable.setChanged
     */
    protected open fun setChanged(updateComparator: Boolean) {
        val level: Level = this.getLevel() ?: return
        val time: Long = level.gameTime
        if (lastSaveTime != time) {
            level.blockEntityChanged(blockPos)
            lastSaveTime = time
        }
        if (updateComparator && !level.isClientSide) markDirtyComparator()
    }

    protected open fun markDirtyComparator() {}

    //    Extensions    //

    protected open fun writeValue(output: HTValueOutput) {}

    protected open fun readValue(input: HTValueInput) {}

    /**
     * @see mekanism.common.tile.base.TileEntityUpdateable.getReducedUpdateTag
     */
    fun getReducedUpdateTag(provider: HolderLookup.Provider): CompoundTag {
        val tag: CompoundTag = super.getUpdateTag(provider)
        initReducedUpdateTag(RagiumPlatform.INSTANCE.createValueOutput(provider, tag))
        return tag
    }

    open fun initReducedUpdateTag(output: HTValueOutput) {}

    open fun handleUpdateTag(input: HTValueInput) {}

    /**
     * [BlockEntity.setBlockState]の後で呼び出されます。
     */
    open fun onUpdatedState(state: BlockState) {}

    /**
     * [BlockEntity.setLevel]の後で呼び出されます。
     */
    open fun onUpdateLevel(level: Level, pos: BlockPos) {}

    /**
     * [BlockEntity.setRemoved]の後で呼び出されます。
     */
    open fun onRemove(level: Level, pos: BlockPos) {}

    /**
     * [Block.useWithoutItem]でGUIを開くときに，クライアント側へ送るデータを書き込みます。
     * @see mekanism.common.tile.base.TileEntityMekanism.encodeExtraContainerData
     */
    open fun writeExtraContainerData(buf: RegistryFriendlyByteBuf) {
        buf.writeBlockPos(getBlockPos())
    }

    /**
     * ブロックのコンパレータ出力を返します。
     */
    open fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = 0

    /**
     * 隣接ブロックが更新された時に呼び出されます。
     */
    open fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
    }
}
