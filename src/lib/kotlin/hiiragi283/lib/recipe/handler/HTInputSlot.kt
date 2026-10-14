package hiiragi283.lib.recipe.handler

import net.minecraft.core.TypedInstance

/**
 * レシピの入力となるスロットを表すクラスです。
 *
 * 参考 : [Mekanism - IInputHandler](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/recipes/inputs/IInputHandler.java)
 * @param TYPE 材料の種類のクラス
 * @param INSTANCE 材料の種類を保持するインスタンスのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
interface HTInputSlot<TYPE : Any, INSTANCE : TypedInstance<TYPE>> : HTInputHandler<TYPE, INSTANCE> {
    /**
     * 現在保持している入力を取得します。
     */
    fun getStoredInput(): INSTANCE

    /**
     * 指定した入力が空かどうか判定します。
     * @return 空の場合は`true`
     */
    fun isEmpty(instance: INSTANCE): Boolean
}
