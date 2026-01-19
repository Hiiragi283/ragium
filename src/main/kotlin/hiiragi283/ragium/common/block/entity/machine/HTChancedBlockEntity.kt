package hiiragi283.ragium.common.block.entity.machine

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
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
    @DescSynced
    @Persisted(subPersisted = true)
    private val inputSlot: HTBasicItemSlot = HTBasicItemSlot.input()

    @DescSynced
    @Persisted(subPersisted = true)
    private val outputSlot: HTBasicItemSlot = HTBasicItemSlot.output()

    @DescSynced
    @Persisted(subPersisted = true)
    private val extraOutputSlots: List<HTBasicItemSlot> = List(2) { HTBasicItemSlot.output() }

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, inputSlot)

        builder.addSlot(HTSlotInfo.OUTPUT, outputSlot)
        for (slot: HTBasicItemSlot in extraOutputSlots) {
            builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, slot)
        }
    }

    final override fun setupMainTab(root: UIElement) {
        RagiumModularUIHelper.chanced(
            root,
            HTItemSlotElement(inputSlot),
            HTItemSlotElement(outputSlot),
            extraOutputSlots.map(::HTItemSlotElement),
        )
        super.setupMainTab(root)
    }

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
