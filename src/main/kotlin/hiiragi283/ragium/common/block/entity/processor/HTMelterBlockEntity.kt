package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.single.HTSingleFluidRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.getCraftingRemainingItem
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.base.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity.Cached<HTSingleFluidRecipe>(RagiumRecipeTypes.MELTING, RagiumBlocks.MELTER, pos, state) {
    lateinit var remainderSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = singleInput(builder, listener)
        // output
        remainderSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)),
        )
    }

    lateinit var outputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // output
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(
                listener,
                blockHolder.getFluidAttribute().getOutputTank(),
            ),
        )
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputTank.getNeeded() > 0

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTSingleFluidRecipe): Boolean =
        HTStackSlotHelper.canInsertStack(outputTank, input, level, recipe::assembleFluid)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTSingleFluidRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputTank.insert(recipe.assembleFluid(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす, 返却物がある場合は移動
        HTStackSlotHelper.shrinkItemStack(
            inputSlot,
            ImmutableItemStack::getCraftingRemainingItem,
            { stack: ImmutableItemStack ->
                val remainder: ImmutableItemStack? = remainderSlot.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                HTItemDropHelper.dropStackAt(level, pos, remainder)
            },
            recipe.getRequiredCount(),
            HTStorageAction.EXECUTE,
        )
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.WITCH_DRINK, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
