package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleCatalystBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected lateinit var catalystSlot: HTBasicItemSlot
        private set
    protected lateinit var outputSlots: List<HTBasicItemSlot>
        private set

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        catalystSlot = builder.addSlot(
            HTSlotInfo.NONE,
            HTBasicItemSlot.create(
                listener,
                limit = 1,
                canExtract = HTStoragePredicates.manualOnly(),
                canInsert = HTStoragePredicates.manualOnly(),
            ),
        )

        outputSlots = List(4) { builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener)) }
    }

    final override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder)
        // slots
        setupInput(widgetHolder)

        widgetHolder += HTItemSlotWidget(
            catalystSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE,
        )

        outputSlots
            .mapIndexed { index: Int, slot: HTBasicItemSlot ->
                HTItemSlotWidget(
                    slot,
                    HTSlotHelper.getSlotPosX(6 + index % 2),
                    HTSlotHelper.getSlotPosY(0.5 + index / 2),
                    HTBackgroundType.OUTPUT,
                )
            }.forEach(widgetHolder::plusAssign)
    }

    protected abstract fun setupInput(widgetHolder: HTWidgetHolder)

    //    Processing    //

    abstract inner class SingleRecipeComponent<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>>(
        finder: HTRecipeFinder<INPUT, RECIPE>,
    ) : HTEnergizedRecipeComponent.Cached<INPUT, RECIPE>(finder, this) {
        protected val catalystHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(catalystSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }

        final override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: INPUT,
            recipe: RECIPE,
        ) {
            outputHandler.insert(recipe.assemble(input, level.registryAccess()))
        }

        final override fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean =
            outputHandler.canInsert(recipe.assemble(input, level.registryAccess()))
    }
}
