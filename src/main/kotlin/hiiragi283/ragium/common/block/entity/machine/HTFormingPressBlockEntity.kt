package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.block.entity.machine.base.HTItemAndItemBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTFormingPressBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemAndItemBlockEntity(RagiumBlockEntityTypes.FORMING_PRESS, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent(RagiumRecipeTypes.PRESSING) {
        playSound(SoundEvents.ANVIL_HIT)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.formingPress
}
