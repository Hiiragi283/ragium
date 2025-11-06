package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.stack.ImmutableStack
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation

/**
 * レシピの完成品を表すインターフェース
 * @param STACK 完成品のクラス
 * @see HTItemResult
 * @see HTFluidResult
 */
interface HTRecipeResult<STACK : ImmutableStack<*, STACK>> {
    /**
     * 完成品のID
     */
    val id: ResourceLocation

    /**
     * 指定された[provider]から完成品の[Result]を返します。
     * @param provider レジストリへのアクセス
     * @return 完成品がある場合は[Result.success], ない場合は[Result.failure]
     */
    fun getStackResult(provider: HolderLookup.Provider?): Result<STACK>

    /**
     * 指定された[provider]から完成品を返します。
     * @param provider レジストリへのアクセス
     * @return 完成品がない場合は`null`
     */
    fun getStackOrNull(provider: HolderLookup.Provider?): STACK? = getStackResult(provider).getOrNull()

    /**
     * 完成品が存在するか判定します。
     * @return [getStackResult]が[Result.failure]の場合は`true`
     */
    fun hasNoMatchingStack(): Boolean = getStackResult(null).isFailure
}
