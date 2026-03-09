package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTChancedRecipe
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTChancedBlockEntity<INPUT : RecipeInput, RECIPE : HTChancedRecipe<INPUT>>(
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity.Energized(type, pos, state) {
    protected lateinit var outputSlot: HTBasicItemSlot
        private set
    protected lateinit var extraOutputSlots: List<HTBasicItemSlot>
        private set

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        createInputSlots(builder, listener)

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        extraOutputSlots = List(getOutputSlotSize()) {
            builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTBasicItemSlot.output(listener))
        }
    }

    protected abstract fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener)

    protected abstract fun getOutputSlotSize(): Int

    protected fun addTripleOutputs(widgetHolder: HTWidgetHolder) {
        // slots
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

    //    Processing    //

    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }
    private val extraOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(extraOutputSlots) }

    final override fun createHandler(): HTRecipeHandler<*, *> = createHandler(getRecipeLookup()) {
        inputFactory = ::createInput
        canComplete = { level: ServerLevel, _, input: INPUT, recipe: RECIPE ->
            recipe.assemble(input, level.registryAccess()).let(outputHandler::canInsert)
        }
        onComplete = { level: ServerLevel, pos: BlockPos, input: INPUT, recipe: RECIPE ->
            // outputs
            recipe.assemble(input, level.registryAccess()).let(outputHandler::insert)
            recipe.assembleExtraItem(input, level).let(extraOutputHandler::insert)
            // input
            this@HTChancedBlockEntity.onComplete(level, pos, input, recipe)
        }
    }

    protected abstract fun getRecipeLookup(): HTRecipeLookup<INPUT, out RECIPE, *>

    protected abstract fun createInput(level: ServerLevel, pos: BlockPos): INPUT?

    protected abstract fun onComplete(
        level: ServerLevel,
        pos: BlockPos,
        input: INPUT,
        recipe: RECIPE,
    )
}
