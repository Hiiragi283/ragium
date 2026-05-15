package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToMultiItemBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) : HTItemToMultiItemBlockEntity(RagiumBlockEntityTypes.CRUSHER, pos, state) {
    override fun playSound() {
        playSound(SoundEvents.GRINDSTONE_USE)
    }

    override fun getOutputSlotSize(): Int = 4

    override fun setupOutputSlots(widgetHolder: HTWidgetHolder) {
        outputSlots
            .mapIndexed { index: Int, slot: HTBasicItemSlot ->
                HTItemWidget.Container(
                    slot,
                    HTSlotHelper.getSlotPosX(6 + index % 2),
                    HTSlotHelper.getSlotPosY(0.5 + index / 2),
                    HTBackgroundType.OUTPUT,
                )
            }.forEach(widgetHolder::addWidget)
    }

    override fun getLookup(): HTRecipeLookup<out HTItemToMultiItemRecipe> = HCRecipeLookups.CRUSHING

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.crusher
}
