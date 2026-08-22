package hiiragi283.ragium.recipe.handler

import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.handler.HTInputSlot
import hiiragi283.lib.recipe.handler.HTOutputSlot
import hiiragi283.lib.recipe.handler.HTRecipeHandler
import hiiragi283.lib.recipe.handler.canInsert
import hiiragi283.lib.recipe.handler.insert
import hiiragi283.lib.recipe.lookup.HTRecipeCache
import hiiragi283.lib.sounds.HTSoundInstance
import hiiragi283.lib.transfer.item.getItemStack
import hiiragi283.lib.transfer.useTransaction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

object HTTestRecipeHandler {
    @JvmStatic
    fun createHandler(
        cache: HTRecipeCache<SingleRecipeInput, HTItemToFluidRecipe>,
        inputSlot: HTInputSlot.Single<ItemResource>,
        outputSlot: HTOutputSlot<FluidResource>,
        energyRate: Int,
        sound: HTSoundInstance,
    ) = object : HTRecipeHandler<SingleRecipeInput, FluidStack, HTItemToFluidRecipe>() {
        override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputSlot.getItemStack())

        override fun findRecipe(level: ServerLevel, input: SingleRecipeInput): HTItemToFluidRecipe? = cache.findFirstRecipe(input, level)

        override fun canComplete(recipe: HTItemToFluidRecipe, input: SingleRecipeInput, output: FluidStack): Boolean {
            val inputCount: Int = recipe.getRequiredAmount(input.item())
            return inputCount != 0 && useTransaction { transaction: Transaction -> inputSlot.canExtract(inputCount, transaction) && outputSlot.canInsert(output, transaction) }
        }

        override fun getMaxProgress(recipe: HTItemToFluidRecipe, input: SingleRecipeInput): Int = recipe.getProgressData(input).getProcessTime(energyRate)

        override fun getProgress(): Int {
            TODO("Not yet implemented")
        }

        override fun onComplete(recipe: HTItemToFluidRecipe, input: SingleRecipeInput, output: FluidStack) {
            useTransaction { transaction: Transaction ->
                inputSlot.extract(recipe.getRequiredAmount(input.item()), transaction)
                outputSlot.insert(output, transaction)
                transaction.commit()
            }
        }
    }
}
