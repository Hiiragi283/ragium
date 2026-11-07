package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTFluidToItemAndFluidRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandlerHelper
import hiiragi283.lib.recipe.result.HTItemAndFluidStack
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTFluidToItemAndFluidBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTFluidToBlockEntity<HTItemAndFluidStack, HTFluidToItemAndFluidRecipe>(type, pos, state) {

    private lateinit var outputTank: HTVariableFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        createInputTank(builder, listener)
        outputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(getTankCapacity(), listener))
    }

    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    HTFluidToBlockEntity    //

    private val outputFluidHandler: HTOutputHandler<FluidStack> = HTOutputHandlerHelper.forFluid(outputTank)
    private val outputItemHandler: HTOutputHandler<ItemStack> = HTOutputHandlerHelper.forItem(outputSlot)

    override fun canComplete(recipe: HTFluidToItemAndFluidRecipe, input: FluidInstance, output: HTItemAndFluidStack): Boolean =
        useTransaction { transaction: Transaction ->
            val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
            when {
                inputHandler.use(recipe.getMatchingStack(input), transaction).failed -> false
                !outputItemHandler.take(itemOutput, transaction).fullOrNoneTaken -> false
                else -> outputFluidHandler.take(fluidOutput, transaction).fullOrNoneTaken
            }
        }

    override fun onComplete(recipe: HTFluidToItemAndFluidRecipe, input: FluidInstance, output: HTItemAndFluidStack) {
        useTransaction { transaction: Transaction ->
            val (itemOutput: ItemStack, fluidOutput: FluidStack) = output
            inputHandler.use(recipe.getMatchingStack(input), transaction)
            outputItemHandler.take(itemOutput, transaction)
            outputFluidHandler.take(fluidOutput, transaction)
            transaction.commit()
        }
        onComplete()
    }
}
