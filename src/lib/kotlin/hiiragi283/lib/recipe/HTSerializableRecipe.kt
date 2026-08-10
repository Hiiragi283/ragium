package hiiragi283.lib.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.PlacementInfo
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeBookCategories
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * シリアライズ可能なレシピを表す，[Recipe]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - MekanismRecipe](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/MekanismRecipe.java)
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTSerializableRecipe<INPUT : RecipeInput> :
    Recipe<INPUT>,
    HTRecipePredicate<INPUT> {
    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun matches(input: INPUT, level: Level): Boolean = matches(input)

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun assemble(input: INPUT): ItemStack = ItemStack.EMPTY

    override fun isSpecial(): Boolean = true

    override fun showNotification(): Boolean = false

    override fun group(): String = ""

    override fun placementInfo(): PlacementInfo = PlacementInfo.NOT_PLACEABLE

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun recipeBookCategory(): RecipeBookCategory = RecipeBookCategories.CRAFTING_MISC
}
