package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(
        RagiumBlockEntityTypes.COMPRESSOR,
        pos,
        state,
        HTMachineType.COMPRESSOR,
        HTRecipeTypes.COMPRESSOR,
    ) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT
}
