package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemAndFluidToFluidBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemAndFluidToFluidBlockEntity(RagiumBlockEntityTypes.BREWERY.get(), RagiumRecipeLookups.BREWING, pos, state) {
    override fun getCompletedSound(): HTSoundInstance = HTSoundInstance(SoundEvents.BREWING_STAND_BREW)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.brewery
}
