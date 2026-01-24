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
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiConsumer

abstract class HTUpgradableBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTUpgradeHandler {
    //    UI    //

    override fun setupTab(consumer: BiConsumer<Tab, UIElement>) {
        super.setupTab(consumer)
        // Upgrade
        if (!enableUpgradeTab()) return
        consumer.accept(
            Tab().setText("Upgrades"),
            UIElement()
                .alineCenter()
                .addCenterLabel(name)
                .addClass("panel_bg")
                .addRowChild {
                    alineCenter()
                    addChildren(upgradeSlots.map(::HTItemSlotElement))
                }.addInventory(),
        )
    }

    protected open fun enableUpgradeTab(): Boolean = true

    //    HTUpgradeHandler    //

    @DescSynced
    @Persisted(subPersisted = true)
    val upgradeSlots: List<HTBasicItemSlot> = List(4) {
        HTBasicItemSlot.create(
            limit = 1,
            canExtract = HTStoragePredicates.manualOnly(),
            canInsert = HTStoragePredicates.manualOnly(),
            filter = ::isValidUpgrade,
        )
    }

    final override fun getUpgrades(): List<HTItemResourceType> = upgradeSlots.mapNotNull(HTItemSlot::getResource)

    final override fun isValidUpgrade(upgrade: HTItemResourceType): Boolean {
        val upgradeData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(upgrade) ?: return false
        val isTarget: Boolean = this.blockState
            .block
            .let(::ItemStack)
            .let(upgradeData::isTarget)
        val isCompatible: Boolean = getUpgrades().all { resource: HTItemResourceType -> HTUpgradeData.areCompatible(upgrade, resource) }
        return isTarget && isCompatible
    }
}
