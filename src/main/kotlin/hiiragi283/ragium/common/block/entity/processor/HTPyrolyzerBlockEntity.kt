package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemToItemAndFluidBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) : HTItemToItemAndFluidBlockEntity(RagiumBlockEntityTypes.PYROLYZER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemToItemAndFluidRecipe> = RagiumRecipeLookups.PYROLYZING

    override fun onComplete() {
        playSound(SoundEvents.BLAZE_DEATH)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.processor.pyrolyzer
}
