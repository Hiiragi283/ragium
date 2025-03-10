package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTLaserAssemblyBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(
        RagiumBlockEntityTypes.LASER_ASSEMBLY,
        pos,
        state,
        HTMachineType.LASER_ASSEMBLY,
        HTRecipeTypes.LASER_ASSEMBLY,
        600,
    ) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    override fun onSucceeded() {
        super.onSucceeded()
        playSound(SoundEvents.BEACON_ACTIVATE)
    }
}
