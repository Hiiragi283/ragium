package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Predicate

/**
 * 機械全般に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(blockHolder, pos, state) {
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

    //    Save & Load    //

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.putBoolean("is_active", this.isActive)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        this.isActive = input.getBoolean("is_active", false)
    }

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        output.putBoolean("is_active", this.isActive)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        this.isActive = input.getBoolean("is_active", false)
    }

    //    Ticking    //

    private var lastActive = false
    var isActive: Boolean = false
        protected set

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val result: Boolean = onUpdateMachine(level, pos, state)
        // 以前の結果と異なる場合は更新する
        if (result != this.isActive) {
            this.lastActive = this.isActive
            this.isActive = result
        }
        return this.lastActive
    }

    protected abstract fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean
}
