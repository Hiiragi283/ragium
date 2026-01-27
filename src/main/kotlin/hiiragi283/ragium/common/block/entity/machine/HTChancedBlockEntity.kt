package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTChancedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot
    private lateinit var extraOutputSlots: List<HTBasicItemSlot>

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
        extraOutputSlots = List(getOutputSlotSize()) {
            builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTBasicItemSlot.output(listener))
        }
    }

    protected abstract fun getOutputSlotSize(): Int

    //    Processing    //

    inner class RecipeComponent<RECIPE : HTChancedRecipe>(
        finder: HTRecipeFinder<SingleRecipeInput, RECIPE>,
        private val sound: SoundEvent,
    ) : HTEnergizedRecipeComponent.Cached<SingleRecipeInput, RECIPE>(finder, this) {
        private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }
        private val extraOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(extraOutputSlots) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            outputHandler.insert(recipe.getResultItem(level.registryAccess()))
            recipe.getExtraResultItems(level).forEach(extraOutputHandler::insert)
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            inputHandler.consume(recipe.ingredient.getRequiredAmount())
        }

        override fun applyEffect() {
            playSound(sound)
        }

        // 副産物は余剰分が出ても無視される
        override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: RECIPE): Boolean =
            outputHandler.canInsert(recipe.getResultItem(level.registryAccess()))

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
            SingleRecipeInput(inputHandler.getItemStack())
    }
}
