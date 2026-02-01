package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.common.gui.HTContainerItemSlot
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
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

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    private lateinit var inputSlots: List<HTBasicItemSlot>
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlots = List(3) { HTBasicItemSlot.input(listener) }

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        inputSlots
            .mapIndexed { index: Int, slot: HTBasicItemSlot ->
                HTContainerItemSlot.create(
                    slot,
                    HTSlotHelper.getSlotPosX(index + 1),
                    HTSlotHelper.getSlotPosY(1),
                    HTContainerItemSlot.Type.INPUT,
                )
            }.map(HTItemWidget::SlotWidget)
            .forEach(widgetHolder::addWidget)

        widgetHolder += HTItemWidget.SlotWidget(
            HTContainerItemSlot.create(
                outputSlot,
                HTSlotHelper.getSlotPosX(6),
                HTSlotHelper.getSlotPosY(1),
                HTContainerItemSlot.Type.OUTPUT,
            ),
        )
    }

    //    Processing    //

    override fun createRecipeComponent(): HTEnergizedRecipeComponent.Cached<HTAlloyingRecipe> = RecipeComponent()

    inner class RecipeComponent :
        HTEnergizedRecipeComponent.Cached<HTAlloyingRecipe>(
            RagiumRecipeTypes.ALLOYING,
            this,
        ) {
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTViewRecipeInput,
            recipe: HTAlloyingRecipe,
        ) {
            outputHandler.insert(recipe.getResultItem(level.registryAccess()))
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTViewRecipeInput,
            recipe: HTAlloyingRecipe,
        ) {
            HTShapelessRecipeHelper.shapelessConsume(recipe.ingredients, inputSlots)
        }

        override fun applyEffect() {
            playSound(SoundEvents.FIRE_EXTINGUISH)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTViewRecipeInput? = HTViewRecipeInput.create {
            items += inputSlots
        }

        override fun canProgressRecipe(level: ServerLevel, input: HTViewRecipeInput, recipe: HTAlloyingRecipe): Boolean =
            outputHandler.canInsert(recipe.getResultItem(level.registryAccess()))
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.alloySmelter
}
