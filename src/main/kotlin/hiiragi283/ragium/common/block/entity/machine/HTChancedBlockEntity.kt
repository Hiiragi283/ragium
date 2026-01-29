package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTChancedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    private lateinit var outputSlot: HTBasicItemSlot
    private lateinit var extraOutputSlots: List<HTBasicItemSlot>

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        createInputSlots(builder, listener)

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        extraOutputSlots = List(getOutputSlotSize()) {
            builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTBasicItemSlot.output(listener))
        }
    }

    protected abstract fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener)

    protected abstract fun getOutputSlotSize(): Int

    //    Processing    //

    abstract inner class RecipeComponent<INPUT : RecipeInput, RECIPE : HTChancedRecipe<INPUT>>(
        finder: HTRecipeFinder<INPUT, RECIPE>,
        owner: Energized,
    ) : HTEnergizedRecipeComponent.Cached<INPUT, RECIPE>(finder, owner) {
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }
        private val extraOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(extraOutputSlots) }

        final override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: INPUT,
            recipe: RECIPE,
        ) {
            outputHandler.insert(recipe.getResultItem(level.registryAccess()))
            recipe.getExtraResultItems(level).forEach(extraOutputHandler::insert)
        }

        // 副産物は余剰分が出ても無視される
        final override fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean =
            outputHandler.canInsert(recipe.getResultItem(level.registryAccess()))
    }
}
