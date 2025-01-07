package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.mergeStack
import hiiragi283.ragium.api.extension.orElse
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.world.World

class HTMachineRecipeProcessor(
    val inventory: Inventory,
    private val itemInputs: IntArray,
    private val itemOutputs: IntArray,
    private val catalystIndex: Int,
    private val fluidStorage: HTMachineFluidStorage,
    private val fluidInputs: IntArray,
    private val fluidOutputs: IntArray,
    recipeType: RecipeType<out HTMachineRecipeBase> = RagiumRecipeTypes.MACHINE,
) : HTRecipeProcessor {
    private val recipeCache: HTRecipeCache<HTMachineInput, out HTMachineRecipeBase> = HTRecipeCache(recipeType)

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Result<Unit> {
        val input: HTMachineInput = HTMachineInput.create(key, tier) {
            itemInputs.map(inventory::getStack).forEach(::add)
            fluidInputs.map(fluidStorage::getVariantStack).forEach(::add)
            catalyst = inventory.getStack(catalystIndex)
        }
        return recipeCache
            .getFirstMatch(input, world)
            .runCatching {
                val recipe: HTMachineRecipeBase = getOrThrow(HTMachineException::NoMatchingRecipe)
                when {
                    !canAcceptOutputs(recipe) -> throw HTMachineException.MergeResult(false)
                    else -> {
                        modifyOutputs(recipe)
                        decrementInputs(recipe)
                    }
                }
            }
    }

    private fun canAcceptOutputs(recipe: HTMachineRecipeBase): Boolean {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTItemResult = recipe.getItemResult(index) ?: return@forEachIndexed
            if (!result.canMerge(inventory.getStack(slot))) {
                return false
            }
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTFluidResult = recipe.getFluidResult(index) ?: return@forEachIndexed
            if (!fluidStorage.map(slot, result::canMerge).orElse(false)) {
                return false
            }
        }
        return true
    }

    private fun modifyOutputs(recipe: HTMachineRecipeBase) {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTItemResult = recipe.getItemResult(index) ?: return@forEachIndexed
            inventory.mergeStack(slot, result)
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTFluidResult = recipe.getFluidResult(index) ?: return@forEachIndexed
            useTransaction { transaction: Transaction ->
                fluidStorage.map(slot) { storageIn: SingleFluidStorage ->
                    if (result.merge(storageIn, transaction) == result.amount) {
                        transaction.commit()
                    }
                }
            }
        }
    }

    private fun decrementInputs(recipe: HTMachineRecipeBase) {
        HTShapelessInputResolver
            .resolve(recipe.itemIngredients, itemInputs.map(inventory::getStack))
            .forEach { (ingredient: HTItemIngredient, stack: ItemStack) ->
                ingredient.onConsume(stack)
            }
        fluidInputs.forEachIndexed { index: Int, slot: Int ->
            val ingredient: HTFluidIngredient = recipe.getFluidIngredient(index) ?: return@forEachIndexed
            fluidStorage.map(slot, ingredient::onConsume)
        }
    }
}
