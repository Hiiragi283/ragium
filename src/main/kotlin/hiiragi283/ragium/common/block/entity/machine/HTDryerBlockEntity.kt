package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemOrFluidBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTDryerBlockEntity(pos: BlockPos, state: BlockState) : HTItemOrFluidBlockEntity(RagiumBlockEntityTypes.DRYER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler =
        createRecipeHandler(RagiumRecipeLookups.DRYING, HTSoundInstance(SoundEvents.FIRE_EXTINGUISH))

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.dryer
}
