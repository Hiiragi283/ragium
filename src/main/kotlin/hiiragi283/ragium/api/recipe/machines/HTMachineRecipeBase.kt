package hiiragi283.ragium.api.recipe.machines

import hiiragi283.ragium.api.extension.itemStack
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTFluidIngredient
import hiiragi283.ragium.api.recipe.HTFluidResult
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTItemResult
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.RegistryWrapper

abstract class HTMachineRecipeBase<T : RecipeInput>(val definition: HTMachineDefinition) : Recipe<T> {
    constructor(type: HTMachineConvertible, tier: HTMachineTier) : this(HTMachineDefinition(type, tier))

    val machineType: HTMachineType
        get() = definition.type
    val tier: HTMachineTier
        get() = definition.tier

    abstract val itemInputs: List<HTItemIngredient>
    abstract val fluidInputs: List<HTFluidIngredient>
    abstract val catalyst: HTItemIngredient
    abstract val itemOutputs: List<HTItemResult>
    abstract val fluidOutputs: List<HTFluidResult>

    val firstOutput: ItemStack
        get() = itemOutputs.getOrNull(0)?.resourceAmount?.itemStack ?: ItemStack.EMPTY

    //    Recipe    //

    override fun craft(input: T, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = firstOutput

    final override fun isIgnoredInRecipeBook(): Boolean = true

    final override fun showNotification(): Boolean = false

    final override fun createIcon(): ItemStack = definition.iconStack

    final override fun isEmpty(): Boolean = true
}
