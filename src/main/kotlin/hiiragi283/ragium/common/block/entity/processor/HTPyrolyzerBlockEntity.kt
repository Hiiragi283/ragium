package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemSeparatingBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) : HTItemSeparatingBlockEntity(RagiumBlockEntityTypes.PYROLYZER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemSeparatingRecipe> = RagiumRecipeLookups.PYROLYZING

    override fun onComplete() {
        playSound(SoundEvents.BLAZE_DEATH)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.pyrolyzer
}
