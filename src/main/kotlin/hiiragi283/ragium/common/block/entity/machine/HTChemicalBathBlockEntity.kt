package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemAndFluidToItemBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTChemicalBathBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemAndFluidToItemBlockEntity(
        RagiumBlockEntityTypes.CHEMICAL_BATH.get(),
        RagiumRecipeLookups.BATHING,
        pos,
        state
    ) {
    override fun getCompletedSound(): HTSoundInstance = HTSoundInstance(SoundEvents.SPONGE_ABSORB)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.chemicalBath
}
