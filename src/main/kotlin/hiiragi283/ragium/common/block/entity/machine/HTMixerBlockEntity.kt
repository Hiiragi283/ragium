package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MIXER, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*, *> = TODO()

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.mixer
}
