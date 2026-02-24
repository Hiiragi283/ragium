package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.recipe.HTItemToChancedRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemToChancedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTChancedBlockEntity(type, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot

    override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    final override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder)
        // slots
        widgetHolder += HTItemSlotWidget(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )

        widgetHolder += HTItemSlotWidget(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder += HTItemSlotWidget(
            extraOutputSlots[0],
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_OUTPUT,
        )
    }

    override fun getOutputSlotSize(): Int = 1

    //    Processing    //

    inner class RecipeComponent<RECIPE : HTItemToChancedRecipe>(
        lookup: HTRecipeLookup<SingleRecipeInput, RECIPE, *>,
        private val sound: SoundEvent,
    ) : ChancedRecipeComponent<SingleRecipeInput, RECIPE>(lookup) {
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            inputHandler.consume(recipe.getRequiredAmount(input))
        }

        override fun applyEffect() {
            playSound(sound)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = createInput(inputHandler)
    }
}
