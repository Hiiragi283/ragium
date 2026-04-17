package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleItemBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected lateinit var inputSlot: HTBasicItemSlot
        private set
    protected lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )

        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<SingleRecipeInput, HTSingleItemRecipe>() {
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun createInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = createInput(inputHandler)

        override fun findRecipe(level: ServerLevel, pos: BlockPos, input: SingleRecipeInput): HTSingleItemRecipe? =
            getCache().getFirstRecipe(input, level)

        override fun canComplete(
            level: ServerLevel,
            pos: BlockPos,
            recipe: HTHandledRecipe<SingleRecipeInput, HTSingleItemRecipe>,
        ): Boolean = recipe.assemble(level.registryAccess()).let(outputHandler::canInsert)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<SingleRecipeInput, HTSingleItemRecipe>) {
            // output
            recipe.assemble(level.registryAccess()).let(outputHandler::insert)
            // input
            inputHandler.consume(recipe.map(HTSingleItemRecipe::getRequiredAmount))

            playSound()
        }
    }

    final override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    protected abstract fun getCache(): HTRecipeCache<SingleRecipeInput, out HTSingleItemRecipe>

    protected abstract fun playSound()

    //    Simple    //

    abstract class Simple(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTSingleItemBlockEntity(type, pos, state) {
        //    Serialize    //

        private val cache: HTRecipeCache<SingleRecipeInput, out HTSingleItemRecipe> = getCache()

        override fun writeValue(output: HTValueOutput) {
            super.writeValue(output)
            cache.serialize(output)
        }

        override fun readValue(input: HTValueInput) {
            super.readValue(input)
            cache.deserialize(input)
        }

        final override fun getCache(): HTRecipeCache<SingleRecipeInput, out HTSingleItemRecipe> = HTLookupRecipeCache.forRecipe(getLookup())

        protected abstract fun getLookup(): HTRecipeLookup<SingleRecipeInput, out HTSingleItemRecipe>
    }
}
