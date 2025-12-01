package hiiragi283.ragium.common.block.entity.generator.base

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemGeneratorBlockEntity<RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTProgressGeneratorBlockEntity<SingleRecipeInput, RECIPE>(blockHolder, pos, state) {
    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var remainderSlot: HTBasicItemSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot =
            singleInput(
                builder,
                listener,
                canInsert = { stack: ImmutableItemStack -> stack.unwrap().getFoodProperties(null) != null },
            )
        // output
        remainderSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
    }

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = inputSlot.toRecipeInput()

    override fun onGenerationUpdated(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: RECIPE,
    ) {
        // インプットを減らす, 返却物がある場合は移動
        HTStackSlotHelper.shrinkItemStack(
            inputSlot,
            { stack: ImmutableItemStack? ->
                val remainder: ImmutableItemStack? = remainderSlot.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                HTItemDropHelper.dropStackAt(level, pos, remainder)
            },
            { 1 },
            HTStorageAction.EXECUTE,
        )
    }
}
