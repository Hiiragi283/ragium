package hiiragi283.ragium.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Selector
import com.lowdragmc.lowdraglib2.gui.ui.elements.Tab
import com.lowdragmc.lowdraglib2.gui.ui.elements.TabView
import com.lowdragmc.lowdraglib2.gui.ui.utils.UIElementProvider
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.gui.HTModularUIHelper
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.api.storage.holder.HTFluidTankHolder
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.common.block.entity.HTModularBlockEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.component.HTSlotInfoComponent
import hiiragi283.ragium.common.storge.holder.HTBasicEnergyBatteryHolder
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.common.storge.holder.HTSlotInfoProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiConsumer

/**
 * 搬入出の面を制御可能な[HTModularBlockEntity]の拡張クラス
 * @see mekanism.common.tile.prefab.TileEntityConfigurableMachine
 */
abstract class HTConfigurableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTModularBlockEntity(
        type,
        pos,
        state,
    ),
    HTSlotInfoProvider {
    final override fun createFluidHandler(): HTFluidTankHolder? {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        createFluidTanks(builder)
        return builder.build()
    }

    protected open fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder) {}

    final override fun createEnergyHandler(): HTEnergyBatteryHolder? {
        val builder: HTBasicEnergyBatteryHolder.Builder = HTBasicEnergyBatteryHolder.builder(this)
        createEnergyBattery(builder)
        return builder.build()
    }

    protected open fun createEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder) {}

    final override fun createItemHandler(): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        createItemSlots(builder)
        return builder.build()
    }

    protected open fun createItemSlots(builder: HTBasicItemSlotHolder.Builder) {}

    //    UI    //

    final override fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI = HTModularUIHelper.createVanillaUI(
        UIElement().addChild(
            TabView().apply {
                // Main
                addTab(
                    Tab().setText("Main"),
                    HTModularUIHelper.createRootWithInv(name, ::setupMainTab),
                )
                // Sub
                setupTab(::addTab)
            },
        ),
        holder.player,
    )

    protected abstract fun setupMainTab(root: UIElement)

    protected open fun setupTab(consumer: BiConsumer<Tab, UIElement>) {
        // Slot Info
        val element: UIElement = UIElement().addClass("panel_bg")

        for (direction: Direction in Direction.entries) {
            Selector<HTSlotInfo>()
                .setSelected(getSlotInfo(direction), false)
                .setCandidates(HTSlotInfo.entries)
                .setCandidateUIProvider(UIElementProvider.text { it.getText(direction) })
                .setOnValueChanged(machineSlot::setSlotInfo.partially1(direction))
                .let(element::addChild)
        }

        consumer.accept(Tab().setText("Slot Info"), element)
    }

    //    HTSlotInfoProvider    //

    @DescSynced
    @Persisted(subPersisted = true)
    val machineSlot: HTSlotInfoComponent = HTSlotInfoComponent(this)

    final override fun getSlotInfo(side: Direction): HTSlotInfo = machineSlot.getSlotInfo(side)
}
