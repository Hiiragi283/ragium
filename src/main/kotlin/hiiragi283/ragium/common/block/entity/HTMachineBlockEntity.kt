package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.world.sendBlockUpdated
import hiiragi283.ragium.common.item.HTComponentItem
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Predicate

/**
 * 機械全般に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    companion object {
        // Slot
        @JvmStatic
        protected fun singleInput(
            builder: HTBasicItemSlotHolder.Builder,
            listener: HTContentListener,
            canInsert: Predicate<ImmutableItemStack> = HTPredicates.alwaysTrue(),
        ): HTItemStackSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0), canInsert = canInsert),
        )

        @JvmStatic
        protected fun singleCatalyst(
            builder: HTBasicItemSlotHolder.Builder,
            listener: HTContentListener,
            canInsert: Predicate<ImmutableItemStack> = HTPredicates.alwaysTrue(),
        ): HTItemStackSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2), 1, canInsert),
        )

        @JvmStatic
        protected fun singleOutput(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener): HTItemStackSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1)),
        )

        @JvmStatic
        protected fun multiOutputs(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener): List<HTItemStackSlot> =
            intArrayOf(5, 6).flatMap { x: Int ->
                doubleArrayOf(0.5, 1.5).map { y: Double ->
                    builder.addSlot(
                        HTSlotInfo.OUTPUT,
                        HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(x), HTSlotHelper.getSlotPosY(y)),
                    )
                }
            }
    }

    lateinit var upgradeSlots: List<HTItemStackSlot>
        private set

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        initializeItemSlots(builder, listener)
        upgradeSlots = (0..3).map { i: Int ->
            val filter: (ImmutableItemStack) -> Boolean = { stack: ImmutableItemStack ->
                when {
                    stack.value() is HTComponentItem -> getMaxMachineTier() == null
                    else -> stack.isOf(RagiumModTags.Items.MACHINE_UPGRADES)
                }
            }
            builder.addSlot(
                HTSlotInfo.CATALYST,
                HTItemStackSlot.create(
                    listener.andThen { level?.sendBlockUpdated(blockPos) },
                    HTSlotHelper.getSlotPosX(8),
                    HTSlotHelper.getSlotPosY(i - 0.5),
                    1,
                    HTPredicates.manualOnly(),
                    HTPredicates.manualOnly(),
                    filter,
                ),
            )
        }
        return builder.build()
    }

    fun hasUpgrade(item: ItemLike): Boolean = upgradeSlots.any { slot: HTItemStackSlot -> slot.getStack()?.isOf(item.asItem()) ?: false }

    fun <T : Any> getUpgradeData(type: DataComponentType<T>): List<T> =
        upgradeSlots.mapNotNull { slot: HTItemStackSlot -> slot.getStack()?.get(type) }

    fun <T : Any> getFirstUpgradeData(type: DataComponentType<T>): T? = getUpgradeData(type).firstOrNull()

    fun getMaxMachineTier(): HTBaseTier? = upgradeSlots
        .asSequence()
        .mapNotNull(HTItemStackSlot::getStack)
        .map(ImmutableItemStack::value)
        .filterIsInstance<HTComponentItem>()
        .map(HTComponentItem::getBaseTier)
        .maxOrNull()

    fun calculateValue(base: Int, type: DataComponentType<LevelBasedValue>): Int {
        var result: Int = base
        for (value: LevelBasedValue in getUpgradeData(type)) {
            result = value.calculate(result).toInt()
        }
        return result
    }

    fun calculateDuration(base: Int): Int = calculateValue(base, RagiumDataComponents.MACHINE_DURATION)

    fun calculateEnergy(base: Int): Int = calculateValue(base, RagiumDataComponents.MACHINE_ENERGY)

    //    Save & Load    //

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.putBoolean("is_active", this.isActive)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        this.isActive = input.getBoolean("is_active", false)
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
