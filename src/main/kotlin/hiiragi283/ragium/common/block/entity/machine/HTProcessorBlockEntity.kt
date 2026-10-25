package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.handler.HTDoubleRecipeHandler
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.HTSingleRecipeHandler
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.energy.HTBasicEnergyHandler
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.holder.HTEnergyHandlerHolder
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.item.HTItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.entity.HTBaseMachineBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyHandlerHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTProcessorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTBaseMachineBlockEntity(type, pos, state) {
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
        widgetHolder += HTProgressWidget(recipeHandler::progression, x, HTSlotHelper.getSlotPosY(1))
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

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeHandler.tick(level)

    //    Energized    //

    abstract class Energized(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(type, pos, state) {
        protected lateinit var handler: HTBasicEnergyHandler
            private set

        override fun createEnergyHandler(listener: Runnable): HTEnergyHandlerHolder? {
            val builder: HTBasicEnergyHandlerHolder.Builder = HTBasicEnergyHandlerHolder.builder(this)
            handler = builder.addHandler(
                HTSlotInfo.INPUT,
                HTBasicEnergyHandler.input(
                    HTMachineUpgradeHelper.modifyCapacity(components(), getConfig().getCapacity()),
                    listener,
                ),
            )
            return builder.build()
        }

        protected abstract fun getConfig(): HTEnergyConfig

        fun addEnergySlot(widgetHolder: HTWidgetHolder, x: Int, y: Int) {
            widgetHolder += HTEnergySlotWidget(handler, x, y)
            widgetHolder.track(handler)
        }

        //    EnergizedProgressor    //

        protected abstract inner class EnergyProgressor : HTRecipeHandler.Progressor {
            protected val baseEnergyPerTick: Int = getConfig().getUsage()
            protected val currentEnergyPerTick: Int
                get() = HTMachineUpgradeHelper.modifySpeed(components(), this.baseEnergyPerTick)

            final override fun getProgress(): Int = useTransaction { transaction: Transaction ->
                val extracted: Int = handler.extract(currentEnergyPerTick, transaction, HTTransferAccess.INTERNAL)
                if (extracted > 0) {
                    transaction.commit()
                }
                extracted
            }
        }

        protected inner class SingleProgressor<INPUT : Any, RECIPE : HTProgressRecipe.Single<*, INPUT>> :
            EnergyProgressor(),
            HTSingleRecipeHandler.Progressor<INPUT, RECIPE> {
            override fun getMaxProgress(recipe: RECIPE, input: INPUT): Int {
                val requiredEnergy: Int = recipe.getProgressData(input).getTotalEnergy(this.baseEnergyPerTick)
                return HTMachineUpgradeHelper.modifyEnergy(components(), requiredEnergy)
            }
        }

        protected inner class DoubleProgressor<
            INPUT_A : Any,
            INPUT_B : Any,
            RECIPE : HTProgressRecipe.Double<*, INPUT_A, INPUT_B>,
            > :
            EnergyProgressor(),
            HTDoubleRecipeHandler.Progressor<INPUT_A, INPUT_B, RECIPE> {
            override fun getMaxProgress(recipe: RECIPE, first: INPUT_A, second: INPUT_B): Int {
                val requiredEnergy: Int = recipe.getProgressData(first, second).getTotalEnergy(this.baseEnergyPerTick)
                return HTMachineUpgradeHelper.modifyEnergy(components(), requiredEnergy)
            }
        }
    }
}
