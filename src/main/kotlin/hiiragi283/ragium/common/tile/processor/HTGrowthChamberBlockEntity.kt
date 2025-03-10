package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
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
        HTMachineType.GROWTH_CHAMBER,
        HTRecipeTypes.GROWTH_CHAMBER,
        600,
    ) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL
}
