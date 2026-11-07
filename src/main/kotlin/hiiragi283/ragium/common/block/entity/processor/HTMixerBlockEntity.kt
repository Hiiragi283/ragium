package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemAndFluidToFluidBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) : HTItemAndFluidToFluidBlockEntity(RagiumBlockEntityTypes.MIXER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemAndFluidToFluidRecipe> = RagiumRecipeLookups.MIXING

    override fun onComplete() {
        playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.mixer
}
