package hiiragi283.ragium.common.block.storage

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.entity.storage.HTUniversalChestBlockEntity
import hiiragi283.ragium.common.item.HTUniversalChestManager
import hiiragi283.ragium.setup.RagiumBlockEntityTypes

class HTUniversalChestBlock(properties: Properties) : HTStorageBlock(RagiumBlockEntityTypes.UNIVERSAL_CHEST, properties) {
    override fun getDescription(): HTTranslation = RagiumTranslation.UNIVERSAL_CHEST

    override fun setup(context: HTBlockWidgetHolderContext, widgetHolder: HTWidgetHolder) {
        val universalChest: HTUniversalChestBlockEntity = context.blockEntity as? HTUniversalChestBlockEntity ?: return
        val slots: List<HTItemSlot> = context.level
            .server
            ?.let { HTUniversalChestManager.getHandler(it, universalChest.color) }
            ?.getItemSlots(null)
            ?: HTUniversalChestManager.createSlots()
        slots.forEachIndexed { index: Int, slot: HTItemSlot ->
            if (slot is HTBasicItemSlot) {
                widgetHolder += HTItemWidget.Container(
                    slot,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(index / 9),
                    HTBackgroundType.NONE,
                )
            }
        }
    }
}
