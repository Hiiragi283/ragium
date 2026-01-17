package hiiragi283.ragium.common.block.entity.processing

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.recipe.input.HTListItemRecipeInput
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.component.HTProcessingRecipeComponent
import hiiragi283.ragium.common.gui.RagiumModularUIHelper
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    private val topInputSlot: HTBasicItemSlot = HTBasicItemSlot.input(filter = { resource: HTItemResourceType ->
        resource != leftInputSlot.getResource() && resource != rightInputSlot.getResource()
    })

    @DescSynced
    @Persisted(subPersisted = true)
    private val leftInputSlot: HTBasicItemSlot = HTBasicItemSlot.input(filter = { resource: HTItemResourceType ->
        resource != rightInputSlot.getResource() && resource != topInputSlot.getResource()
    })

    @DescSynced
    @Persisted(subPersisted = true)
    private val rightInputSlot: HTBasicItemSlot = HTBasicItemSlot.input(filter = { resource: HTItemResourceType ->
        resource != topInputSlot.getResource() && resource != leftInputSlot.getResource()
    })

    @DescSynced
    @Persisted(subPersisted = true)
    private val outputSlot: HTBasicItemSlot = HTBasicItemSlot.output()

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {
        builder.addSlot(HTSlotInfo.INPUT, topInputSlot)
        builder.addSlot(HTSlotInfo.EXTRA_INPUT, leftInputSlot)
        builder.addSlot(HTSlotInfo.EXTRA_INPUT, rightInputSlot)

        builder.addSlot(HTSlotInfo.OUTPUT, outputSlot)
    }

    override fun setupMainTab(root: UIElement) {
        RagiumModularUIHelper.alloySmelter(
            root,
            HTItemSlotElement(topInputSlot),
            HTItemSlotElement(leftInputSlot),
            HTItemSlotElement(rightInputSlot),
            HTItemSlotElement(outputSlot),
        )
        super.setupMainTab(root)
    }

    //    Processing    //

    override fun createRecipeComponent(): HTProcessingRecipeComponent.Cached<HTListItemRecipeInput, HTAlloyingRecipe> =
        object : HTProcessingRecipeComponent.Cached<HTListItemRecipeInput, HTAlloyingRecipe>(
            RagiumRecipeTypes.ALLOYING,
            this,
        ) {
            private val inputHandlers: List<HTSlotInputHandler<HTItemResourceType>> =
                listOf(topInputSlot, leftInputSlot, rightInputSlot).map(::HTSlotInputHandler)
            private val outputHandler: HTItemOutputHandler = HTItemOutputHandler.single(outputSlot)

            override fun insertOutput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTListItemRecipeInput,
                recipe: HTAlloyingRecipe,
            ) {
                outputHandler.insert(recipe.getResultItem(level.registryAccess()))
            }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: HTListItemRecipeInput,
                recipe: HTAlloyingRecipe,
            ) {
                inputHandlers[0].consume(recipe.firstIngredient)
                inputHandlers[1].consume(recipe.secondIngredient)
                inputHandlers[2].consume(recipe.thirdIngredient)
            }

            override fun applyEffect() {
                playSound(SoundEvents.FIRE_EXTINGUISH)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTListItemRecipeInput =
                HTListItemRecipeInput(inputHandlers.map { it.getItemStack() })

            override fun canProgressRecipe(level: ServerLevel, input: HTListItemRecipeInput, recipe: HTAlloyingRecipe): Boolean =
                outputHandler.canInsert(recipe.getResultItem(level.registryAccess()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.alloySmelter
}
