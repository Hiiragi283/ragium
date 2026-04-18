package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.handler.HTRecipeHandler
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidView
import hiiragi283.core.api.storage.fluid.toStackOrEmpty
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.api.storage.item.toStackOrEmpty
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.toIor
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTProcessorBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    protected lateinit var recipeHandler: HTProgressHandler<*>
        private set
    protected lateinit var recipeComponent: HTRecipeComponent
        private set

    override fun initializeVariables() {
        super.initializeVariables()
        recipeHandler = createHandler()
        recipeComponent = HTRecipeComponent(this, recipeHandler)
    }

    protected abstract fun createHandler(): HTProgressHandler<*>

    fun addProgressBar(widgetHolder: HTWidgetHolder, x: Int = HTSlotHelper.getSlotPosX(4)) {
        widgetHolder += HTProgressWidget.createArrow(
            recipeComponent.fractionSlot,
            x,
            HTSlotHelper.getSlotPosY(1),
        )
    }

    final override fun createFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {}

    final override fun createEnergyHandler(listener: HTContentListener): HTEnergyBatteryHolder? {
        val builder: HTBasicEnergyBatteryHolder.Builder = HTBasicEnergyBatteryHolder.builder(this)
        createEnergyBattery(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {}

    final override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder, recipeHandler.createListener(listener))
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {}

    //    Ticking    //

    fun modifyTime(time: Int): Int = time // modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeHandler.tick(level, pos)

    //    Extension    //

    fun createInput(view: HTItemView): SingleRecipeInput? {
        val resource: HTItemResourceType = view.getResource() ?: return null
        return SingleRecipeInput(resource.toStack(view.getAmount()))
    }

    fun createInput(view: HTFluidView): HTSingleFluidRecipeInput? {
        val resource: HTFluidResourceType = view.getResource() ?: return null
        return HTSingleFluidRecipeInput(resource.toStack(view.getAmount()))
    }

    fun createInput(firstView: HTItemView, secondView: HTItemView): HTDoubleRecipeInput? {
        val ior: Ior<HTItemResourceType, HTItemResourceType> = (firstView.getResource() to secondView.getResource()).toIor() ?: return null
        return ior.toPair().let { (first: HTItemResourceType?, second: HTItemResourceType?) ->
            HTDoubleRecipeInput(
                first.toStackOrEmpty(firstView.getAmount()),
                second.toStackOrEmpty(secondView.getAmount()),
            )
        }
    }

    fun createInput(itemView: HTItemView, fluidView: HTFluidView): HTItemAndFluidRecipeInput? {
        val ior: Ior<HTItemResourceType, HTFluidResourceType> = (itemView.getResource() to fluidView.getResource()).toIor() ?: return null
        return ior.toPair().let { (item: HTItemResourceType?, fluid: HTFluidResourceType?) ->
            HTItemAndFluidRecipeInput(
                item.toStackOrEmpty(itemView.getAmount()),
                fluid.toStackOrEmpty(fluidView.getAmount()),
            )
        }
    }

    //    RecipeHandler    //

    abstract class RecipeHandler<INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> : HTRecipeHandler<INPUT, RECIPE>() {
        final override fun findRecipe(level: ServerLevel, pos: BlockPos): HTHandledRecipe<INPUT, RECIPE>? {
            val input: INPUT = createInput(level, pos) ?: return null
            val recipe: RECIPE = findRecipe(level, pos, input) ?: return null
            return HTHandledRecipe.create(input, recipe)
        }

        protected abstract fun createInput(level: ServerLevel, pos: BlockPos): INPUT?

        protected abstract fun findRecipe(level: ServerLevel, pos: BlockPos, input: INPUT): RECIPE?
    }

    //    Energized    //

    abstract class Energized(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntity(type, pos, state) {
        lateinit var battery: HTMachineEnergyBattery.Processor
            private set

        final override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
            battery = builder.addSlot(HTSlotInfo.INPUT, HTMachineEnergyBattery.input(listener, this))
        }

        abstract fun getConfig(): HTEnergyConfig

        fun updateAndGetProgress(time: Int): Int {
            // if (isCreative()) return 0
            battery.currentEnergyPerTick = battery.baseEnergyPerTick
            // modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it }
            return battery.currentEnergyPerTick * modifyTime(time)
        }

        //    ProgressHandler    //

        abstract inner class ProgressHandler<INPUT : RecipeInput, RECIPE : HTProcessingRecipe<INPUT>> : RecipeHandler<INPUT, RECIPE>() {
            final override fun getMaxProgress(recipe: HTHandledRecipe<INPUT, RECIPE>): Int = recipe.recipe.time.let(::updateAndGetProgress)

            final override fun getProgress(level: ServerLevel, pos: BlockPos): Int = battery.consume()
        }
    }
}
