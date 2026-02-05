package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTWashingRecipe
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

class HTWasherBlockEntity(pos: BlockPos, state: BlockState) : HTChancedBlockEntity(RagiumBlockEntityTypes.WASHER, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    override fun getOutputSlotSize(): Int = 3

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent()

    private inner class RecipeComponent :
        ChancedRecipeComponent<HTItemAndFluidRecipeInput, HTWashingRecipe>(
            RagiumRecipeTypes.WASHING,
            this,
        ) {
        private val itemInputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
        private val fluidInputHandler: HTSlotInputHandler<HTFluidResourceType> by lazy { HTSlotInputHandler(inputTank) }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTItemAndFluidRecipeInput,
            recipe: HTWashingRecipe,
        ) {
            itemInputHandler.consume(recipe.itemIngredient)
            fluidInputHandler.consume(recipe.fluidIngredient)
        }

        override fun applyEffect() {
            playSound(SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput? =
            createInput(itemInputHandler, fluidInputHandler)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.washer
}
