package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.insert
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.util.HTMachineException
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class HTMachineRecipeProcessor private constructor(
    val machineKey: HTMachineKey,
    val inventory: Inventory,
    private val itemInputs: IntArray,
    itemOutputs: IntArray,
    private val catalystIndex: Int,
    private val fluidStorage: HTMachineFluidStorage,
    private val fluidInputSlots: IntArray,
    fluidOutputs: IntArray,
) : HTRecipeProcessor {
    constructor(
        machineKey: HTMachineKey,
        inventory: HTMachineInventory,
        fluidStorage: HTMachineFluidStorage,
    ) : this(
        machineKey,
        inventory,
        inventory.inputs,
        inventory.outputs,
        inventory.catalyst.asInt,
        fluidStorage,
        fluidStorage.inputs.map(HTTieredFluidStorage::syncIndex).toIntArray(),
        fluidStorage.outputs.map(HTTieredFluidStorage::syncIndex).toIntArray(),
    )

    private val recipeCache: HTRecipeCache<HTMachineInput, out HTMachineRecipe> =
        HTRecipeCache(machineKey.getEntryOrNull()!!.getOrDefault(HTMachinePropertyKeys.RECIPE_TYPE))

    private val inputStacks: List<ItemStack>
        get() = itemInputs.map(inventory::getStack)

    private val itemStorage: InventoryStorage = InventoryStorage.of(inventory, null)
    private val itemOutputs: Storage<ItemVariant> = itemOutputs.map(itemStorage::getSlot).let(::CombinedStorage)

    private val fluidInputs: Storage<FluidVariant> = fluidInputSlots
        .map(fluidStorage::getStorage)
        .mapNotNull(DataResult<HTTieredFluidStorage>::getOrNull)
        .let(::CombinedStorage)
    private val fluidOutputs: Storage<FluidVariant> = fluidOutputs
        .map(fluidStorage::getStorage)
        .mapNotNull(DataResult<HTTieredFluidStorage>::getOrNull)
        .let(::CombinedStorage)

    override fun process(world: World, tier: HTMachineTier): Result<Unit> {
        val input: HTMachineInput = HTMachineInput.create(machineKey, tier) {
            inputStacks.forEach(::add)
            fluidInputSlots.map(fluidStorage::getVariantStack).forEach(::add)
            catalyst = inventory.getStack(catalystIndex)
        }
        return recipeCache
            .getFirstMatch(input, world)
            .runCatching {
                val recipe: HTMachineRecipe = getOrThrow(HTMachineException::NoMatchingRecipe)
                when {
                    !canAcceptOutputs(recipe) -> throw HTMachineException.MergeResult(false)
                    else -> {
                        modifyOutputs(recipe)
                        decrementInputs(recipe)
                    }
                }
            }
    }

    private fun canAcceptOutputs(recipe: HTMachineRecipe): Boolean {
        // try to insert item results
        recipe.data.itemResults.forEach { result: HTItemResult ->
            val (variant: ItemVariant, amount: Long) = result.variantStack
            if (StorageUtil.simulateInsert(itemOutputs, variant, amount, null) != amount) {
                return false
            }
        }
        // try to insert fluid results
        recipe.data.fluidResults.forEach { result: HTFluidResult ->
            val amount: Long = result.amount
            if (StorageUtil.simulateInsert(fluidOutputs, result.variant, amount, null) != amount) {
                return false
            }
        }
        return true
    }

    private fun modifyOutputs(recipe: HTMachineRecipe) {
        // insert item results
        recipe.data.itemResults.forEach { result: HTItemResult ->
            useTransaction { transaction: Transaction ->
                itemOutputs.insert(result.variantStack, transaction)
                transaction.commit()
            }
        }
        // insert fluid results
        recipe.data.fluidResults.forEach { result: HTFluidResult ->
            useTransaction { transaction: Transaction ->
                fluidOutputs.insert(result.variant, result.amount, transaction)
                transaction.commit()
            }
        }
    }

    private fun decrementInputs(recipe: HTMachineRecipe) {
        // extract item ingredients
        HTShapelessInputResolver
            .resolve(recipe.data.itemIngredients, inputStacks)
            .forEach { (ingredient: HTItemIngredient, stack: ItemStack) ->
                ingredient.onConsume(stack)
            }
        // extract fluid ingredients
        fluidInputSlots.forEachIndexed { index: Int, slot: Int ->
            val ingredient: HTFluidIngredient = recipe.getFluidIngredient(index) ?: return@forEachIndexed
            fluidStorage.map(slot, ingredient::onConsume)
        }
    }
}
