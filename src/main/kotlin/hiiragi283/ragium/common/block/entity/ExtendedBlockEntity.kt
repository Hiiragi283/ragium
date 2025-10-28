package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTAbstractBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockInteractContext
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.common.network.HTUpdateBlockEntityPacket
import hiiragi283.ragium.common.util.HTPacketHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import java.util.function.Consumer

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 * @see mekanism.common.tile.base.TileEntityUpdateable
 */
abstract class ExtendedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTAbstractBlockEntity {
    //    Save & Read    //

    final override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = getReducedUpdateTag(registries)

    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        loadAdditional(tag, lookupProvider)
    }

    final override fun onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket, lookupProvider: HolderLookup.Provider) {
        val tag: CompoundTag = pkt.tag
        if (!tag.isEmpty) handleUpdateTag(tag, lookupProvider)
    }

    fun sendUpdatePacket(level: ServerLevel) {
        if (isRemoved) return
        HTPacketHelper.sendToClient(level, blockPos, HTUpdateBlockEntityPacket.create(this))
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        super.setBlockState(blockState)
        afterUpdateState(blockState)
    }

    final override fun setLevel(level: Level) {
        super.setLevel(level)
        afterLevelInit(level)
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
        val level: Level = this.level ?: return
        val time: Long = level.gameTime
        if (lastSaveTime != time) {
            level.blockEntityChanged(blockPos)
            lastSaveTime = time
        }
        if (updateComparator && !isClientSide.isTrue) {
            markDirtyComparator()
        }
        (level as? ServerLevel)?.let(::sendPassivePacket)
    }

    protected open fun markDirtyComparator() {}

    protected open fun sendPassivePacket(level: ServerLevel) {}

    //    Extensions    //

    val isClientSide: TriState get() {
        val level: Level = this.level ?: return TriState.DEFAULT
        return when (level.isClientSide) {
            true -> TriState.TRUE
            false -> TriState.FALSE
        }
    }

    /**
     * @see mekanism.common.tile.base.TileEntityUpdateable.getReducedUpdateTag
     */
    fun getReducedUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveCustomOnly(registries)

    /**
     * [BlockEntity.setBlockState]の後で呼び出されます。
     */
    open fun afterUpdateState(state: BlockState) {}

    /**
     * [BlockEntity.setLevel]の後で呼び出されます。
     */
    open fun afterLevelInit(level: Level) {}

    /**
     * ブロックが右クリックされたときに呼ばれます。
     *
     * [Block.useWithoutItem]より先に呼び出されます。
     */
    open fun onRightClickedWithItem(context: HTBlockInteractContext, stack: ItemStack, hand: InteractionHand): ItemInteractionResult =
        when (this) {
            // 液体コンテナで触ると搬出入を行う
            is HTFluidInteractable -> interactWith(context.level, context.player, hand)
            else -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        }

    /**
     * [Block.useWithoutItem]でGUIを開くときに，クライアント側へ送るデータを書き込みます。
     * @see mekanism.common.tile.base.TileEntityMekanism.encodeExtraContainerData
     */
    open fun writeExtraContainerData(buf: RegistryFriendlyByteBuf) {
        buf.writeBlockPos(getBlockPos())
    }

    /**
     * ブロックが左クリックされたときに呼ばれます。
     */
    open fun onLeftClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {
    }

    /**
     * ブロックが設置されたときに呼ばれます。
     */
    open fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
    }

    /**
     * ブロックが破壊されたときにインベントリの中身をドロップします。
     */
    open fun dropInventory(consumer: Consumer<ItemStack>) {}

    open fun onRemove(level: Level, pos: BlockPos) {
        dropInventory { stack: ItemStack -> dropStackAt(level, pos, stack) }
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
