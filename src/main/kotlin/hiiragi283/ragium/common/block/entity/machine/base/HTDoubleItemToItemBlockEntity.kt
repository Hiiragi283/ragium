package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.cache.completed.HTDoubleInputCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTDoubleItemToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(type, pos, state) {
    private lateinit var leftInputSlot: HTBasicItemSlot
    private lateinit var rightInputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        leftInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                filter = { resource: HTItemResourceType -> rightInputSlot.getResource() != resource },
            ),
        )
        rightInputSlot = builder.addSlot(
            getSecondarySlotInfo(),
            HTBasicItemSlot.input(
                listener,
                filter = { resource: HTItemResourceType -> leftInputSlot.getResource() != resource },
            ),
        )

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    protected abstract fun getSecondarySlotInfo(): HTSlotInfo

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTDoubleItemToItemRecipe, HTDoubleInputCompletedRecipe.DoubleItem>() {
        private val cache: HTRecipeCaches.DoubleItem<HTDoubleItemToItemRecipe> = HTRecipeCaches.DoubleItem(getLookup())
        private val leftInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(leftInputSlot) }
        private val rightInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(rightInputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTDoubleItemToItemRecipe? = cache.findFirstRecipe(leftInputHandler.getStack(), rightInputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTDoubleItemToItemRecipe): HTDoubleInputCompletedRecipe.DoubleItem = HTDoubleInputCompletedRecipe.DoubleItem(
            recipe,
            leftInputHandler,
            rightInputHandler,
            outputHandler,
        )

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTDoubleInputCompletedRecipe.DoubleItem) {
            recipe.complete()
            playSound(SoundEvents.CRAFTER_CRAFT)
        }
    }

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl()

    protected abstract fun getLookup(): HTRecipeLookup<HTDoubleItemToItemRecipe>
}
