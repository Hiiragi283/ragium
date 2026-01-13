package hiiragi283.ragium.common.block.entity

import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Tab
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.gui.element.HTItemSlotElement
import hiiragi283.core.api.gui.element.addCenterLabel
import hiiragi283.core.api.gui.element.addChildren
import hiiragi283.core.api.gui.element.addInventory
import hiiragi283.core.api.gui.element.addRowChild
import hiiragi283.core.api.gui.element.alineCenter
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.common.block.entity.component.HTMachineUpgradeComponent
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiConsumer

abstract class HTUpgradableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTUpgradeHandler {
    //    UI    //

    override fun setupTab(consumer: BiConsumer<Tab, UIElement>) {
        super.setupTab(consumer)
        // Upgrade
        consumer.accept(
            Tab().setText("Upgrades"),
            UIElement()
                .alineCenter()
                .addCenterLabel(name)
                .addClass("panel_bg")
                .addRowChild {
                    alineCenter()
                    addChildren(machineUpgrade.upgradeSlots.map(::HTItemSlotElement))
                }.addInventory(),
        )
    }

    //    HTUpgradeHandler    //

    @DescSynced
    @Persisted(subPersisted = true)
    val machineUpgrade: HTMachineUpgradeComponent = HTMachineUpgradeComponent(this)

    final override fun getUpgrades(): List<HTItemResourceType> = machineUpgrade.getUpgrades()

    final override fun isValidUpgrade(upgrade: HTItemResourceType, existing: List<HTItemResourceType>): Boolean =
        machineUpgrade.isValidUpgrade(upgrade, existing)
}
