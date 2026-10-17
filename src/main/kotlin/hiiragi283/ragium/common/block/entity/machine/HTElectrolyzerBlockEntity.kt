package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTFluidSeparatingBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTElectrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidSeparatingBlockEntity(RagiumBlockEntityTypes.ELECTROLYZER.get(), pos, state) {
    override fun createRecipeHandler(): HTRecipeHandler<*, *, *> = createRecipeHandler(
        RagiumRecipeLookups.ELECTROLYZING,
        HTSoundInstance(SoundEvents.FIREWORK_ROCKET_TWINKLE)
    )

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.electrolyzer
}
