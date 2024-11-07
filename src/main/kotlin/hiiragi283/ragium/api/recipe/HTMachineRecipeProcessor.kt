package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
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
    private val fluidStorage: HTMachineFluidStorage? = null,
    private val fluidInputs: IntArray = intArrayOf(),
    private val fluidOutputs: IntArray = intArrayOf(),
) {
    private val matchGetter: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache(RagiumRecipeTypes.MACHINE)

    fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Boolean = runCatching {
        val input: HTMachineInput = HTMachineInput.create(key, tier) {
            itemInputs.map(inventory::getStack).forEach(::add)
            fluidInputs.forEach { fluidStorage?.getResourceAmount(it)?.let(::add) }
            catalyst = inventory.getStack(catalystIndex)
        }
        val recipeEntry: RecipeEntry<HTMachineRecipe> = matchGetter
            .getFirstMatch(input, world)
            .getOrNull() ?: return@runCatching false
        val recipe: HTMachineRecipe = recipeEntry.value
        if (!canAcceptOutputs(recipe)) return@runCatching false
        if (!tier.canProcess(world)) return@runCatching false
        modifyOutputs(recipe)
        decrementInputs(recipe)
        true
    }.getOrDefault(false)

    private fun canAcceptOutputs(recipe: HTMachineRecipe): Boolean {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            recipe.itemOutputs.getOrNull(index)?.let { result: HTItemResult ->
                if (!result.canMerge(inventory.getStack(slot))) {
                    return false
                }
            }
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            recipe.fluidOutputs.getOrNull(index)?.let { result: HTFluidResult ->
                val storageIn: SingleFluidStorage = fluidStorage?.get(slot) ?: return@let
                if (!result.canMerge(storageIn)) {
                    return false
                }
            }
        }
        return true
    }

    private fun modifyOutputs(recipe: HTMachineRecipe) {
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            recipe.itemOutputs.getOrNull(index)?.let { result: HTItemResult ->
                inventory.modifyStack(slot, result::merge)
            }
        }
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            recipe.fluidOutputs.getOrNull(index)?.let { result: HTFluidResult ->
                useTransaction { transaction: Transaction ->
                    val storageIn: SingleSlotStorage<FluidVariant> = fluidStorage?.get(slot) ?: return@let
                    val inserted: Long = result.merge(storageIn, transaction)
                    if (inserted > 0) {
                        transaction.commit()
                    } else {
                        transaction.abort()
                    }
                }
            }
        }
    }

    private fun decrementInputs(recipe: HTMachineRecipe) {
        itemInputs.forEachIndexed { index: Int, slot: Int ->
            recipe.itemInputs.getOrNull(index)?.let { ingredient: HTIngredient.Item ->
                ingredient.onConsume(inventory.getStack(slot))
            }
        }
        fluidInputs.forEachIndexed { index: Int, slot: Int ->
            recipe.fluidInputs.getOrNull(index)?.let { ingredient: HTIngredient.Fluid ->
                val storageIn: SingleSlotStorage<FluidVariant> = fluidStorage?.get(slot) ?: return@let
                useTransaction { transaction: Transaction ->
                    val variantIn: FluidVariant = storageIn.resource
                    val extracted: Long = storageIn.extract(variantIn, ingredient.amount, transaction)
                    if (extracted > 0) {
                        transaction.commit()
                    } else {
                        transaction.abort()
                    }
                }
            }
        }
    }
}
