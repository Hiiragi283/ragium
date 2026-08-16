package hiiragi283.ragium.block.entity

import com.mojang.logging.LogUtils
import hiiragi283.lib.block.entity.HTAbstractBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ProblemReporter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.redstone.Orientation
import net.minecraft.world.level.storage.TagValueOutput
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import org.slf4j.Logger

/**
 * Hiiragi Seriesで使用される[BlockEntity]の拡張クラスです。
 *
 * 参考 : [Mekanism - TileEntityUpdateable](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/tile/base/TileEntityUpdateable.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTExtendedBlockEntity(type: BlockEntityType<*>, pos: BlockPos, blockState: BlockState) :
    BlockEntity(type, pos, blockState),
    HTAbstractBlockEntity {
    companion object {
        @JvmField
        protected val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Extensions    //

    /**
     * セーブ時に値を書き込みます。
     * @param output 値の書き込み先
     */
    protected open fun writeValue(output: ValueOutput) {}

    /**
     * ロード時に値を読み取ります。
     * @param input 値の読み取り元
     */
    protected open fun readValue(input: ValueInput) {}

    fun createReporter(): ProblemReporter = ProblemReporter.ScopedCollector(this.problemPath(), LOGGER)

    /**
     * [writeReducedUpdateTag]に基づいた更新用のNBTを作成します。
     */
    fun createReducedUpdateTag(registries: HolderLookup.Provider): CompoundTag = TagValueOutput.createWithContext(createReporter(), registries)
        .also(::writeReducedUpdateTag)
        .buildResult()

    /**
     * 更新時に値を書き込みます。
     * @param output 値の書き込み先
     */
    open fun writeReducedUpdateTag(output: ValueOutput) {}

    /**
     * 更新時に値を読み取ります。
     * @param input 値の読み取り元
     */
    open fun readUpdateTag(input: ValueInput) {}

    /**
     * 更新用のパケットを送ります。
     * @param level パケットの送り元となるレベル
     */
    fun sendUpdatePacket(level: ServerLevel) {
        if (isRemoved) return
        // val payload: HTUpdateBlockEntityPacket = HTUpdateBlockEntityPacket.create(this) ?: return TODO
        // PacketDistributor.sendToPlayersTrackingChunk(level, ChunkPos.containing(blockPos), payload)
    }

    /**
     * 赤石信号を更新せずに保存のフラグを立てます。
     */
    protected fun setOnlySave() {
        setChanged(false)
    }

    /**
     * 赤石信号を更新しつつ保存のフラグを立てます。
     */
    override fun setChanged() {
        setChanged(true)
    }

    private var lastSaveTime: Long = 0

    protected open fun setChanged(updateComparator: Boolean) {
        val level: Level = this.level ?: return
        val time: Long = level.gameTime
        if (lastSaveTime != time) {
            level.blockEntityChanged(blockPos)
            lastSaveTime = time
        }
        if (updateComparator && !level.isClientSide) markDirtyComparator()
    }

    /**
     * 赤石信号が更新されるときに呼び出されます。
     */
    protected open fun markDirtyComparator() {}

    /**
     * ブロックのコンパレータ出力を返します。
     */
    open fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = 0

    /**
     * 隣接ブロックが更新された時に呼び出されます。
     */
    open fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, block: Block, orientation: Orientation?, movedByPiston: Boolean) {}

    //    BlockEntity    //

    final override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
        writeValue(output)
    }

    final override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        readValue(input)
    }

    final override fun getUpdatePacket(): ClientboundBlockEntityDataPacket = ClientboundBlockEntityDataPacket.create(this)

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = createReducedUpdateTag(registries)

    final override fun handleUpdateTag(input: ValueInput) {
        super.handleUpdateTag(input)
        readUpdateTag(input)
        requestModelDataUpdate()
    }

    override fun onDataPacket(net: Connection, valueInput: ValueInput) {
        super.onDataPacket(net, valueInput)
        if (valueInput.keySet().isNotEmpty()) {
            readUpdateTag(valueInput)
        }
    }
}
