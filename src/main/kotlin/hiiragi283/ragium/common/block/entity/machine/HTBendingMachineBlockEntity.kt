package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTBendingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(RagiumBlockEntityTypes.BENDING_MACHINE, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent(RagiumRecipeTypes.BENDING) {
        playSound(SoundEvents.ANVIL_PLACE, pitch = 0.5f)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.bendingMachine
}
