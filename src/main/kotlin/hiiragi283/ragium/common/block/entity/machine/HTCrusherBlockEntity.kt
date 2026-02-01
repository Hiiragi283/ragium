package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) : HTItemToChancedBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state) {
    override fun setupOutputSlots(widgetHolder: HTWidgetHolder) {
        widgetHolder += HTItemSlotWidget(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )

        for (i: Int in extraOutputSlots.indices) {
            val slot: HTBasicItemSlot = extraOutputSlots[i]
            widgetHolder += HTItemSlotWidget(
                slot,
                HTSlotHelper.getSlotPosX(7.5),
                HTSlotHelper.getSlotPosY(i),
                HTBackgroundType.EXTRA_OUTPUT,
            )
        }
    }

    override fun getOutputSlotSize(): Int = 3

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent(RagiumRecipeTypes.CRUSHING, SoundEvents.GRINDSTONE_USE)

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.crusher
}
