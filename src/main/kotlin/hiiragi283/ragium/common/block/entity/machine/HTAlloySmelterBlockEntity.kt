package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.resource.isOf
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    private val inputItems: Array<ItemStack> = Array(3) { ItemStack.EMPTY }
    private lateinit var inputSlots: List<HTBasicItemSlot>
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlots = inputItems.indices
            .map { index: Int ->
                val info: HTSlotInfo = when (index) {
                    0 -> HTSlotInfo.INPUT
                    else -> HTSlotInfo.EXTRA_INPUT
                }

                builder.addSlot(info, ArraySlot(index, listener))
            }

        builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    private inner class ArraySlot(private val index: Int, listener: HTContentListener?) :
        HTBasicItemSlot(
            HTConst.ABSOLUTE_MAX_STACK_SIZE,
            HTStoragePredicates.notExternal(),
            { resource: HTItemResourceType, _ -> inputItems.none(resource::isOf) },
            HTStoragePredicates.alwaysTrue(),
            listener,
        ) {
        override fun setStackInternal(stack: ItemStack) {
            inputItems[index] = stack
        }

        override fun setAmount(amount: Int) {
            inputItems[index].count = amount
        }

        override fun getResource(): HTItemResourceType? = inputItems[index].toResource()

        override fun getAmount(): Int = inputItems[index].count
    }

    //    Processing    //

    override fun createRecipeComponent(): HTEnergizedRecipeComponent.Cached<HTAlloyingRecipe> = RecipeComponent()

    inner class RecipeComponent :
        HTEnergizedRecipeComponent.Cached<HTAlloyingRecipe>(
            RagiumRecipeTypes.ALLOYING,
            this,
        ) {
        private val inputHandlers: List<HTSlotInputHandler<HTItemResourceType>> = inputSlots.map(::HTSlotInputHandler)
        private val outputHandler: HTItemOutputHandler = HTItemOutputHandler.single(outputSlot)

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTViewRecipeInput,
            recipe: HTAlloyingRecipe,
        ) {
            outputHandler.insert(recipe.getResultItem(level.registryAccess()))
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTViewRecipeInput,
            recipe: HTAlloyingRecipe,
        ) {
            inputHandlers[0].consume(recipe.firstIngredient)
            inputHandlers[1].consume(recipe.secondIngredient)
            inputHandlers[2].consume(recipe.thirdIngredient)
        }

        override fun applyEffect() {
            playSound(SoundEvents.FIRE_EXTINGUISH)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTViewRecipeInput? = HTViewRecipeInput.create {
            items +=
                inputHandlers
        }

        override fun canProgressRecipe(level: ServerLevel, input: HTViewRecipeInput, recipe: HTAlloyingRecipe): Boolean =
            outputHandler.canInsert(recipe.getResultItem(level.registryAccess()))
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.alloySmelter
}
