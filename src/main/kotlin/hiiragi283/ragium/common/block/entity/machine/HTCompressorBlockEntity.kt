package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(RagiumBlockEntityTypes.COMPRESSOR.get(), RagiumRecipeLookups.COMPRESSING, pos, state) {
    override fun getCompletedSound(): HTSoundInstance = HTSoundInstance(SoundEvents.ANVIL_LAND)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.compressor
}
