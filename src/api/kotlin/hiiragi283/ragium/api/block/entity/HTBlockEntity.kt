package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.extension.sendUpdatePacket
import hiiragi283.ragium.api.registry.HTDeferredMachine
import hiiragi283.ragium.api.util.HTNbtCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Supplier

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 */
abstract class HTBlockEntity(type: Supplier<out BlockEntityType<*>>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTHandlerBlockEntity,
    HTNbtCodec {
    companion object {
        const val ENCH_KEY = "enchantment"
        const val OWNER_KEY = "owner"

        @JvmStatic
        fun <T : BlockEntity> getTicker(): BlockEntityTicker<T> =
            BlockEntityTicker<T> { level: Level, pos: BlockPos, state: BlockState, blockEntity: T ->
                if (blockEntity is HTBlockEntity) {
                    blockEntity.tick(level, pos, state)
                }
            }
    }

    constructor(machine: HTDeferredMachine<*, *>, pos: BlockPos, state: BlockState) : this(
        machine.blockEntityHolder,
        pos,
        state,
    )

    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        CompoundTag().apply { saveAdditional(this, registries) }

    final override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        super.handleUpdateTag(tag, lookupProvider)
        loadAdditional(tag, lookupProvider)
    }

    override fun setChanged() {
        super.setChanged()
        sendUpdatePacket()
    }

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        writeNbt(tag, registries.createSerializationContext(NbtOps.INSTANCE))
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        readNbt(tag, registries.createSerializationContext(NbtOps.INSTANCE))
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        beforeUpdateState(blockState)
        super.setBlockState(blockState)
        afterUpdateState(blockState)
    }

    //    Extension    //

    /**
     * [BlockState]が更新される前に呼び出されます。
     */
    protected open fun beforeUpdateState(state: BlockState) {}

    /**
     * [BlockState]が更新された後に呼び出されます。
     */
    protected open fun afterUpdateState(state: BlockState) {}

    /**
     * ブロックが右クリックされたときに呼ばれます。
     *
     * [onRightClicked]より先に呼び出されます。
     */
    open fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

    /**
     * ブロックが右クリックされたときに呼ばれます。
     */
    open fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = InteractionResult.PASS

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
     * ブロックが置換されたときに呼ばれます。
     */
    open fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
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

    /**
     * このブロックエンティティが生成されてからの経過時間
     *
     * セーブのたびにリセットされる
     */
    var totalTick: Int = 0
        protected set

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        when (level.isClientSide) {
            true -> clientTick(level, pos, state)
            false -> serverTick(level, pos, state)
        }
    }

    protected open fun clientTick(level: Level, pos: BlockPos, state: BlockState) {
        totalTick++
    }

    protected open fun serverTick(level: Level, pos: BlockPos, state: BlockState) {
        totalTick++
    }
}
