package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.input.HTSingleCatalystRecipeInput
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTFormingPressBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleCatalystBlockEntity(RagiumBlockEntityTypes.FORMING_PRESS, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        super.createItemSlots(builder, listener)
    }

    override fun setupInput(widgetHolder: HTWidgetHolder) {
        widgetHolder += HTItemSlotWidget(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
    }

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent()

    private inner class RecipeComponent :
        SingleRecipeComponent<HTItemResourceType, HTItemIngredient, HTPressingRecipe>(RagiumRecipeTypes.PRESSING) {
        override fun createInputHandler(): HTSlotInputHandler<HTItemResourceType> = HTSlotInputHandler(inputSlot)

        override fun applyEffect() {
            playSound(SoundEvents.ANVIL_LAND)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTSingleCatalystRecipeInput? =
            HTSingleCatalystRecipeInput(inputHandler.getItemStack(), catalystHandler.getItemStack())
                .takeUnless(HTSingleCatalystRecipeInput::isEmpty)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.formingPress
}
