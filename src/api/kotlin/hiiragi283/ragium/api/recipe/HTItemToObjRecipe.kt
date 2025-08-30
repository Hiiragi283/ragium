package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一の[ItemStack]から完成品を生成するレシピのインターフェース
 * @param RESULT 完成品のクラス
 * @see [HTItemToItemRecipe]
 * @see [HTItemToFluidRecipe]
 */
interface HTItemToObjRecipe<RESULT : HTRecipeResult<*>> : HTRecipe<SingleRecipeInput> {
    /**
     * レシピの材料
     */
    val ingredient: HTItemIngredient

    /**
     * レシピの完成品
     */
    val result: RESULT

    override fun test(input: SingleRecipeInput): Boolean = !isIncomplete && ingredient.test(input.item())

    override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY
}
