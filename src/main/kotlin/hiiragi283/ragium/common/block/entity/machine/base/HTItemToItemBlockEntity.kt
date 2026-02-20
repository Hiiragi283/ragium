package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemToItemBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
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
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(3.5))
        // slots
        widgetHolder += HTItemSlotWidget(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )

        widgetHolder += HTItemSlotWidget(
            outputSlot,
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Processing    //

    protected inner class RecipeComponent<RECIPE : HTSingleProcessingRecipe.ItemToItem>(
        lookup: HTRecipeLookup<SingleRecipeInput, RECIPE>,
        private val user: HTSoundPlayerBlockEntity.User,
    ) : HTEnergizedRecipeComponent.ProcessingCached<SingleRecipeInput, RECIPE>(lookup, this) {
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            outputHandler.insert(recipe.assemble(input, level.registryAccess()))
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            inputHandler.consume(recipe.ingredient)
        }

        override fun applyEffect() {
            user.playSound(this@HTItemToItemBlockEntity)
        }

        override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: RECIPE): Boolean =
            outputHandler.canInsert(recipe.assemble(input, level.registryAccess()))

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = createInput(inputHandler)
    }
}
