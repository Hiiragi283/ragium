package hiiragi283.ragium.common.block.entity.component

import hiiragi283.ragium.api.block.attribute.HTUpgradeGroupBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttribute
import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.component.HTComponentInput
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.attachments.HTAttachedItems
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.upgrade.HTSlotUpgradeHandler
import hiiragi283.ragium.api.upgrade.HTUpgradeGroup
import hiiragi283.ragium.api.upgrade.HTUpgradeProvider
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.world.sendBlockUpdated
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentMap

class HTMachineUpgradeComponent(private val owner: HTBlockEntity) :
    HTBlockEntityComponent,
    HTSlotUpgradeHandler {
    init {
        owner.addComponent(this)
    }

    private val upgradeSlots: List<HTBasicItemSlot> = (0..3).map { i: Int ->
        val filter: (ImmutableItemStack) -> Boolean = { isValidUpgrade(it, getUpgrades()) }
        HTBasicItemSlot.create(
            HTContentListener(owner::setChanged).andThen { owner.level?.sendBlockUpdated(owner.blockPos) },
            HTSlotHelper.getSlotPosX(8),
            HTSlotHelper.getSlotPosY(i - 0.5),
            canExtract = HTPredicates.manualOnly(),
            canInsert = HTPredicates.manualOnly(),
            filter = filter,
        )
    }
    private val group: HTUpgradeGroup? = owner.blockHolder
        .getAttribute<HTUpgradeGroupBlockAttribute>()
        ?.group

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
        val provider: HTUpgradeProvider = upgrade.getCapability(RagiumCapabilities.UPGRADE_ITEM) ?: return false
        val group: HTUpgradeGroup = provider.getGroup() ?: return true
        return when (group) {
            this.group -> existing.none { stack: ImmutableItemStack ->
                stack.getCapability(RagiumCapabilities.UPGRADE_ITEM)?.getGroup() == group
            }
            else -> false
        }
    }
}
