package hiiragi283.ragium.common.block.entity

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTHandlerBlockEntity
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.network.HTBlockEntityUpdatePacket
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper
import net.neoforged.neoforge.network.PacketDistributor
import org.slf4j.Logger
import java.util.*

/**
 * Ragiumで使用する[BlockEntity]の拡張クラス
 */
abstract class HTBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type.get(), pos, state),
    HTHandlerBlockEntity,
    HTNbtCodec {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Save & Load    //

    /**
     * クライアント側へ同期する際に送る[CompoundTag]
     */
    final override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveCustomOnly(registries)

    /**
     * クライアント側に同期パケットを送る
     */
    fun sendUpdatePacket(serverLevel: ServerLevel) {
        PacketDistributor.sendToPlayersTrackingChunk(
            serverLevel,
            ChunkPos(blockPos),
            HTBlockEntityUpdatePacket(blockPos, getUpdateTag(serverLevel.registryAccess())),
        )
    }

    /**
     * クライアント側でパケットを受け取った時の処理
     */
    final override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        loadAdditional(tag, lookupProvider)
    }

    val upgrades = HTItemStackHandler(4, ::onUpgradeUpdated)

    @Suppress("unused")
    private fun onUpgradeUpdated(index: Int) {
        reloadUpgrades()
        setChanged()
    }

    var outputSide: Direction? = null
        protected set

    final override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val writer: HTNbtCodec.Writer = object : HTNbtCodec.Writer {
            override fun <T : Any> write(codec: Codec<T>, key: String, value: T) {
                codec
                    .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), value)
                    .ifSuccess { tag.put(key, it) }
                    .ifError { error: DataResult.Error<Tag> -> LOGGER.error(error.message()) }
            }

            override fun <T : Any> writeNullable(codec: Codec<T>, key: String, value: T?) {
                write(ExtraCodecs.optionalEmptyMap(codec), key, Optional.ofNullable(value))
            }

            override fun write(key: String, serializable: INBTSerializable<CompoundTag>) {
                tag.put(key, serializable.serializeNBT(registries))
            }
        }
        // Target Side
        writer.writeNullable(Direction.CODEC, RagiumConstantValues.TARGET_SIDE, outputSide)
        // Upgrades
        writer.write(RagiumConstantValues.UPGRADES, upgrades)
        // Custom
        writeNbt(writer)
    }

    final override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        val reader: HTNbtCodec.Reader = object : HTNbtCodec.Reader {
            override fun <T : Any> read(codec: Codec<T>, key: String): DataResult<T> = codec
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get(key))

            override fun read(key: String, serializable: INBTSerializable<CompoundTag>) {
                serializable.deserializeNBT(registries, tag.getCompound(key))
            }
        }
        // Target Side
        reader
            .read(Direction.CODEC, RagiumConstantValues.TARGET_SIDE)
            .ifSuccess { direction: Direction -> outputSide = direction }
        // Upgrades
        reader.read(RagiumConstantValues.UPGRADES, upgrades)
        // Custom
        readNbt(reader)
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

    //    Extension    //

    /**
     * [setBlockState]の後で呼び出されます。
     */
    protected open fun afterUpdateState(state: BlockState) {}

    /**
     * [setLevel]の後で呼び出されます。
     */
    protected open fun afterLevelInit(level: Level) {}

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
    ): ItemInteractionResult {
        // レンチでクリックすると出力面を設定
        if (stack.`is`(Tags.Items.TOOLS_WRENCH)) {
            this.outputSide = hitResult.direction
            level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        } else if (stack.`is`(RagiumCommonTags.Items.PAPER)) {
            // 紙でクリックすると出力面を消去
            this.outputSide = null
            level.playSound(null, pos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        // 液体コンテナで触ると搬出入を行う
        val fluidHandler: IFluidHandler? = getFluidHandler(null)
        return when (fluidHandler) {
            is HTFluidHandler -> fluidHandler.interactWith(level, player, hand)
            else -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        }
    }

    /**
     * ブロックが右クリックされたときに呼ばれます。
     */
    open fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (this is MenuProvider) {
            if (!level.isClientSide) {
                player.openMenu(this, pos)
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return InteractionResult.PASS
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
     * ブロックが置換されたときに呼ばれます。
     */
    open fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        upgrades.dropStacksAt(level, pos)
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
     * アップグレードが更新されたときに呼び出されます。
     */
    protected open fun reloadUpgrades() {}

    /**
     * 対象となるブロックにアイテムを移動します
     */
    protected fun exportItems(level: ServerLevel, pos: BlockPos) {
        val handler: IItemHandler = getItemHandler(null) ?: return
        val targetSide: Direction = this.outputSide ?: return
        val outputHandler: IItemHandler? =
            level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(targetSide), targetSide.opposite)
        if (outputHandler != null) {
            for (slot: Int in (0 until handler.slots)) {
                var stack: ItemStack = handler.extractItem(slot, 64, true)
                if (stack.isEmpty) continue
                stack = handler.getStackInSlot(slot)
                if (ItemHandlerHelper.insertItem(outputHandler, stack, false).isEmpty) {
                    handler.extractItem(slot, stack.count, false)
                }
            }
        }
    }

    /**
     * 対象となるブロックに液体を移動します
     */
    protected fun exportFluids(level: ServerLevel, pos: BlockPos) {
        val handler: IFluidHandler = getFluidHandler(null) ?: return
        val targetSide: Direction = this.outputSide ?: return
        val outputHandler: IFluidHandler? =
            level.getCapability(Capabilities.FluidHandler.BLOCK, pos.relative(targetSide), targetSide.opposite)
        if (outputHandler != null) {
            FluidUtil.tryFluidTransfer(outputHandler, handler, Int.MAX_VALUE, true)
        }
    }
}
