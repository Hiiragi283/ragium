package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.serialization.component.HTComponentInput
import hiiragi283.core.api.serialization.component.HTComponentSerializable
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.upgrade.HTSlotUpgradeHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

class HTMachineUpgradeComponent(private val owner: HTBlockEntity) :
    HTComponentSerializable,
    HTDataSerializable,
    HTSlotUpgradeHandler {
    private val upgradeSlots: List<HTBasicItemSlot> = (0..3).map { i: Int ->
        val filter: (HTItemResourceType) -> Boolean = { isValidUpgrade(it, getUpgrades()) }
        HTBasicItemSlot.create(
            limit = 1,
            canExtract = HTStoragePredicates.manualOnly(),
            canInsert = HTStoragePredicates.manualOnly(),
            filter = filter,
        )
    }

    //    HTComponentSerializable    //

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

    //    HTDataSerializable    //

    override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
    }

    //    HTSlotUpgradeHandler    //

    override fun getUpgradeSlots(): List<HTBasicItemSlot> = upgradeSlots

    override fun isValidUpgrade(upgrade: HTItemResourceType, existing: List<HTItemResourceType>): Boolean {
        val upgradeData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(upgrade) ?: return false
        val isTarget: Boolean = owner.blockState
            .block
            .let(::ItemStack)
            .let(upgradeData::isTarget)
        val isCompatible: Boolean = existing.all { resource: HTItemResourceType -> HTUpgradeData.areCompatible(upgrade, resource) }
        return isTarget && isCompatible
    }
}
