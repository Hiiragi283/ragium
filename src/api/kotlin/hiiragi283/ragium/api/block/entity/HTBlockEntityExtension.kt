package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.storage.HTContentListener
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.Tags

interface HTBlockEntityExtension : HTContentListener {
    val isRemote: Boolean

    /**
     * @see [mekanism.common.tile.interfaces.ITileWrapper.getLevel]
     */
    fun getLevel(): Level?

    fun getLevelOrThrow(): Level = checkNotNull(getLevel()) { "Level is not initialized!" }

    fun getDimension(): ResourceKey<Level> = getLevelOrThrow().dimension()

    /**
     * @see [mekanism.common.tile.interfaces.ITileWrapper.getBlockPos]
     */
    fun getBlockPos(): BlockPos

    /**
     * @see [mekanism.common.tile.base.TileEntityUpdateable.getReducedUpdateTag]
     */
    fun getReducedUpdateTag(registries: HolderLookup.Provider): CompoundTag

    /**
     * [BlockEntity.setBlockState]の後で呼び出されます。
     */
    fun afterUpdateState(state: BlockState) {}

    /**
     * [BlockEntity.setLevel]の後で呼び出されます。
     */
    fun afterLevelInit(level: Level) {}

    /**
     * ブロックが右クリックされたときに呼ばれます。
     *
     * [onRightClicked]より先に呼び出されます。
     */
    fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = when {
        // レンチでクリックすると搬入出を設定
        stack.`is`(Tags.Items.TOOLS_WRENCH) -> {
            onRightClickedWithWrench(stack, state, level, pos, player, hand, hitResult)
        }
        // 液体コンテナで触ると搬出入を行う
        else -> when (this) {
            is HTFluidInteractable -> interactWith(level, player, hand)
            else -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        }
    }

    fun onRightClickedWithWrench(
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
    fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = InteractionResult.PASS

    /**
     * [onRightClicked]でGUIを開くときに，クライアント側へ送るデータを書き込みます。
     * @see [mekanism.common.tile.base.TileEntityMekanism.encodeExtraContainerData]
     */
    fun writeExtraContainerData(buf: RegistryFriendlyByteBuf) {
        buf.writeBlockPos(getBlockPos())
    }

    /**
     * ブロックが左クリックされたときに呼ばれます。
     */
    fun onLeftClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ) {
    }

    /**
     * ブロックが設置されたときに呼ばれます。
     */
    fun setPlacedBy(
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
    fun dropInventory(consumer: (ItemStack) -> Unit) {}

    /**
     * ブロックのコンパレータ出力を返します。
     */
    fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = 0

    /**
     * 隣接ブロックが更新された時に呼び出されます。
     */
    fun neighborChanged(
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
    fun reloadUpgrades() {}
}
