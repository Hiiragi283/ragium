package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/**
 * 機械全般に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    companion object {
        @JvmStatic
        fun getComponentTier(stack: ImmutableItemStack): HTComponentTier? {
            for ((tier: HTComponentTier, item: HTDeferredItem<*>) in RagiumItems.COMPONENTS) {
                if (stack.isOf(item)) {
                    return tier
                }
            }
            return null
        }

        // Slot
        @JvmStatic
        protected fun singleInput(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener): HTItemStackSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)),
        )

        @JvmStatic
        protected fun singleCatalyst(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener): HTItemStackSlot =
            builder.addSlot(
                HTSlotInfo.CATALYST,
                HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2), 1),
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

    fun getComponentTier(): HTComponentTier? = upgradeSlots[3].getStack()?.let(::getComponentTier)

    final override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        initializeItemSlots(builder, listener)
        upgradeSlots = (0..3).map { i: Int ->
            val filter: (ImmutableItemStack) -> Boolean = when (i) {
                3 -> { stack -> getComponentTier(stack) != null }
                else -> { stack -> getComponentTier(stack) == null }
            }
            builder.addSlot(
                HTSlotInfo.CATALYST,
                HTItemStackSlot.create(
                    listener,
                    HTSlotHelper.getSlotPosX(8),
                    HTSlotHelper.getSlotPosY(i - 0.5),
                    canExtract = HTPredicates.manualOnly(),
                    canInsert = HTPredicates.manualOnly(),
                    filter = filter,
                ),
            )
        }
        return builder.build()
    }

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
