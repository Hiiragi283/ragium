package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank =
            builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)))
    }

    private lateinit var inputSlots: List<HTBasicItemSlot>
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlots = List(3) { HTBasicItemSlot.input(listener) }

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        widgetHolder += HTProgressWidget.createArrow(
            recipeComponent.fractionSlot,
            HTSlotHelper.getSlotPosX(5),
            HTSlotHelper.getSlotPosY(1),
        )
        // slots
        widgetHolder += HTFluidWidget
            .createSlot(
                inputTank,
                HTSlotHelper.getSlotPosX(2.5),
                HTSlotHelper.getSlotPosY(2),
            ).setBackground(HTBackgroundType.EXTRA_INPUT)

        inputSlots
            .mapIndexed { index: Int, slot: HTBasicItemSlot ->
                HTItemSlotWidget(
                    slot,
                    HTSlotHelper.getSlotPosX(index + 1.5),
                    HTSlotHelper.getSlotPosY(0),
                    HTBackgroundType.INPUT,
                )
            }.forEach(widgetHolder::addWidget)

        widgetHolder += HTItemSlotWidget(
            outputSlot,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Processing    //

    override fun createRecipeComponent(): HTEnergizedRecipeComponent.Cached<HTAlloyingRecipe> = RecipeComponent()

    inner class RecipeComponent :
        HTEnergizedRecipeComponent.Cached<HTAlloyingRecipe>(
            RagiumRecipeTypes.ALLOYING,
            this,
        ) {
        private val inputHandler: HTSlotInputHandler<HTFluidResourceType> by lazy { HTSlotInputHandler(inputTank) }
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
