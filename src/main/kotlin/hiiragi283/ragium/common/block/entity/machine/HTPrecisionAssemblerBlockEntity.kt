package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTTripleItemToItemBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPrecisionAssemblerBlockEntity(pos: BlockPos, state: BlockState) :
    HTTripleItemToItemBlockEntity(
        RagiumBlockEntityTypes.PRECISION_ASSEMBLER.get(),
        RagiumRecipeLookups.ASSEMBLING,
        pos,
        state
    ) {
    override fun getCompletedSound(): HTSoundInstance = HTSoundInstance(SoundEvents.CRAFTER_CRAFT)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.precisionAssembler
}
