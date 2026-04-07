package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.handler.assembleFluid
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.BREWERY, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity()),
        )
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // inputs
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder += HTFluidWidget
            .createTank(
                inputTank,
                HTSlotHelper.getSlotPosX(1),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.EXTRA_INPUT)
        // output
        widgetHolder += HTFluidWidget
            .createTank(
                outputTank,
                HTSlotHelper.getSlotPosX(7.5),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.OUTPUT)
    }

    //    Serialize    //

    private lateinit var cache: HTRecipeCache<HTItemAndFluidRecipeInput, HCBrewingRecipe>

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
    private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

    override fun initRecipeCache() {
        cache = HTLookupRecipeCache.forRecipe(HTVanillaRecipeTypes.BREWING)
    }

    override fun createHandler(): HTRecipeHandler<*, *> = createHandler(
        { _, _ -> createInput(itemInputHandler, fluidInputHandler) },
        cache,
        {
            canComplete = { level: ServerLevel, _, recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, HCBrewingRecipe> ->
                recipe.assembleFluid(level.registryAccess()).let(fluidOutputHandler::canInsert)
            }
            onComplete = { level: ServerLevel, _, recipe: HTHandledRecipe<HTItemAndFluidRecipeInput, HCBrewingRecipe> ->
                // output
                recipe.assembleFluid(level.registryAccess()).let(fluidOutputHandler::insert)
                // input
                val recipe: HCBrewingRecipe = recipe.recipe
                fluidInputHandler.consume(recipe.potionFrom)
                itemInputHandler.consume(recipe.ingredient)

                playSound(SoundEvents.BREWING_STAND_BREW)
            }
        },
    )

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.brewery
}
