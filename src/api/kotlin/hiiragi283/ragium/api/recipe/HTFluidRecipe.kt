package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * 液体を完成品にとるレシピのインターフェース
 * @param INPUT レシピの入力となるクラス
 */
interface HTFluidRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    /**
     * 指定された[input]と[registries]から[ImmutableFluidStack]を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @return 完成品となる[ImmutableFluidStack]
     */
    fun assembleFluid(input: INPUT, registries: HolderLookup.Provider): ImmutableFluidStack?

    //    Extension    //

    /**
     * 指定された引数からアイテムの完成品を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @param result [ImmutableFluidStack]の[HTRecipeResult]
     * @return [test]の戻り値が`false`，または[HTRecipeResult.getStackOrNull]が`null`の場合は`null`
     */
    fun getFluidResult(input: INPUT, registries: HolderLookup.Provider?, result: HTFluidResult?): ImmutableFluidStack? = when {
        test(input) -> result?.getStackOrNull(registries)
        else -> null
    }

    /**
     * 指定された引数からアイテムの完成品を返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @param result [Optional]の[HTFluidResult]
     * @return [test]の戻り値が`false`，または[HTRecipeResult.getStackOrNull]が`null`の場合は`null`
     */
    fun getFluidResult(input: INPUT, registries: HolderLookup.Provider?, result: Optional<HTFluidResult>): ImmutableFluidStack? =
        getFluidResult(input, registries, result.getOrNull())
}
