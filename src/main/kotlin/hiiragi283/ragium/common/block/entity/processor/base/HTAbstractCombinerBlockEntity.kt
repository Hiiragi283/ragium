package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.HTEnergizedProcessorBlockEntity
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTAbstractCombinerBlockEntity : HTEnergizedProcessorBlockEntity.Cached<HTMultiRecipeInput, HTCombineRecipe> {
    constructor(
        recipeCache: HTRecipeCache<HTMultiRecipeInput, HTCombineRecipe>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : super(recipeCache, blockHolder, pos, state)

    constructor(
        finder: HTRecipeFinder<HTMultiRecipeInput, HTCombineRecipe>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : super(finder, blockHolder, pos, state)

    lateinit var inputTank: HTFluidStackTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(listener, blockHolder.getFluidAttribute().getInputTank(), canInsert = ::filterFluid),
        )
    }

    protected abstract fun filterFluid(stack: ImmutableFluidStack): Boolean

    lateinit var leftInputSlot: HTItemStackSlot
        private set
    lateinit var rightInputSlot: HTItemStackSlot
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // inputs
        leftInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(0)),
        )
        rightInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0)),
        )
        // output
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemStackSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1)),
        )
    }

    final override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput? = HTMultiRecipeInput.create {
        items += leftInputSlot.getStack()
        items += rightInputSlot.getStack()
        fluids += inputTank.getStack()
    }

    final override fun canProgressRecipe(level: ServerLevel, input: HTMultiRecipeInput, recipe: HTCombineRecipe): Boolean = outputSlot
        .insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: HTCombineRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.assembleItem(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        HTStackSlotHelper.shrinkStack(leftInputSlot, recipe::getLeftRequiredCount, HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(rightInputSlot, recipe::getRightRequiredCount, HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(inputTank, recipe::getRequiredAmount.partially1(input), HTStorageAction.EXECUTE)
    }
}
