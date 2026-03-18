package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.input.HTShapelessRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
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
        inputSlots = List(3) { builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener)) }

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4.5))
        // slots
        inputSlots
            .mapIndexed { index: Int, slot: HTBasicItemSlot ->
                HTItemSlotWidget.container(
                    slot,
                    HTSlotHelper.getSlotPosX(index + 1),
                    HTSlotHelper.getSlotPosY(0.5),
                    HTBackgroundType.INPUT,
                )
            }.forEach(widgetHolder::addWidget)

        widgetHolder += HTItemSlotWidget.container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6.5),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Processing    //

    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

    override fun createHandler(): HTRecipeHandler<*, *> = createHandler(
        RagiumRecipeTypes.ALLOYING,
        { _, _ ->
            val map: Map<HTItemResourceType, Int> = HTShapelessRecipeHelper.createMap(inputSlots)
            if (map.isEmpty()) null else HTShapelessRecipeInput(map)
        },
        {
            canComplete = { level: ServerLevel, _, recipe: HTHandledRecipe<HTShapelessRecipeInput, HTAlloyingRecipe> ->
                recipe.assemble(level.registryAccess()).let(outputHandler::canInsert)
            }
            onComplete = { level: ServerLevel, _, recipe: HTHandledRecipe<HTShapelessRecipeInput, HTAlloyingRecipe> ->
                // output
                recipe.assemble(level.registryAccess()).let(outputHandler::insert)
                // input
                val recipe: HTAlloyingRecipe = recipe.recipe
                HTShapelessRecipeHelper.shapelessConsume(recipe.ingredients, inputSlots)

                playSound(SoundEvents.FIRE_EXTINGUISH)
            }
        },
    )

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.alloySmelter
}
