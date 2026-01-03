package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTProcessingRecipeComponent
import hiiragi283.ragium.common.recipe.HTComplexRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.state.BlockState

abstract class HTComplexBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    lateinit var inputTank: HTBasicFluidTank
        private set
    lateinit var outputTank: HTBasicFluidTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank =
            builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)))
        outputTank =
            builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)))
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    private val itemInputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
    private val fluidInputHandler: HTSlotInputHandler<HTFluidResourceType> by lazy { HTSlotInputHandler(inputTank) }

    private val itemOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }
    private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

    //    Processing    //

    inner class RecipeComponent<RECIPE : HTComplexRecipe>(
        finder: HTRecipeFinder<HTItemAndFluidRecipeInput, RECIPE>,
        private val sound: SoundEvent,
    ) : HTProcessingRecipeComponent.Cached<HTItemAndFluidRecipeInput, RECIPE>(finder, this) {
        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTItemAndFluidRecipeInput,
            recipe: RECIPE,
        ) {
            val access: RegistryAccess = level.registryAccess()
            itemOutputHandler.insert(recipe.getResultItem(access))
            fluidOutputHandler.insert(recipe.getResultFluid(access))
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTItemAndFluidRecipeInput,
            recipe: RECIPE,
        ) {
            itemInputHandler.consume(recipe.getItemIngredient())
            fluidInputHandler.consume(recipe.getFluidIngredient())
        }

        override fun applyEffect() {
            playSound(sound)
        }

        override fun canProgressRecipe(level: ServerLevel, input: HTItemAndFluidRecipeInput, recipe: RECIPE): Boolean {
            val access: RegistryAccess = level.registryAccess()
            val bool1: Boolean = itemOutputHandler.canInsert(recipe.getResultItem(access))
            val bool2: Boolean = fluidOutputHandler.canInsert(recipe.getResultFluid(access))
            return bool1 && bool2
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput =
            HTItemAndFluidRecipeInput(itemInputHandler.getItemStack(), fluidInputHandler.getFluidStack())
    }
}
