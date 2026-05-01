package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.cache.HTTripleInputRecipeCache
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.impl.recipe.cache.HTAlloyingCompletedRecipe
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    private lateinit var topInputSlot: HTBasicItemSlot
    private lateinit var leftInputSlot: HTBasicItemSlot
    private lateinit var rightInputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        topInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { leftInputSlot.getResource() != it && rightInputSlot.getResource() != it },
            ),
        )
        leftInputSlot = builder.addSlot(
            HTSlotInfo.EXTRA_INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { rightInputSlot.getResource() != it && topInputSlot.getResource() != it },
            ),
        )
        rightInputSlot = builder.addSlot(
            HTSlotInfo.EXTRA_INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { topInputSlot.getResource() != it && leftInputSlot.getResource() != it },
            ),
        )

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemSlotWidget.container(
            topInputSlot,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            leftInputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            rightInputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT,
        )

        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTAlloyingRecipe, HTAlloyingCompletedRecipe>() {
        private val cache = AlloyingCache()
        private val topInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(topInputSlot) }
        private val leftInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(leftInputSlot) }
        private val rightInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(rightInputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTAlloyingRecipe? =
            cache.findFirstRecipe(topInputHandler.getStack(), leftInputHandler.getStack(), rightInputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTAlloyingRecipe): HTAlloyingCompletedRecipe = HTAlloyingCompletedRecipe(
            recipe,
            topInputHandler,
            leftInputHandler,
            rightInputHandler,
            outputHandler,
        )

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTAlloyingCompletedRecipe) {
            recipe.complete()
            playSound(SoundEvents.FIRE_EXTINGUISH)
        }
    }

    private class AlloyingCache :
        HTTripleInputRecipeCache<ItemStack, ItemStack, ItemStack, HTAlloyingRecipe>(RagiumRecipeLookups.ALLOYING) {
        override fun isEmpty(firstInput: ItemStack, secondInput: ItemStack, thirdInput: ItemStack): Boolean =
            firstInput.isEmpty || secondInput.isEmpty
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.alloySmelter
}
