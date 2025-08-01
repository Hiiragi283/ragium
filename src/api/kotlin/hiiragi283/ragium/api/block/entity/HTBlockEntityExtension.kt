package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.tag.RagiumCommonTags
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.capability.IFluidHandler

interface HTBlockEntityExtension : HTHandlerBlockEntity {
    val upgrades: HTItemHandler
    var outputSide: Direction?

    /**
     * [net.minecraft.world.level.block.entity.BlockEntity.setBlockState]の後で呼び出されます。
     */
    fun afterUpdateState(state: BlockState) {}

    /**
     * [net.minecraft.world.level.block.entity.BlockEntity.setLevel]の後で呼び出されます。
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
    fun onRightClicked(
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
    fun dropInventory(consumer: (ItemStack) -> Unit) {
        upgrades.getStackView().forEach(consumer)
    }

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
