package hiiragi283.ragium.api.recipe.fluid

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import net.minecraft.core.HolderLookup

/**
 * 液体を完成品にとるレシピのインターフェース
 */
interface HTFluidRecipe : HTRecipe {
    /**
     * 指定された[input]と[provider]から[hiiragi283.ragium.api.stack.ImmutableFluidStack]を返します。
     * @param input レシピの入力
     * @param provider レジストリのアクセス
     * @return 完成品となる[hiiragi283.ragium.api.stack.ImmutableFluidStack]
     */
    fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack?
}
