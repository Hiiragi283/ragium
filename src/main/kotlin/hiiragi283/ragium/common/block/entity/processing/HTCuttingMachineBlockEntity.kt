package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTChancedBlockEntity(RagiumBlockEntityTypes.CUTTING_MACHINE, pos, state) {
    override fun getOutputSlotSize(): Int = 2

    override fun createRecipeComponent(): HTRecipeComponent<*, *> =
        RecipeComponent(RagiumRecipeTypes.CUTTING, SoundEvents.UI_STONECUTTER_TAKE_RESULT)

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.cuttingMachine
}
