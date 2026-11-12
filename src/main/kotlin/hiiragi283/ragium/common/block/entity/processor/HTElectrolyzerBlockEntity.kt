package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTFluidSeparatingBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTElectrolyzerBlockEntity(pos: BlockPos, state: BlockState) : HTFluidSeparatingBlockEntity(RagiumBlockEntityTypes.REFINERY.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTFluidSeparatingRecipe> = RagiumRecipeLookups.ELECTROLYZING

    override fun onComplete() {
        playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.electrolyzer
}
