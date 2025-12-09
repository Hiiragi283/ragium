package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
import hiiragi283.ragium.common.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTAbstractCombinerBlockEntity : HTProcessorBlockEntity.Cached<HTCombineRecipe> {
    constructor(
        recipeCache: HTRecipeCache<HTRecipeInput, HTCombineRecipe>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : super(recipeCache, blockHolder, pos, state)

    constructor(
        finder: HTRecipeFinder<HTRecipeInput, HTCombineRecipe>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : super(finder, blockHolder, pos, state)

    lateinit var inputTank: HTBasicFluidTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, blockHolder.getFluidAttribute().getInputTank(), canInsert = ::filterFluid),
        )
    }

    protected abstract fun filterFluid(stack: ImmutableFluidStack): Boolean

    lateinit var leftInputSlot: HTBasicItemSlot
        private set
    lateinit var rightInputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // inputs
        leftInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(0)),
        )
        rightInputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(listener, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0)),
        )
        // output
        outputSlot = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTOutputItemSlot.create(listener, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(1)),
        )
    }

    final override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    final override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTRecipeInput? = HTRecipeInput.create {
        items += leftInputSlot.getStack()
        items += rightInputSlot.getStack()
        fluids += inputTank.getStack()
    }

    final override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTCombineRecipe): Boolean =
        HTStackSlotHelper.canInsertStack(outputSlot, input, level, recipe::assembleItem)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
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
