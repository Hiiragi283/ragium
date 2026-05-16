package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) : HTItemToItemBlockEntity.Simple(RagiumBlockEntityTypes.COMPRESSOR, pos, state) {
    override fun getViewerTypes(): Iterable<HTRecipeViewerType<*>> = listOf(RagiumRecipeViewerTypes.COMPRESSING)

    override fun getLookup(): HTRecipeLookup<out HTItemToItemRecipe> = RagiumRecipeLookups.COMPRESSING

    override fun playSound() {
        playSound(SoundEvents.WOOL_PLACE)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.compressor
}
