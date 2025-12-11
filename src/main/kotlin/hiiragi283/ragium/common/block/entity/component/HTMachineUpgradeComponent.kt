package hiiragi283.ragium.common.block.entity.component

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.entity.HTBlockEntityWithUpgrade
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.item.component.HTStackContents
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.serialization.component.HTComponentInput
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.world.sendBlockUpdated
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.BlockEntityType

class HTMachineUpgradeComponent(private val owner: HTBlockEntity) :
    HTBlockEntityComponent,
    HTBlockEntityWithUpgrade {
    init {
        owner.addComponent(this)
    }

    val upgradeSlots: List<HTBasicItemSlot> = (0..3).map { i: Int ->
        val filter: (ImmutableItemStack) -> Boolean = filter@{ stack: ImmutableItemStack ->
            canApplyUpgrade(stack.unwrap()) && !hasUpgrade(stack.value())
        }
        HTBasicItemSlot.create(
            HTContentListener(owner::setChanged).andThen { owner.level?.sendBlockUpdated(owner.blockPos) },
            HTSlotHelper.getSlotPosX(8),
            HTSlotHelper.getSlotPosY(i - 0.5),
            canExtract = HTPredicates.manualOnly(),
            canInsert = HTPredicates.manualOnly(),
            filter = filter,
        )
    }

    /**
     * 指定した[item]を保持しているかどうか判定します。
     */
    override fun hasUpgrade(item: ItemLike): Boolean = upgradeSlots.any { slot: HTBasicItemSlot ->
        slot.getStack()?.isOf(item.asItem()) ?: false
    }

    /**
     * 現在保持しているアップグレードの一覧を返します。。
     */
    override fun getMachineUpgrades(): List<Pair<HTMachineUpgrade, Int>> = upgradeSlots.mapNotNull { slot: HTBasicItemSlot ->
        val upgrade: HTMachineUpgrade = slot
            .getStack()
            ?.unwrap()
            ?.let { RagiumPlatform.INSTANCE.getMachineUpgrade(owner.getRegistryAccess(), it) }
            ?: return@mapNotNull null
        upgrade to slot.getAmount()
    }

    override fun canApplyUpgrade(stack: ItemStack): Boolean {
        if (RagiumPlatform.INSTANCE.getMachineUpgrade(owner.getRegistryAccess(), stack) == null) return false
        val filter: HTKeyOrTagEntry<BlockEntityType<*>> = stack.get(RagiumDataComponents.MACHINE_UPGRADE_FILTER) ?: return true
        return filter.isOf(HTBlockEntity.getBlockEntityType(owner.blockHolder))
    }

    //    HTComponentSerializable    //

    override fun applyComponents(input: HTComponentInput) {
        input.use(RagiumDataComponents.MACHINE_UPGRADES) { contents: HTItemContents ->
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
                .let(HTStackContents.Companion::fromNullable),
        )
    }

    //    HTValueSerializable    //

    override fun serialize(output: HTValueOutput) {
        HTCapabilityCodec.ITEM.saveTo(output.child("upgrades"), upgradeSlots)
    }

    override fun deserialize(input: HTValueInput) {
        HTCapabilityCodec.ITEM.loadFrom(input.childOrEmpty("upgrades"), upgradeSlots)
    }
}
