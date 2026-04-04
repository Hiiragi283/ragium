package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToChancedBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToChancedBlockEntity(RagiumBlockEntityTypes.CUTTING_MACHINE, pos, state) {
    override fun playSound() {
        playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT)
    }

    override fun getLookup(): HTRecipeLookup<SingleRecipeInput, out HTItemToChancedRecipe, *> = RagiumRecipeTypes.CUTTING

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.cuttingMachine
}
