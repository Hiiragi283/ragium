package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.state.BlockState

class HTWaterCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidCollectorBlockEntity(RagiumBlocks.WATER_COLLECTOR, pos, state) {
    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 液体を生成できるかチェック
        val stack: ImmutableFluidStack = getGeneratedFluid(level, pos) ?: return false
        // 液体を搬入できるかチェック
        if (tank.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) != null) return false
        tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS)
        return true
    }

    private fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): ImmutableFluidStack? {
        var amount = 0
        // 海洋バイオームまたは河川系バイオームの場合 -> +1000 mB
        val biome: Holder<Biome> = level.getBiome(pos)
        if (biome.`is`(BiomeTags.IS_OCEAN) || biome.`is`(BiomeTags.IS_RIVER)) {
            amount += 1000
        }
        // 周囲に2ブロック以上の水源がある -> +500 mB
        val waterSources: Int = Direction.Plane.HORIZONTAL
            .count { direction: Direction -> level.getFluidState(pos.relative(direction)).`is`(FluidTags.WATER) }
        if (waterSources >= 2) {
            amount += 500
        }
        return HTFluidHolderLike.WATER.toImmutableStack(amount)
    }
}
