package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation

/**
 * レシピの完成品を表すインターフェース
 * @param STACK 完成品のクラス
 * @see [HTItemResult]
 * @see [HTFluidResult]
 */
interface HTRecipeResult<STACK : Any> {
    /**
     * 完成品のID
     */
    val id: ResourceLocation

    /**
     * 指定された[provider]から完成品の[DataResult]を返します。
     * @param provider レジストリへのアクセス
     * @return 完成品がある場合は[DataResult.success], ない場合は[DataResult.error]
     */
    fun getStackResult(provider: HolderLookup.Provider?): DataResult<STACK>

    /**
     * 指定された[provider]から完成品を返します。
     * @param provider レジストリへのアクセス
     */
    fun getOrEmpty(provider: HolderLookup.Provider?): STACK

    /**
     * 完成品が存在するか判定します。
     * @return [getStackResult]が[DataResult.error]の場合は`true`
     */
    fun hasNoMatchingStack(): Boolean = getStackResult(null).isError
}
