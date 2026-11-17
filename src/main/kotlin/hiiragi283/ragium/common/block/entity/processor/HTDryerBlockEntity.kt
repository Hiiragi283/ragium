package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTFluidToItemAndFluidRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTFluidToItemAndFluidBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTDryerBlockEntity(pos: BlockPos, state: BlockState) : HTFluidToItemAndFluidBlockEntity(RagiumBlockEntityTypes.DRYER.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTFluidToItemAndFluidRecipe> = RagiumRecipeLookups.DRYING

    override fun onComplete() {
        playSound(SoundEvents.WET_SPONGE_DRIES)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.processor.dryer
}
