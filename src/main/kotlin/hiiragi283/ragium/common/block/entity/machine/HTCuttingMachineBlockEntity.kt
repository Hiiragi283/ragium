package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToChancedBlockEntity(RagiumBlockEntityTypes.CUTTING_MACHINE, pos, state) {
    override fun setupOutputSlots(widgetHolder: HTWidgetHolder) {
        widgetHolder += HTItemSlotWidget(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.OUTPUT,
        )

        widgetHolder += HTItemSlotWidget(
            extraOutputSlots[0],
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_OUTPUT,
        )
    }

    override fun getOutputSlotSize(): Int = 2

    override fun createRecipeComponent(): HTRecipeComponent<*, *> =
        RecipeComponent(RagiumRecipeTypes.CUTTING, SoundEvents.UI_STONECUTTER_TAKE_RESULT)

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.cuttingMachine
}
