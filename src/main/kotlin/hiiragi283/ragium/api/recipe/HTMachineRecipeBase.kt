package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.registry.RegistryWrapper

/**
 * 機械レシピのベースとなるクラス
 * @see hiiragi283.ragium.common.recipe.HTMachineRecipe
 * @see hiiragi283.ragium.api.data.HTMachineRecipeJsonBuilder
 */
interface HTMachineRecipeBase : Recipe<HTMachineInput> {
    val definition: HTMachineDefinition

    val key: HTMachineKey
        get() = definition.key
    val tier: HTMachineTier
        get() = definition.tier

    /**
     * アイテムの材料の一覧
     */
    val itemIngredients: List<HTItemIngredient>

    /**
     * 指定した[index]から[HTItemIngredient]を返します。
     */
    fun getItemIngredient(index: Int): HTItemIngredient? = itemIngredients.getOrNull(index)

    /**
     * 指定した[index]から[HTFluidIngredient]を返します。
     */
    fun getFluidIngredient(index: Int): HTFluidIngredient?

    /**
     * 触媒となる[HTItemIngredient]
     */
    val catalyst: HTItemIngredient?

    /**
     * 指定した[index]から[HTItemResult]を返します。
     */
    fun getItemResult(index: Int): HTItemResult?

    /**
     * 指定した[index]から[HTFluidResult]を返します。
     */
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
