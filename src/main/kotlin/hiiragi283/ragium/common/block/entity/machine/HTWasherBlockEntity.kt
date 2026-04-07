package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.recipe.base.HTItemFluidMultiOutputRecipe
import hiiragi283.ragium.common.block.entity.machine.base.HTItemFluidMultiOutputBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTWasherBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemFluidMultiOutputBlockEntity(RagiumBlockEntityTypes.WASHER, pos, state) {
    override fun playSound() {
        playSound(SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE)
    }

    override fun getOutputSlotSize(): Int = 4

    override fun getLookup(): HTRecipeLookup<HTItemAndFluidRecipeInput, out HTItemFluidMultiOutputRecipe> = RagiumRecipeLookups.WASHING

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.washer
}
