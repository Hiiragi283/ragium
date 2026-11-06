package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.recipe.base.HTItemAndFluidToFluidRecipe
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTItemAndFluidToFluidBlockEntityN
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemAndFluidToFluidBlockEntityN(RagiumBlockEntityTypes.BREWERY.get(), pos, state) {
    override fun getLookup(): HTRecipeLookup<HTItemAndFluidToFluidRecipe> = RagiumRecipeLookups.BREWING

    override fun onComplete() {
        playSound(SoundEvents.BREWING_STAND_BREW)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.brewery
}
