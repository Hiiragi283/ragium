package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

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

    //    Extension    //

    /**
     * 指定された引数からアイテムの完成品を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @param result [FluidStack]の[HTRecipeResult]
     * @return [test]の戻り値が`false`，または[HTRecipeResult.getStackOrNull]が`null`の場合は[FluidStack.EMPTY]
     */
    fun getFluidResult(input: INPUT, registries: HolderLookup.Provider, result: HTFluidResult?): FluidStack = when {
        test(input) -> result?.getStackOrNull(registries)
        else -> null
    } ?: FluidStack.EMPTY

    /**
     * 指定された引数からアイテムの完成品を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @param result [Optional]の[HTFluidResult]
     * @return [test]の戻り値が`false`，または[HTRecipeResult.getStackOrNull]が`null`の場合は[FluidStack.EMPTY]
     */
    fun getFluidResult(input: INPUT, registries: HolderLookup.Provider, result: Optional<HTFluidResult>): FluidStack =
        getFluidResult(input, registries, result.getOrNull())
}
