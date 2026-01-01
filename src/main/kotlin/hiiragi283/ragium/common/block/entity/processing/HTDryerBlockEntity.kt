package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTDryerBlockEntity(pos: BlockPos, state: BlockState) : HTAbstractComplexBlockEntity(RagiumBlockEntityTypes.DRYER, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*> = RecipeComponent(RagiumRecipeTypes.DRYING, SoundEvents.SPONGE_ABSORB)

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.dryer
}
