package hiiragi283.lib.recipe.handler.input

import net.minecraft.core.TypedInstance

/**
 * レシピの入力となるスロットを表すクラスです。
 *
 * 参考 : [Mekanism - IInputHandler](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/recipes/inputs/IInputHandler.java)
 * @param INSTANCE 材料の種類を保持するインスタンスのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
interface HTInputSlot<INSTANCE : TypedInstance<*>> : HTInputHandler<INSTANCE> {
    /**
     * 現在保持している入力を取得します。
     */
    fun getStoredInput(): INSTANCE
}
