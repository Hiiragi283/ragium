package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level
import java.util.Optional
import java.util.function.Predicate
import kotlin.jvm.optionals.getOrNull

/**
 * Ragiumで使用する[Recipe]の拡張インターフェース
 * @see [mekanism.api.recipes.MekanismRecipe]
 */
interface HTRecipe<INPUT : RecipeInput> :
    Recipe<INPUT>,
    Predicate<INPUT> {
    /**
     * 指定された[input]がこのレシピの条件を満たしているか判定します。
     */
    override fun test(input: INPUT): Boolean

    override fun matches(input: INPUT, level: Level): Boolean = !isIncomplete && test(input)

    @Deprecated("Not used in Ragium")
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    @Deprecated("use assemble(INPUT, HolderLookup.Provider) instead of this")
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun isSpecial(): Boolean = true

    abstract override fun isIncomplete(): Boolean

    //    Extension    //

    /**
     * 指定された引数からアイテムの完成品を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @param result [ItemStack]の[HTRecipeResult]
     * @return [test]の戻り値が`false`，または[HTRecipeResult.getStackOrNull]が`null`の場合は[ItemStack.EMPTY]
     */
    fun getItemResult(input: INPUT, registries: HolderLookup.Provider?, result: HTItemResult?): ItemStack = when {
        test(input) -> result?.getStackOrNull(registries)
        else -> null
    } ?: ItemStack.EMPTY

    /**
     * 指定された引数からアイテムの完成品を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @param result [Optional]で包まれた[HTItemResult]
     * @return [test]の戻り値が`false`，または[HTRecipeResult.getStackOrNull]が`null`の場合は[ItemStack.EMPTY]
     */
    fun getItemResult(input: INPUT, registries: HolderLookup.Provider?, result: Optional<HTItemResult>): ItemStack =
        getItemResult(input, registries, result.getOrNull())
}
