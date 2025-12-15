package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe

/**
 * Ragiumで使用する[Recipe]の拡張インターフェース
 * @see mekanism.api.recipes.MekanismRecipe
 */
interface HTRecipe :
    Recipe<HTRecipeInput>,
    HTAbstractRecipe {
    @Deprecated("Not used in Ragium", level = DeprecationLevel.ERROR)
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    @Deprecated(
        "Use `assembleItem(HTRecipeInput, HolderLookup.Provider) `instead",
        ReplaceWith("this.assembleItem(input, registries)"),
        DeprecationLevel.ERROR,
    )
    override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack =
        assembleItem(input, registries)?.unwrap() ?: ItemStack.EMPTY

    @Deprecated("Use `assemble(HTRecipeInput, HolderLookup.Provider) `instead", level = DeprecationLevel.ERROR)
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    @Deprecated("Not used in Ragium", level = DeprecationLevel.ERROR)
    override fun getRemainingItems(input: HTRecipeInput): NonNullList<ItemStack> = super.getRemainingItems(input)

    @Deprecated("Not used in Ragium", level = DeprecationLevel.ERROR)
    override fun getIngredients(): NonNullList<Ingredient> = super.getIngredients()

    override fun isSpecial(): Boolean = true
}
