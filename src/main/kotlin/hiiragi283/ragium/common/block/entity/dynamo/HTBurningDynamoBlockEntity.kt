package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTBurningDynamoBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidDynamoBlockEntity(RagiumDataMaps.BURNING_FUEL, RagiumBlockEntityTypes.BURNING_DYNAMO, pos, state) {
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()
    override val sound: SoundEvent = SoundEvents.BLAZE_AMBIENT
}
