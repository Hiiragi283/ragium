package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTLocationToRecipe
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTLocationToFluidBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(type, pos, state) {

    fun createRecipeHandler(
        lookup: HTRecipeLookup<HTLocationToRecipe<FluidStack>>,
        sound: HTSoundInstance
    ): HTRecipeHandler = object : HTRecipeHandler() {
        private val cache: HTRecipeCacheHelper.Located<HTLocationToRecipe<FluidStack>> =
            HTRecipeCacheHelper.Located(lookup)
        private val outputTank: HTOutputSlot<FluidStack> by lazy {
            HTOutputSlotHelper.forFluid(this@HTLocationToFluidBlockEntity.outputTank)
        }

        private var outputCache: FluidStack? = null

        override fun run() {
            super.run()
            outputCache = null
        }

        override fun tick(level: ServerLevel): Boolean {
            if (!shouldCheck) return false
            // インプットに一致するレシピを探索する
            val recipe: HTLocationToRecipe<FluidStack> =
                cache.findFirstRecipe(level, blockPos, level) ?: return run {
                    shouldCheck = false
                    updateProgress(-1)
                    false
                }
            // アウトプットに完成品を搬出できるか判定する
            val output: FluidStack = recipe.apply(level, blockPos)
            outputCache = output
            if (!canProgress) {
                if (useTransaction { outputTank.take(output, it) == HTOutputSlot.TakeResult.FULL }) {
                    canProgress = true
                } else {
                    return false
                }
            }
            // レシピの最大進捗量を更新する
            /*val maxProgress: Int = progressor.getMaxProgress(recipe, level, blockPos)
            if (this.maxProgress != maxProgress) {
                updateProgress(maxProgress)
            }
            // 進捗を更新する
            if (progress < maxProgress) {
                progress += progressor.getProgress()
            }*/
            // 進捗が最大量を超えたらレシピを実行する
            if (progress >= maxProgress) {
                progress -= maxProgress
                canProgress = false
                useTransaction {
                    outputTank.take(output, it)
                    it.commit()
                }
                playSound(sound)
                outputCache = null
            }
            return true
        }
    }

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false
        )
        widgetHolder.track(outputTank)
    }
}
