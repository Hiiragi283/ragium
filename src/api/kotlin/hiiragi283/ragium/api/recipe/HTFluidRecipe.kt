package hiiragi283.ragium.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 液体を完成品にとるレシピのインターフェース
 * @param INPUT レシピの入力となるクラス
 */
interface HTFluidRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    /**
     * 指定された[input]と[registries]から[FluidStack]を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @return 完成品となる[FluidStack]
     */
    fun assembleFluid(input: INPUT, registries: HolderLookup.Provider): FluidStack
}
