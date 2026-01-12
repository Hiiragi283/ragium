package hiiragi283.ragium.common.block.entity

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Tab
import com.lowdragmc.lowdraglib2.gui.ui.elements.TabView
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.gui.element.addCenterLabel
import hiiragi283.core.api.gui.element.addChildren
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.gui.slot.toSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.util.HTModularUIHelper
import hiiragi283.ragium.api.upgrade.HTSlotUpgradeHandler
import hiiragi283.ragium.common.block.entity.component.HTMachineUpgradeComponent
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

abstract class HTUpgradableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTSlotUpgradeHandler {
    //    UI    //

    final override fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI = HTModularUIHelper.createVanillaUI(
        UIElement()
            .addChild(
                TabView()
                    .addTab(
                        Tab().setText("Main"),
                        HTModularUIHelper.createRootWithInv(name, ::setupMainTab),
                    ).addTab(
                        Tab().setText("Upgrades"),
                        UIElement()
                            .alineCenter()
                            .addCenterLabel(name)
                            .addClass("panel_bg")
                            .addRowChild {
                                alineCenter()
                                addChildren(machineUpgrade.getUpgradeSlots().map(HTBasicItemSlot::toSlot))
                            }.addChild(InventorySlots().layout { it.marginTop(5f) }),
                    ),
            ),
        holder.player,
    )

    protected abstract fun setupMainTab(root: UIElement)

    //    HTSlotUpgradeHandler    //

    @DescSynced
    @Persisted(subPersisted = true)
    val machineUpgrade: HTMachineUpgradeComponent = HTMachineUpgradeComponent(this)

    final override fun getUpgradeSlots(): List<HTItemSlot> = machineUpgrade.getUpgradeSlots()

    override fun isValidUpgrade(upgrade: HTItemResourceType, existing: List<HTItemResourceType>): Boolean =
        machineUpgrade.isValidUpgrade(upgrade, existing)
}
