package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.mergeStack
import hiiragi283.ragium.api.extension.unitMap
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.inventory.Inventory
import net.minecraft.world.World

class HTMachineRecipeProcessor(
    val inventory: Inventory,
    private val itemInputs: IntArray,
    private val itemOutputs: IntArray,
    private val catalystIndex: Int,
    private val fluidStorage: HTMachineFluidStorage,
    private val fluidInputs: IntArray,
    private val fluidOutputs: IntArray,
) : HTRecipeProcessor {
    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache(RagiumRecipeTypes.MACHINE)

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): HTUnitResult {
        val input: HTMachineInput = HTMachineInput.create(key, tier) {
            itemInputs.map(inventory::getStack).forEach(::add)
            fluidInputs.map(fluidStorage::getVariantStack).forEach(::add)
            catalyst = inventory.getStack(catalystIndex)
        }
        return recipeCache
            .getFirstMatch(input, world)
            .unitMap { recipe: HTMachineRecipe ->
                when {
                    !canAcceptOutputs(recipe) -> HTUnitResult.errorString { "Failed to merge results into outputs!" }
                    else -> {
                        modifyOutputs(recipe)
                        decrementInputs(recipe)
                        HTUnitResult.success()
                    }
                }
            }
    }

    private fun canAcceptOutputs(recipe: HTMachineRecipe): Boolean {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTItemResult = recipe.itemOutputs.getOrNull(index) ?: return@forEachIndexed
            if (!result.canMerge(inventory.getStack(slot))) {
                return false
            }
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTFluidResult = recipe.fluidOutputs.getOrNull(index) ?: return@forEachIndexed
            if (!fluidStorage.map(slot, result::canMerge).result().orElse(false)) {
                return false
            }
        }
        return true
    }

    private fun modifyOutputs(recipe: HTMachineRecipe) {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTItemResult = recipe.itemOutputs.getOrNull(index) ?: return@forEachIndexed
            inventory.mergeStack(slot, result)
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTFluidResult = recipe.fluidOutputs.getOrNull(index) ?: return@forEachIndexed
            useTransaction { transaction: Transaction ->
                fluidStorage.map(slot) { storageIn: SingleFluidStorage ->
                    if (result.merge(storageIn, transaction) == result.amount) {
                        transaction.commit()
                    }
                }
            }
        }
    }

    private fun decrementInputs(recipe: HTMachineRecipe) {
        itemInputs.forEachIndexed { index: Int, slot: Int ->
            val ingredient: HTItemIngredient = recipe.itemInputs.getOrNull(index) ?: return@forEachIndexed
            ingredient.onConsume(inventory.getStack(slot))
        }
        fluidInputs.forEachIndexed { index: Int, slot: Int ->
            val ingredient: HTFluidIngredient = recipe.fluidInputs.getOrNull(index) ?: return@forEachIndexed
            fluidStorage.map(slot, ingredient::onConsume)
        }
    }
}
