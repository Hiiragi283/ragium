package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.ragium.common.block.entity.machine.base.HTSingleMultiOutputBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleMultiOutputBlockEntity(RagiumBlockEntityTypes.CUTTING_MACHINE, pos, state) {
    override fun playSound() {
        playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT)
    }

    override fun getOutputSlotSize(): Int = 2

    override fun getLookup(): HTRecipeLookup<SingleRecipeInput, out HTSingleMultiOutputRecipe> = RagiumRecipeLookups.CUTTING

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.cuttingMachine
}
