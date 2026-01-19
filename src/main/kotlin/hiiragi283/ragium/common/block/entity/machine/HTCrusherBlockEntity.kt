package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) : HTChancedBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state) {
    override fun getOutputSlotSize(): Int = 3

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent(RagiumRecipeTypes.CRUSHING, SoundEvents.GRINDSTONE_USE)

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.crusher
}
