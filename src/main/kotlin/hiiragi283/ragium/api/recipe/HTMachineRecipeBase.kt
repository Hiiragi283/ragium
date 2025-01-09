package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
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
abstract class HTMachineRecipeBase(val definition: HTMachineDefinition) : Recipe<HTMachineInput> {
    val key: HTMachineKey = definition.key
    val tier: HTMachineTier = definition.tier

    /**
     * アイテムの材料の一覧
     */
    abstract val itemIngredients: List<HTItemIngredient>

    /**
     * 液体の材料の一覧
     */
    abstract val fluidIngredients: List<HTFluidIngredient>

    /**
     * 触媒となる[HTItemIngredient]
     */
    abstract val catalyst: HTItemIngredient?

    /**
     * アイテムの完成品の一覧
     */
    abstract val itemResults: List<HTItemResult>

    /**
     * 液体の完成品の一覧
     */
    abstract val fluidResults: List<HTFluidResult>

    /**
     * 指定した[index]から[HTItemIngredient]を返します。
     */
    fun getItemIngredient(index: Int): HTItemIngredient? = itemIngredients.getOrNull(index)

    /**
     * 指定した[index]から[HTFluidIngredient]を返します。
     */
    fun getFluidIngredient(index: Int): HTFluidIngredient? = fluidIngredients.getOrNull(index)

    /**
     * 指定した[index]から[HTItemResult]を返します。
     */
    fun getItemResult(index: Int): HTItemResult? = itemResults.getOrNull(index)

    /**
     * 指定した[index]から[HTFluidResult]を返します。
     */
    fun getFluidResult(index: Int): HTFluidResult? = fluidResults.getOrNull(index)

    /**
     * このレシピが有効かどうか判定します。
     */
    fun isValid(): Boolean {
        if (key !in RagiumAPI.getInstance().machineRegistry) return false
        val bool1: Boolean = itemIngredients.isNotEmpty() && itemIngredients.none(HTItemIngredient::isEmpty)
        val bool2: Boolean = fluidIngredients.isNotEmpty() && fluidIngredients.none(HTFluidIngredient::isEmpty)
        val bool3: Boolean = itemResults.isNotEmpty() && itemResults.none(HTItemResult::isEmpty)
        val bool4: Boolean = fluidResults.isNotEmpty() && fluidResults.none(HTFluidResult::isEmpty)
        return bool1 || bool2 || bool3 || bool4
    }

    //    Recipe    //

    final override fun craft(input: HTMachineInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    final override fun fits(width: Int, height: Int): Boolean = true

    final override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = getItemResult(0)?.stack ?: ItemStack.EMPTY

    final override fun isIgnoredInRecipeBook(): Boolean = true

    final override fun showNotification(): Boolean = false

    final override fun createIcon(): ItemStack = definition.iconStack

    final override fun isEmpty(): Boolean = true
}
