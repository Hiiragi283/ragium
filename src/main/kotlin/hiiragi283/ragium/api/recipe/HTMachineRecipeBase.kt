package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.registry.RegistryWrapper

interface HTMachineRecipeBase : Recipe<HTMachineInput> {
    val definition: HTMachineDefinition

    val key: HTMachineKey
        get() = definition.key
    val tier: HTMachineTier
        get() = definition.tier

    val itemIngredients: List<HTItemIngredient>

    fun getItemIngredient(index: Int): HTItemIngredient? = itemIngredients.getOrNull(index)

    fun getFluidIngredient(index: Int): HTFluidIngredient?

    val catalyst: HTItemIngredient?

    fun getItemResult(index: Int): HTItemResult?

    fun getFluidResult(index: Int): HTFluidResult?

    //    Recipe    //

    override fun craft(input: HTMachineInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = getItemResult(0)?.stack ?: ItemStack.EMPTY

    override fun isIgnoredInRecipeBook(): Boolean = true

    override fun showNotification(): Boolean = false

    override fun createIcon(): ItemStack = definition.iconStack

    override fun isEmpty(): Boolean = true
}
