package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTChancedBlockEntity(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    private lateinit var inputSlots: List<HTBasicItemSlot>

    override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlots = List(3) { HTBasicItemSlot.input(listener) }
    }

    override fun getOutputSlotSize(): Int = 1

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4.5))
        // slots
        inputSlots
            .mapIndexed { index: Int, slot: HTBasicItemSlot ->
                HTItemSlotWidget(
                    slot,
                    HTSlotHelper.getSlotPosX(index + 1),
                    HTSlotHelper.getSlotPosY(0.5),
                    HTBackgroundType.INPUT,
                )
            }.forEach(widgetHolder::addWidget)

        widgetHolder += HTItemSlotWidget(
            outputSlot,
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.OUTPUT,
        )

        widgetHolder += HTItemSlotWidget(
            extraOutputSlots[0],
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_OUTPUT,
        )
    }

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent()

    private inner class RecipeComponent :
        ChancedRecipeComponent<HTShapelessRecipeInput, HTAlloyingRecipe>(RagiumRecipeTypes.ALLOYING, this) {
        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTShapelessRecipeInput,
            recipe: HTAlloyingRecipe,
        ) {
            HTShapelessRecipeHelper.shapelessConsume(recipe.ingredients, inputSlots)
        }

        override fun applyEffect() {
            playSound(SoundEvents.FIRE_EXTINGUISH)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTShapelessRecipeInput? {
            val map: Map<HTItemResourceType, Int> = HTShapelessRecipeHelper.createMap(inputSlots)
            if (map.isEmpty()) return null
            return HTShapelessRecipeInput(map)
        }
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.alloySmelter
}
