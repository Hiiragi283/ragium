package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTTypedProgressHandler
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTScannerBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.SCANNER.get(), pos, state) {
    private lateinit var topInputSlot: HTBasicItemSlot
    private lateinit var downInputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        topInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { resource: HTItemResourceType -> resource.isOf(RagiumItems.MEMORY_DISC.key) },
            ),
        )
        downInputSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemWidget.Container(
            topInputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(topInputSlot)
        widgetHolder += HTItemWidget.Container(
            downInputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(downInputSlot)

        widgetHolder += HTItemWidget.Container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : HTTypedProgressHandler<ItemStack>() {
        private val topInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(topInputSlot) }
        private val downInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(downInputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findRecipe(level: ServerLevel, pos: BlockPos): ItemStack? {
            if (topInputHandler.isEmpty()) return null
            val resourceIn: HTItemResourceType = downInputHandler.getResource() ?: return null
            return RagiumDataComponents.createMemoryDisc(resourceIn)
        }

        override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: ItemStack): Boolean = outputHandler.canInsert(recipe)

        override fun getMaxProgress(recipe: ItemStack): Int = updateAndGetProgress(20 * 30)

        override fun getProgress(level: ServerLevel, pos: BlockPos): Int = 1

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: ItemStack) {
            outputHandler.insert(recipe)
            topInputHandler.consume(1)
        }
    }

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.scanner
}
