package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToMultiItemBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCuttingMachineBlockEntity(pos: BlockPos, state: BlockState) : HTItemToMultiItemBlockEntity(RagiumBlockEntityTypes.CUTTING_MACHINE.get(), pos, state) {
    override fun playSound() {
        playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT)
    }

    override fun getOutputSlotSize(): Int = 2

    override fun setupOutputSlots(widgetHolder: HTWidgetHolder) {
        val (firstOutput: HTBasicItemSlot, secondOutput: HTBasicItemSlot) = outputSlots
        widgetHolder += HTItemWidget.Container(
            firstOutput,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(firstOutput)
        widgetHolder += HTItemWidget.Container(
            secondOutput,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_OUTPUT,
        )
        widgetHolder.track(secondOutput)
    }

    override fun getViewerTypes(): Iterable<HTRecipeViewerType<*>> = listOf(RagiumRecipeViewerTypes.CUTTING)

    override fun getLookup(): HTRecipeLookup<HTItemToMultiItemRecipe> = RagiumRecipeLookups.CUTTING

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.cuttingMachine
}
