package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BoneMealItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.util.TriState

class HTSprinklerBlockEntity(pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(RagiumBlockEntityTypes.SPRINKLER, pos, state),
    HTFluidTankHandler {
    private val inputTank: HTFluidTank = HTFluidTank.create("input_tank", this) {
        validator = { variant: HTFluidVariant -> variant.isIn(Tags.Fluids.WATER) }
    }

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        inputTank.readNbt(nbt, registryOps)
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = interactWith(level, player, hand)

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tick毎に一度実行する
        if (totalTick % 20 != 0) return TriState.DEFAULT
        // 高さを0~2の範囲でチェックする
        for (height: Int in (0..2)) {
            if (glowCrop(level, pos, height).isTrue) {
                return TriState.TRUE
            }
        }
        return TriState.DEFAULT
    }

    private fun glowCrop(level: ServerLevel, pos: BlockPos, height: Int): TriState {
        // 範囲内のランダムなブロックを対象とする
        val targetPos: BlockPos = BlockPos
            .betweenClosedStream(-4, height, -4, 4, height, 4)
            .map(pos::offset)
            .filter { posIn: BlockPos -> posIn != pos }
            .toList()
            .random()
        // 水を消費できない場合はスキップ
        if (!inputTank.canExtract(50)) return TriState.DEFAULT
        // ランダムチックを呼び出す
        if (BoneMealItem.applyBonemeal(ItemStack.EMPTY, level, targetPos, null)) {
            inputTank.extract(50, false)
            return TriState.TRUE
        }
        return TriState.DEFAULT
    }

    //    Fluid    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getFluidTank(tank: Int): HTFluidTank? = inputTank

    override fun getTanks(): Int = 1
}
