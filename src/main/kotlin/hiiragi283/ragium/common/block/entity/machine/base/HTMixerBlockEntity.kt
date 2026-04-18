package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

abstract class HTMixerBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    //    Serialize    //

    private val cache: HTLookupRecipeCache<HTMixingRecipeInput, HTMixingRecipe> =
        HTLookupRecipeCache.forRecipe(RagiumRecipeLookups.MIXING)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTMixingRecipeInput, HTMixingRecipe>() {
        private val itemOutputHandler: HTItemOutputHandler by lazy { createItemOutputs() }
        private val fluidOutputHandler: HTFluidOutputHandler by lazy { createFluidOutputs() }

        override fun createInput(level: ServerLevel, pos: BlockPos): HTMixingRecipeInput? =
            createInput().takeUnless(HTMixingRecipeInput::isEmpty)

        override fun findRecipe(level: ServerLevel, pos: BlockPos, input: HTMixingRecipeInput): HTMixingRecipe? =
            cache.getFirstRecipe(input, level)

        override fun canComplete(
            level: ServerLevel,
            pos: BlockPos,
            recipe: HTHandledRecipe<HTMixingRecipeInput, HTMixingRecipe>,
        ): Boolean {
            val access: RegistryAccess = level.registryAccess()
            val bool1: Boolean = recipe.map(access, HTMixingRecipe::assembleItems).all(itemOutputHandler::canInsert)
            val bool2: Boolean = recipe.map(access, HTMixingRecipe::assembleFluids).all(fluidOutputHandler::canInsert)
            return bool1 && bool2
        }

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<HTMixingRecipeInput, HTMixingRecipe>) {
            val access: RegistryAccess = level.registryAccess()
            // outputs
            recipe.map(access, HTMixingRecipe::assembleItems).forEach(itemOutputHandler::insert)
            recipe.map(access, HTMixingRecipe::assembleFluids).forEach(fluidOutputHandler::insert)
            // inputs
            recipe.map(HTMixingRecipe::getRequiredAmounts).let(::consumeInputs)
            // sound
            playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE)
        }
    }

    final override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    protected abstract fun createItemOutputs(): HTItemOutputHandler

    protected abstract fun createFluidOutputs(): HTFluidOutputHandler

    protected abstract fun createInput(): HTMixingRecipeInput

    protected abstract fun consumeInputs(amounts: HTMixingRecipe.RequiredAmounts)

    final override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.mixer
}
