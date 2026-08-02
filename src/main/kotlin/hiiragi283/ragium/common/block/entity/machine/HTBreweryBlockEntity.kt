package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.recipe.viewer.HCRecipeViewerTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemOrFluidBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) : HTItemOrFluidBlockEntity(RagiumBlockEntityTypes.BREWERY.get(), pos, state) {
    override fun getViewerTypes(): Iterable<HTRecipeViewerType<*>> = listOf(HCRecipeViewerTypes.BREWING)

    override fun getLookup(): HTRecipeLookup<HTItemOrFluidRecipe> = HCRecipeLookups.BREWING

    override fun playSound() {
        playSound(SoundEvents.BREWING_STAND_BREW)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.brewery
}
