package hiiragi283.ragium.common.block.entity.processing

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTProcessingRecipeComponent
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTChancedBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    private val inputTank: HTBasicFluidTank = HTVariableFluidTank.input(
        getTankCapacity(RagiumFluidConfigType.FIRST_INPUT),
        canInsert = RagiumFluids.LUBRICANT::isOf,
    )

    final override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, inputTank)
    }

    @DescSynced
    @Persisted(subPersisted = true)
    private val inputSlot: HTBasicItemSlot = HTBasicItemSlot.input()

    @DescSynced
    @Persisted(subPersisted = true)
    private val outputSlots: List<HTBasicItemSlot> = List(getOutputSlotSize()) { HTBasicItemSlot.output() }

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, inputSlot)
        for (slot: HTBasicItemSlot in outputSlots) {
            builder.addSlot(HTSlotInfo.OUTPUT, slot)
        }
    }

    protected abstract fun getOutputSlotSize(): Int

    final override fun setupMainTab(root: UIElement) {
        super.setupMainTab(root)
    }

    //    Processing    //

    inner class RecipeComponent<RECIPE : HTChancedRecipe>(
        finder: HTRecipeFinder<SingleRecipeInput, RECIPE>,
        private val sound: SoundEvent,
    ) : HTProcessingRecipeComponent.Cached<SingleRecipeInput, RECIPE>(finder, this) {
        private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: RECIPE,
        ) {
            recipe.getResultItems(level).forEach(outputHandler::insert)
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

        override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: RECIPE): Boolean {
            for (stack: ItemStack in recipe.getResultItems(level.registryAccess(), 0f)) {
                if (!outputHandler.canInsert(stack)) return false
            }
            return true
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
            SingleRecipeInput(inputHandler.getItemStack())
    }
}
