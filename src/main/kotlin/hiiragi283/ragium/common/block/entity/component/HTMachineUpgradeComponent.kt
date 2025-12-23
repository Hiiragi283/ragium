package hiiragi283.ragium.common.block.entity.component

import hiiragi283.core.api.block.entity.HTBlockEntityComponent
import hiiragi283.core.api.serialization.component.HTComponentInput
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.world.sendBlockUpdated
import hiiragi283.core.common.block.entity.HTBlockEntity
import hiiragi283.core.common.storage.HTCapabilityCodec
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.upgrade.HTSlotUpgradeHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.item.ItemStack

class HTMachineUpgradeComponent(private val owner: HTBlockEntity) :
    HTBlockEntityComponent,
    HTSlotUpgradeHandler {
    init {
        owner.addComponent(this)
    }

    private val upgradeSlots: List<HTBasicItemSlot> = (0..3).map { i: Int ->
        val filter: (ImmutableItemStack) -> Boolean = { isValidUpgrade(it, getUpgrades()) }
        HTBasicItemSlot.create(
            {
                owner.setChanged()
                owner.level?.sendBlockUpdated(owner.blockPos)
            },
            limit = 1,
            canExtract = HTStoragePredicates.manualOnly(),
            canInsert = HTStoragePredicates.manualOnly(),
            filter = filter,
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
                upgradeSlots.getOrNull(i)?.setStackUnchecked(contents[i])
            }
        }
    }

    override fun collectComponents(builder: DataComponentMap.Builder) {
        builder.set(
            RagiumDataComponents.MACHINE_UPGRADES,
            upgradeSlots
                .map(HTBasicItemSlot::getStack)
                .let(::HTAttachedItems),
        )
    }
    //    HTSlotUpgradeHandler    //

    override fun getUpgradeSlots(): List<HTItemSlot> = upgradeSlots

    override fun isValidUpgrade(upgrade: ImmutableItemStack, existing: List<ImmutableItemStack>): Boolean {
        val upgradeData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(upgrade) ?: return false
        val isTarget: Boolean = owner.blockState
            .block
            .let(::ItemStack)
            .let(upgradeData::isTarget)
        val isCompatible: Boolean = existing.all { stack: ImmutableItemStack -> HTUpgradeData.areCompatible(upgrade, stack) }
        return isTarget && isCompatible
    }
}
