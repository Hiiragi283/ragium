package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTMultiOutputBlockEntity<INPUT : RecipeInput, RECIPE : HTMultiOutputRecipe<INPUT>>(
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity.Energized(type, pos, state) {
    protected lateinit var outputSlots: List<HTBasicItemSlot>
        private set

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        createInputSlots(builder, listener)

        outputSlots = List(getOutputSlotSize()) { builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTBasicItemSlot.output(listener)) }
    }

    protected abstract fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener)

    protected abstract fun getOutputSlotSize(): Int

    //    Serialize    //

    private val cache: HTRecipeCache<INPUT, out RECIPE> = HTLookupRecipeCache.forRecipe(getLookup())

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        cache.serialize(output)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        cache.deserialize(input)
    }

    //    Processing    //

    protected abstract inner class MultiOutputProgressHandler : ProgressHandler<INPUT, RECIPE>() {
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }

        final override fun findRecipe(level: ServerLevel, pos: BlockPos, input: INPUT): RECIPE? = cache.getFirstRecipe(input, level)

        final override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<INPUT, RECIPE>): Boolean = recipe
            .map(true, HTMultiOutputRecipe<INPUT>::assembleItems)
            .all(outputHandler::canInsert)

        final override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<INPUT, RECIPE>) {
            // outputs
            recipe
                .map(false, HTMultiOutputRecipe<INPUT>::assembleItems)
                .forEach(outputHandler::insert)
            // input
            completeInput(level, pos, recipe)
        }

        protected abstract fun completeInput(level: ServerLevel, pos: BlockPos, recipe: HTHandledRecipe<INPUT, RECIPE>)
    }

    protected abstract fun getLookup(): HTRecipeLookup<INPUT, out RECIPE>
}
