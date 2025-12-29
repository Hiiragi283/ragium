package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTPyrolyzerBlockEntity(pos: BlockPos, state: BlockState) :
    HTAbstractComplexBlockEntity<HTPyrolyzingRecipe>(
        RagiumRecipeTypes.PYROLYZING,
        RagiumBlockEntityTypes.PYROLYZER,
        pos,
        state,
    ) {
    override fun playSound() {
        playSound(SoundEvents.BLAZE_AMBIENT)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.pyrolyzer
}
