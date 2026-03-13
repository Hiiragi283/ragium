package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.common.recipe.handler.HTFluidInputHandler
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
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

class HTFreezerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.FREEZER, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    Processing    //

    private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
    private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

    private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

    override fun createHandler(): HTRecipeHandler<*, *> = createHandler(RagiumRecipeTypes.FREEZING) {
        inputFactory = { _, _ -> createInput(itemInputHandler, fluidInputHandler) }
        canComplete = { level: ServerLevel, _, input: HTItemAndFluidRecipeInput, recipe: HTFreezingRecipe ->
            recipe.assemble(input, level.registryAccess()).let(outputHandler::canInsert)
        }
        onComplete = { level, _, input, recipe ->
            // output
            recipe.assemble(input, level.registryAccess()).let(outputHandler::insert)
            // input
            fluidInputHandler.consume(recipe.fluidIngredient)
            itemInputHandler.consume(recipe.itemIngredient)

            playSound(SoundEvents.GLASS_HIT)
        }
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.freezer
}
