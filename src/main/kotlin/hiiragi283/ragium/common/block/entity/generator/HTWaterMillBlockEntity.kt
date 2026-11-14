package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.transfer.transaction.Transaction

/**
 * 参考 : [TechReborn - WaterMillBlockEntity](https://github.com/TechReborn/TechReborn/blob/26.3/src/main/java/techreborn/blockentity/generator/basic/WaterMillBlockEntity.java)
 */
class HTWaterMillBlockEntity(pos: BlockPos, state: BlockState) : HTGeneratorBlockEntity(RagiumBlockEntityTypes.WATER_MILL.get(), pos, state) {
    private var adjacentWater = 0

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (level.gameTime % 20 == 0L) {
            updateAdjacentWater(level, pos)
        }
        return useTransaction { transaction: Transaction ->
            val inserted: Int = generateEnergy(transaction, adjacentWater)
            if (inserted > 0) {
                transaction.commit()
            }
            inserted
        } > 0
    }

    private fun updateAdjacentWater(level: ServerLevel, pos: BlockPos) {
        adjacentWater = 0
        for (direction: Direction in Direction.Plane.HORIZONTAL.stream()) {
            val state: FluidState = level.getFluidState(pos.relative(direction))
            if (state.`is`(Tags.Fluids.WATER) && state.getValueOrElse(FlowingFluid.FALLING, false)) adjacentWater++
        }
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.generator.waterMill
}
