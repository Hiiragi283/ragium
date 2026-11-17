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

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) : HTItemToItemAndFluidBlockEntity(RagiumBlockEntityTypes.EXTRACTOR.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemToItemAndFluidRecipe> = RagiumRecipeLookups.EXTRACTING

    override fun onComplete() {
        playSound(SoundEvents.SLIME_BLOCK_BREAK)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.processor.extractor
}
