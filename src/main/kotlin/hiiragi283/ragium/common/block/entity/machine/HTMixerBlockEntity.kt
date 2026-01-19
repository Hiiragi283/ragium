package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) : HTComplexBlockEntity(RagiumBlockEntityTypes.MIXER, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*, *> =
        RecipeComponent(RagiumRecipeTypes.MIXING, SoundEvents.BUCKET_FILL_AXOLOTL)

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.mixer
}
