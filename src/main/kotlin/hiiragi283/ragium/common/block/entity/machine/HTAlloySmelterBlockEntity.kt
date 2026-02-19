package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.block.entity.machine.base.HTCombineItemRecipeBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTCombineItemRecipeBlockEntity(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> =
        CombineRecipeComponent(RagiumRecipeTypes.ALLOYING) { it.playSound(SoundEvents.FIRE_EXTINGUISH) }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.alloySmelter
}
