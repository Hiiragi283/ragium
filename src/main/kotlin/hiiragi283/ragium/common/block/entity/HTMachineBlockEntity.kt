package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.world.sendBlockUpdated
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.HTCapabilityCodec
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * 機械全般に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state),
    HTUpgradableBlockEntity {
    companion object {
        // Slot
        @JvmStatic
        protected fun singleInput(
            builder: HTBasicItemSlotHolder.Builder,
            listener: HTContentListener,
            canInsert: Predicate<ImmutableItemStack> = HTPredicates.alwaysTrue(),
        ): HTBasicItemSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0), canInsert = canInsert),
        )

        @JvmStatic
        protected fun singleOutput(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener): HTBasicItemSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1)),
        )

        @JvmStatic
        protected fun upperOutput(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener): HTBasicItemSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0.5)),
        )

        @JvmStatic
        protected fun extraOutput(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener): HTBasicItemSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2)),
        )
    }

    //    HTUpgradableBlockEntity    //

    val upgradeSlots: List<HTBasicItemSlot> = (0..3).map { i: Int ->
        val filter: (ImmutableItemStack) -> Boolean = filter@{ stack: ImmutableItemStack ->
            canApplyUpgrade(stack.unwrap()) && !hasUpgrade(stack.value())
        }
        HTBasicItemSlot.create(
            HTContentListener(::setOnlySave).andThen { level?.sendBlockUpdated(blockPos) },
            HTSlotHelper.getSlotPosX(8),
            HTSlotHelper.getSlotPosY(i - 0.5),
            canExtract = HTPredicates.manualOnly(),
            canInsert = HTPredicates.manualOnly(),
            filter = filter,
        )
    }

    final override fun hasUpgrade(item: ItemLike): Boolean = upgradeSlots.any { slot: HTBasicItemSlot ->
        slot.getStack()?.isOf(item.asItem()) ?: false
    }

    override fun getMachineUpgrades(): List<Pair<HTMachineUpgrade, Int>> = upgradeSlots.mapNotNull { slot: HTBasicItemSlot ->
        val upgrade: HTMachineUpgrade = slot
            .getStack()
            ?.unwrap()
            ?.let { RagiumPlatform.INSTANCE.getMachineUpgrade(getRegistryAccess(), it) }
            ?: return@mapNotNull null
        upgrade to slot.getAmount()
    }

    override fun canApplyUpgrade(stack: ItemStack): Boolean {
        if (RagiumPlatform.INSTANCE.getMachineUpgrade(getRegistryAccess(), stack) == null) return false
        val filter: HTKeyOrTagEntry<BlockEntityType<*>> = stack.get(RagiumDataComponents.MACHINE_UPGRADE_FILTER) ?: return true
        return filter.isOf(getBlockEntityType(this.blockHolder))
    }

    override fun collectDrops(consumer: Consumer<ImmutableItemStack>) {
        super.collectDrops(consumer)
        upgradeSlots.mapNotNull(HTBasicItemSlot::getStack).forEach(consumer)
    }

    //    Save & Load    //

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.putBoolean("is_active", this.isActive)
        HTCapabilityCodec.ITEM.saveTo(output.child("upgrades"), upgradeSlots)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        this.isActive = input.getBoolean("is_active", false)
        HTCapabilityCodec.ITEM.loadFrom(input.childOrEmpty("upgrades"), upgradeSlots)
    }

    //    Ticking    //

    var isActive: Boolean = false
        protected set

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val result: Boolean = onUpdateMachine(level, pos, state)
        // 以前の結果と異なる場合は強制的に同期させる
        if (result != this.isActive) {
            isActive = result
            return true
        }
        return result
    }

    protected abstract fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean
}
