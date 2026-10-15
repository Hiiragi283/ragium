package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.handler.HTInputHandler
import hiiragi283.lib.recipe.handler.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlotHelper
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTSizedShapelessBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {
    fun createRecipeHandler(lookup: HTRecipeLookup<HTSizedShapelessRecipe>, sound: HTSoundInstance) =
        object : SimpleRecipeHandler<HTRecipeInput, ItemStack, HTSizedShapelessRecipe>(lookup) {
            private val inputSlots: HTInputHandler<ItemInstance> by lazy {
                HTInputHandlerHelper.forItems(this@HTSizedShapelessBlockEntity.inputSlots)
            }
            private val outputSlot: HTOutputSlot<ItemStack> by lazy {
                HTOutputSlotHelper.forItem(this@HTSizedShapelessBlockEntity.outputSlot)
            }

            override fun createProgressor(): Progressor<HTRecipeInput, HTSizedShapelessRecipe> = EnergizedProgressor()

            override fun createInput(): HTRecipeInput = HTRecipeInput(
                this@HTSizedShapelessBlockEntity.inputSlots.map(HTBasicItemSlot::getStackCopy),
                listOf(),
                customKeys
            )

            override fun canComplete(recipe: HTSizedShapelessRecipe, input: HTRecipeInput, output: ItemStack): Boolean =
                useTransaction { transaction: Transaction ->
                    when {
                        recipe.getMatchingStack(input).any { inputSlots.use(it, transaction).failed } -> false
                        else -> outputSlot.take(output, transaction).fullOrNoneTaken
                    }
                }

            override fun onComplete(recipe: HTSizedShapelessRecipe, input: HTRecipeInput, output: ItemStack) {
                useTransaction { transaction: Transaction ->
                    recipe.getMatchingStack(input).forEach { inputSlots.use(it, transaction) }
                    outputSlot.take(output, transaction)
                    transaction.commit()
                }
                playSound(sound)
            }
        }

    protected lateinit var inputSlots: List<HTBasicItemSlot>
        private set
    protected lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlots = List(getInputSize()) { builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener)) }
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    protected abstract fun getInputSize(): Int
}
