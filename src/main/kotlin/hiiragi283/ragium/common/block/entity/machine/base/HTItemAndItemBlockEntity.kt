package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemAndItemBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    protected lateinit var inputSlot: HTBasicItemSlot
        private set
    protected lateinit var catalystSlot: HTBasicItemSlot
        private set
    protected lateinit var outputSlot: HTBasicItemSlot
        private set

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        catalystSlot = builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemSlotWidget(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTItemSlotWidget(
            catalystSlot,
            HTSlotHelper.getSlotPosX(4.25),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.EXTRA_INPUT,
        )

        widgetHolder += HTItemSlotWidget(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Processing    //

    protected inner class RecipeComponent<RECIPE : HTItemAndItemRecipe>(
        lookup: HTRecipeLookup<HTDoubleRecipeInput, RECIPE, *>,
        private val user: HTSoundPlayerBlockEntity.User,
    ) : HTEnergizedRecipeComponent.Cached<HTDoubleRecipeInput, RECIPE>(lookup, this) {
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val catalystHandler: HTItemInputHandler by lazy { HTItemInputHandler(catalystSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTDoubleRecipeInput,
            recipe: RECIPE,
        ) {
            recipe.assemble(input, level.registryAccess()).let(outputHandler::insert)
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTDoubleRecipeInput,
            recipe: RECIPE,
        ) {
            val (inputCount: Int, catalystCount: Int) = recipe.getRequiredAmount(input)
            inputHandler.consume(inputCount)
            catalystHandler.consume(catalystCount)
        }

        override fun applyEffect() {
            user.playSound(this@HTItemAndItemBlockEntity)
        }

        override fun canProgressRecipe(level: ServerLevel, input: HTDoubleRecipeInput, recipe: RECIPE): Boolean =
            recipe.assemble(input, level.registryAccess()).let(outputHandler::canInsert)

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTDoubleRecipeInput? = createInput(inputHandler, catalystHandler)
    }
}
