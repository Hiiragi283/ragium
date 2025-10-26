package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.extension.randomKt
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.BoneMealItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState

class HTSprinklerBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity.Tickable(TODO() as Holder<Block>, pos, state) {
    private val tank: HTFluidStackTank = TODO()

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    private fun glowCrop(level: ServerLevel, pos: BlockPos, height: Int): TriState {
        // 範囲内のランダムなブロックを対象とする
        val targetPos: BlockPos = BlockPos
            .betweenClosedStream(-4, height, -4, 4, height, 4)
            .map(pos::offset)
            .filter { posIn: BlockPos -> posIn != pos }
            .toList()
            .random(level.randomKt)
        // 水を消費できない場合はスキップ
        if (tank.extract(50, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty()) return TriState.DEFAULT
        // ランダムチックを呼び出す
        if (BoneMealItem.applyBonemeal(ItemStack.EMPTY, level, targetPos, null)) {
            tank.extract(50, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
            return TriState.TRUE
        }
        return TriState.DEFAULT
    }
}
