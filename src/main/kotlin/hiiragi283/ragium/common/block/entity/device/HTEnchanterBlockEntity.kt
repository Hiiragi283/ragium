package hiiragi283.ragium.common.block.entity.device

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.support.recipe.cache.HTTripleInputRecipeCache
import hiiragi283.core.support.recipe.handler.HTFluidInputHandler
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.fluid.HTBasicFluidTank
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.cache.completed.HTEnchantingCompletedRecipe
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.fluid.HTVariableFluidTank
import hiiragi283.ragium.support.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTEnchanterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(RagiumBlockEntityTypes.ENCHANTER.get(), pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(
                listener,
                getTankCapacity(),
                filter = { it.isOf(HCFluids.EXPERIENCE.fluidTag) },
            ),
        )
    }

    private lateinit var baseSlot: HTBasicItemSlot
    private lateinit var additionSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        baseSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        additionSlot = builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(5.25), RagiumRecipeViewerTypes.ENCHANTING)
        // slots
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(0),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        widgetHolder += HTItemWidget.Container(
            baseSlot,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(baseSlot)
        widgetHolder += HTItemWidget.Container(
            additionSlot,
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.EXTRA_INPUT,
        )
        widgetHolder.track(additionSlot)

        widgetHolder += HTItemWidget.Container(
            outputSlot,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : RecipeHandler<HTEnchantingRecipe, HTEnchantingCompletedRecipe>() {
        private val cache = EnchantingCache()
        private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val baseInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(baseSlot) }
        private val additionInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(additionSlot) }

        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTEnchantingRecipe? = cache.findFirstRecipe(baseInputHandler.getStack(), additionInputHandler.getStack(), fluidInputHandler.getAmount(), level)

        override fun completeRecipe(recipe: HTEnchantingRecipe): HTEnchantingCompletedRecipe = HTEnchantingCompletedRecipe(
            recipe,
            baseInputHandler,
            additionInputHandler,
            fluidInputHandler,
            outputHandler,
        )

        override fun getMaxProgress(recipe: HTEnchantingCompletedRecipe): Int = modifyTime(200)

        override fun getProgress(level: ServerLevel, pos: BlockPos): Int = 1

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTEnchantingCompletedRecipe) {
            recipe.complete()
            playSound(SoundEvents.ENCHANTMENT_TABLE_USE)
        }
    }

    private class EnchantingCache : HTTripleInputRecipeCache<ItemStack, ItemStack, Int, HTEnchantingRecipe>(RagiumRecipeLookups.ENCHANTING) {
        override fun isEmpty(firstInput: ItemStack, secondInput: ItemStack, thirdInput: Int): Boolean = firstInput.isEmpty || secondInput.isEmpty || thirdInput <= 0
    }

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl()
}
