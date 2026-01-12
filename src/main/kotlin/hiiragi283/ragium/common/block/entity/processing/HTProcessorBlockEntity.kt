package hiiragi283.ragium.common.block.entity.processing

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.math.div
import hiiragi283.core.api.math.times
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeKeys
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.storge.energy.HTMachineEnergyBattery
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

abstract class HTProcessorBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    @DescSynced
    @Persisted(subPersisted = true)
    protected val recipeComponent: HTRecipeComponent<*, *> = createRecipeComponent()

    protected abstract fun createRecipeComponent(): HTRecipeComponent<*, *>

    fun getProgress(): Fraction = recipeComponent.getProgress(isActive())

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = recipeComponent.tick(level, pos)

    //    UI    //

    override fun setupElements(root: UIElement) {
        root.addChild(
            ProgressBar()
                .label { label: Label ->
                    label.bindDataSource(
                        SupplierDataSource.of {
                            HTCommonTranslation.PROGRESS.translate((getProgress() * 100).toInt())
                        },
                    )
                }.bind(DataBindingBuilder.floatValS2C(::getProgress.andThen(Fraction::toFloat)).build()),
        )
    }

    //    Energized    //

    abstract class Energized(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTProcessorBlockEntity(type, pos, state) {
        @DescSynced
        @Persisted(subPersisted = true)
        val battery: HTMachineEnergyBattery.Processor = HTMachineEnergyBattery.input(this)

        final override fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder) {
            builder.addSlot(HTSlotInfo.INPUT, battery)
        }

        fun updateAndGetProgress(time: Int): Int {
            if (isCreative()) return 0
            battery.currentEnergyPerTick = modifyValue(HTUpgradeKeys.ENERGY_EFFICIENCY) { battery.baseEnergyPerTick / it }
            val time: Int = modifyValue(HTUpgradeKeys.SPEED) { time / (it * getBaseMultiplier()) }
            return battery.currentEnergyPerTick * time
        }

        //    UI    //

        override fun setupElements(root: UIElement) {
            super.setupElements(root)
            root.addChild(
                ProgressBar()
                    .label { label: Label ->
                        label.bindDataSource(SupplierDataSource.of { HTCommonTranslation.STORED_FE.translate(battery.getAmount()) })
                    }.bind(DataBindingBuilder.floatValS2C(battery::getLevelAsFloat).build()),
            )
        }
    }
}
