package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
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
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemSlotWidget.container(
            topInputSlot,
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            leftInputSlot,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT,
        )
        widgetHolder += HTItemSlotWidget.container(
            rightInputSlot,
            HTSlotHelper.getSlotPosX(2),
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

    //    Serialize    //

    private val cache: HTRecipeCache<HTAlloyingRecipe.Input, HTAlloyingRecipe> = HTLookupRecipeCache.forRecipe(RagiumRecipeLookups.ALLOYING)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTAlloyingRecipe.Input, HTAlloyingRecipe>() {
        private val topInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(topInputSlot) }
        private val leftInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(leftInputSlot) }
        private val rightInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(rightInputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun createInput(level: ServerLevel, pos: BlockPos): HTAlloyingRecipe.Input? {
            val topStack: ItemStack = topInputSlot.getStack()
            val leftStack: ItemStack = leftInputSlot.getStack()
            if (topStack.isEmpty || leftStack.isEmpty) return null
            return HTAlloyingRecipe.Input(topStack, leftStack, rightInputSlot.getStack())
        }

        override fun findRecipe(level: ServerLevel, pos: BlockPos, input: HTAlloyingRecipe.Input): HTAlloyingRecipe? =
            cache.getFirstRecipe(input, level)

        override fun canComplete(
            level: ServerLevel,
            pos: BlockPos,
            recipe: HTHandledRecipe<HTAlloyingRecipe.Input, HTAlloyingRecipe>,
        ): Boolean = recipe.assemble(true).let(outputHandler::canInsert)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<HTAlloyingRecipe.Input, HTAlloyingRecipe>) {
            // output
            recipe.assemble(false).let(outputHandler::insert)
            // input
            val recipe: HTAlloyingRecipe = recipe.recipe
            topInputHandler.consume(recipe.primary)
            leftInputHandler.consume(recipe.secondary)
            rightInputHandler.consume(recipe.tertiary)
            playSound(SoundEvents.FIRE_EXTINGUISH)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.alloySmelter
}
