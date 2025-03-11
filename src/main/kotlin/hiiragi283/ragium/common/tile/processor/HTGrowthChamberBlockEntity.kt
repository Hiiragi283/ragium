package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTGrowthChamberBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(
        RagiumBlockEntityTypes.GROWTH_CHAMBER,
        pos,
        state,
        HTRecipeTypes.GROWTH_CHAMBER,
        600,
    ) {
    override fun checkCondition(level: ServerLevel, pos: BlockPos, simulate: Boolean): Result<Unit> =
        checkEnergyConsume(level, 640, simulate)
}
