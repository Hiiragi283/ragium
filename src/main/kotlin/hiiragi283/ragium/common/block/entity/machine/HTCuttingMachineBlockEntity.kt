package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToDoubleItemBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) : HTItemToDoubleItemBlockEntity(RagiumBlockEntityTypes.CUTTING_MACHINE.get(), RagiumRecipeLookups.CUTTING, pos, state) {
    override fun getCompletedSound(): HTSoundInstance = HTSoundInstance(SoundEvents.UI_STONECUTTER_TAKE_RESULT)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.cuttingMachine
}
