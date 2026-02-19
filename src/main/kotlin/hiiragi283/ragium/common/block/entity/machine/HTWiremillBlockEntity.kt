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

class HTWiremillBlockEntity(pos: BlockPos, state: BlockState) : HTItemToItemBlockEntity(RagiumBlockEntityTypes.WIREMILL, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent(RagiumRecipeTypes.WIRING) {
        playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT, pitch = 1.5f)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.wiremill
}
