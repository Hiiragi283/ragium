package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.block.entity.HTSoundPlayerBlockEntity
import hiiragi283.core.api.recipe.HTFluidRecipe
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.common.recipe.handler.HTFluidInputHandler
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemOrFluidBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)),
        )
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    Processing    //

    protected inner class RecipeComponent<RECIPE>(
        lookup: HTRecipeLookup<HTItemAndFluidRecipeInput, RECIPE>,
        private val itemIngredient: (RECIPE) -> HTItemIngredient?,
        private val fluidIngredient: (RECIPE) -> HTFluidIngredient?,
        private val user: HTSoundPlayerBlockEntity.User,
    ) : HTEnergizedRecipeComponent.Cached<HTItemAndFluidRecipeInput, RECIPE>(
            lookup,
            this,
        ) where RECIPE : HTProcessingRecipe<HTItemAndFluidRecipeInput>, RECIPE : HTFluidRecipe {
        private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

        private val fluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }
        private val itemOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

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
            fluidInputHandler.consume(fluidIngredient(recipe))
            itemInputHandler.consume(itemIngredient(recipe))
        }

        override fun applyEffect() {
            user.playSound(this@HTItemOrFluidBlockEntity)
        }

        final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemAndFluidRecipeInput? =
            createInput(itemInputHandler, fluidInputHandler)

        final override fun canProgressRecipe(level: ServerLevel, input: HTItemAndFluidRecipeInput, recipe: RECIPE): Boolean {
            val access: RegistryAccess = level.registryAccess()
            val bool1: Boolean = itemOutputHandler.canInsert(recipe.getResultItem(access))
            val bool2: Boolean = fluidOutputHandler.canInsert(recipe.getResultFluid(access))
            return bool1 && bool2
        }
    }
}
