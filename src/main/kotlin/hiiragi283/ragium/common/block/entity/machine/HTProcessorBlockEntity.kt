package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.transfer.energy.HTBasicEnergyHandler
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.config.HTEnergyConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBaseMachineBlockEntity(type, pos, state) {
    protected val recipeHandler: HTRecipeHandler by lazy(::createRecipeHandler)

    protected abstract fun createRecipeHandler(): HTRecipeHandler

    final override fun createFluidHandler(listener: Runnable): HTResourceSlotHolder<HTFluidTank>? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder) {
            recipeHandler.run()
            listener.run()
        }
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {}

    final override fun createItemHandler(listener: Runnable): HTResourceSlotHolder<HTItemSlot>? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder) {
            recipeHandler.run()
            listener.run()
        }
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {}

    fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(3.75)) {
        widgetHolder += HTProgressWidget.createArrow(recipeHandler::progression, x, HTSlotHelper.getSlotPosY(1))
    }

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        recipeHandler.serialize(output)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        recipeHandler.deserialize(input)
    }

    override fun writeReducedUpdateTag(output: ValueOutput) {
        super.writeReducedUpdateTag(output)
        recipeHandler.serialize(output)
    }

    override fun readUpdateTag(input: ValueInput) {
        super.readUpdateTag(input)
        recipeHandler.deserialize(input)
    }

    //    Ticking    //

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean =
        recipeHandler.tick(level)

    //    Energized    //

    abstract class Energized(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntity(type, pos, state) {
        lateinit var handler: HTBasicEnergyHandler
            private set

        override fun initializeVariables(listener: Runnable) {
            super.initializeVariables(listener)
            handler = HTBasicEnergyHandler.input(
                HTMachineUpgradeHelper.modifyCapacity(components(), getConfig().getCapacity()),
                listener
            )
        }

        abstract fun getConfig(): HTEnergyConfig

        fun addEnergySlot(widgetHolder: HTWidgetHolder, x: Int, y: Int) {
            widgetHolder += HTEnergySlotWidget(handler, x, y)
            widgetHolder.track(handler)
        }

        override fun writeValue(output: ValueOutput) {
            super.writeValue(output)
            output.putChild(HTConstants.ENERGY, handler)
        }

        override fun readValue(input: ValueInput) {
            super.readValue(input)
            input.readChild(HTConstants.ENERGY, handler)
        }

        //    EnergizedProgressor    //

        /*protected inner class EnergizedProgressor<INPUT : RecipeInput, RECIPE : HTProgressRecipe<INPUT>> :
            HTRecipeHandler.Progressor<INPUT, RECIPE> {
            private val baseEnergyPerTick: Int = getConfig().getUsage()
            private val currentEnergyPerTick: Int
                get() = HTMachineUpgradeHelper.modifySpeed(components(), this.baseEnergyPerTick)

            override fun getMaxProgress(recipe: RECIPE, input: INPUT): Int {
                val requiredEnergy: Int = recipe.getProgressData(input).getTotalEnergy(this.baseEnergyPerTick)
                return HTMachineUpgradeHelper.modifyEnergy(components(), requiredEnergy)
            }

            override fun getProgress(): Int = useTransaction { transaction: Transaction ->
                val extracted: Int = handler.extract(currentEnergyPerTick, transaction, HTTransferAccess.INTERNAL)
                if (extracted > 0) {
                    transaction.commit()
                }
                extracted
            }
        }*/
    }
}
