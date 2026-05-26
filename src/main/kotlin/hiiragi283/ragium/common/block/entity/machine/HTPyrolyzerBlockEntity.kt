package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.ragium.common.block.entity.machine.base.HTItemOrFluidBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) : HTItemOrFluidBlockEntity(RagiumBlockEntityTypes.PYROLYZER, pos, state) {
    override fun getViewerTypes(): Iterable<HTRecipeViewerType<*>> = listOf(RagiumRecipeViewerTypes.PYROLYZING)

    override fun getLookup(): HTRecipeLookup<HTItemOrFluidRecipe> = RagiumRecipeLookups.PYROLYZING

    override fun playSound() {
        playSound(SoundEvents.FIRE_EXTINGUISH)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.pyrolyzer
}
