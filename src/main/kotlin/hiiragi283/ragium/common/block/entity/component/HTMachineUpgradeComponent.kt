package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.serialization.component.HTComponentInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.setStack
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.item.ItemStack

class HTMachineUpgradeComponent(private val owner: HTBlockEntity) :
    HTBlockEntityComponent,
    HTUpgradeHandler {
    init {
        owner.addComponent(this)
    }

    private val upgradeSlots: List<HTBasicItemSlot> = List(4) {
        HTBasicItemSlot.create(
            owner::setChanged,
            limit = 1,
            canExtract = HTStoragePredicates.manualOnly(),
            canInsert = HTStoragePredicates.manualOnly(),
            filter = ::isValidUpgrade,
        )
    }

    //    HTBlockEntityComponent    //

    override fun serialize(output: HTValueOutput) {
        HTCapabilityCodec.ITEM.saveTo(output.child("upgrades"), upgradeSlots)
    }

    override fun deserialize(input: HTValueInput) {
        HTCapabilityCodec.ITEM.loadFrom(input.childOrEmpty("upgrades"), upgradeSlots)
    }

    override fun applyComponents(input: HTComponentInput) {
        input.use(RagiumDataComponents.MACHINE_UPGRADES) { contents: HTAttachedItems ->
            for (i: Int in contents.indices) {
                upgradeSlots.getOrNull(i)?.setStack(contents[i])
            }
        }
    }

    override fun collectComponents(builder: DataComponentMap.Builder) {
        builder.set(
            RagiumDataComponents.MACHINE_UPGRADES,
            upgradeSlots
                .map(HTBasicItemSlot::getItemStack)
                .let(::HTAttachedItems),
        )
    }
    //    HTUpgradeHandler    //

    override fun getUpgrades(): List<HTItemResourceType> = upgradeSlots.mapNotNull(HTItemSlot::getResource)

    override fun isValidUpgrade(upgrade: HTItemResourceType): Boolean {
        val upgradeData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(upgrade) ?: return false
        val isTarget: Boolean = owner.blockState
            .block
            .let(::ItemStack)
            .let(upgradeData::isTarget)
        val isCompatible: Boolean = getUpgrades().all { resource: HTItemResourceType -> HTUpgradeData.areCompatible(upgrade, resource) }
        return isTarget && isCompatible
    }
}
