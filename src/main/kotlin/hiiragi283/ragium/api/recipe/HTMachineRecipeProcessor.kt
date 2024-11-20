package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.inventory.Inventory
import net.minecraft.recipe.RecipeEntry
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipeProcessor(
    val inventory: Inventory,
    private val itemInputs: IntArray,
    private val itemOutputs: IntArray,
    private val catalystIndex: Int,
    private val fluidStorage: HTMachineFluidStorage,
    private val fluidInputs: IntArray,
    private val fluidOutputs: IntArray,
) {
    private val matchGetter: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache(RagiumRecipeTypes.MACHINE)

    fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Boolean = runCatching {
        val input: HTMachineInput = HTMachineInput.create(key, tier) {
            itemInputs.map(inventory::getStack).forEach(::add)
            fluidInputs.map(fluidStorage::getResourceAmount).forEach(::add)
            catalyst = inventory.getStack(catalystIndex)
        }
        val recipeEntry: RecipeEntry<HTMachineRecipe> = matchGetter
            .getFirstMatch(input, world)
            .getOrNull() ?: return@runCatching false
        val recipe: HTMachineRecipe = recipeEntry.value
        when {
            !canAcceptOutputs(recipe) -> false
            !tier.canProcess(world) -> false
            else -> {
                modifyOutputs(recipe)
                decrementInputs(recipe)
                true
            }
        }
    }.getOrDefault(false)

    private fun canAcceptOutputs(recipe: HTMachineRecipe): Boolean {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTItemResult = recipe.itemOutputs.getOrNull(index) ?: return@forEachIndexed
            if (!result.canMerge(inventory.getStack(slot))) {
                return false
            }
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTFluidResult = recipe.fluidOutputs.getOrNull(index) ?: return@forEachIndexed
            if (!result.canMerge(fluidStorage.get(slot))) {
                return false
            }
        }
        return true
    }

    private fun modifyOutputs(recipe: HTMachineRecipe) {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTItemResult = recipe.itemOutputs.getOrNull(index) ?: return@forEachIndexed
            inventory.modifyStack(slot, result::merge)
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val result: HTFluidResult = recipe.fluidOutputs.getOrNull(index) ?: return@forEachIndexed
            useTransaction { transaction: Transaction ->
                val inserted: Long = result.merge(fluidStorage.get(slot), transaction)
                if (inserted == result.amount) {
                    transaction.commit()
                } else {
                    transaction.abort()
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
            val storageIn: SingleFluidStorage = fluidStorage.get(slot)
            ingredient.onConsume(storageIn)
        }
    }
}
