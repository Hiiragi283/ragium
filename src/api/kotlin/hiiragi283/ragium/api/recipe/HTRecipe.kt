package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level
import java.util.function.Predicate

/**
 * Ragiumで使用する[Recipe]の拡張インターフェース
 * @see mekanism.api.recipes.MekanismRecipe
 */
interface HTRecipe<INPUT : RecipeInput> :
    Recipe<INPUT>,
    Predicate<INPUT> {
    /**
     * 指定された[input]がこのレシピの条件を満たしているか判定します。
     */
    override fun test(input: INPUT): Boolean

    override fun matches(input: INPUT, level: Level): Boolean = !isIncomplete && test(input)

    @Deprecated("Not used in Ragium", level = DeprecationLevel.ERROR)
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    fun assembleItem(input: INPUT, provider: HolderLookup.Provider): ImmutableItemStack?

    @Deprecated(
        "Use `assembleItem(INPUT, HolderLookup.Provider) `instead",
        ReplaceWith("this.assembleItem(input, registries)"),
        DeprecationLevel.ERROR,
    )
    override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack =
        assembleItem(input, registries)?.unwrap() ?: ItemStack.EMPTY

    @Deprecated("Use `assemble(INPUT, HolderLookup.Provider) `instead", level = DeprecationLevel.ERROR)
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    @Deprecated("Not used in Ragium", level = DeprecationLevel.ERROR)
    override fun getRemainingItems(input: INPUT): NonNullList<ItemStack> = super.getRemainingItems(input)

    @Deprecated("Not used in Ragium", level = DeprecationLevel.ERROR)
    override fun getIngredients(): NonNullList<Ingredient> = super.getIngredients()

    override fun isSpecial(): Boolean = true

    abstract override fun isIncomplete(): Boolean

    //    Extension    //

    /**
     * 指定された引数からアイテムの完成品を返します。
     * @param input レシピの入力
     * @param provider レジストリのアクセス
     * @param result [ImmutableItemStack]の[HTRecipeResult]
     * @return [test]の戻り値が`false`，または[HTRecipeResult.getStackOrNull]が`null`の場合は`null`
     */
    fun getItemResult(input: INPUT, provider: HolderLookup.Provider?, result: HTItemResult?): ImmutableItemStack? = when {
        test(input) -> result?.getStackOrNull(provider)
        else -> null
    }
}
