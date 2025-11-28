package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProgressGeneratorBlockEntity<SingleRecipeInput, ImmutableItemStack>(RagiumBlocks.THERMAL_GENERATOR, pos, state) {
    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var remainderSlot: HTItemStackSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener, canInsert = { stack: ImmutableItemStack -> stack.unwrap().getBurnTime(null) > 0 })
        // output
        remainderSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
    }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = inputSlot.toRecipeInput()

    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): ImmutableItemStack? = input.item().toImmutable()

    override fun getEnergyToGenerate(recipe: ImmutableItemStack): Int = recipe.unwrap().getBurnTime(null)

    override fun onGenerationUpdated(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: ImmutableItemStack,
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
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
