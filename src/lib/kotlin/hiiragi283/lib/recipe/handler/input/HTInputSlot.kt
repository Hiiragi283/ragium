package hiiragi283.lib.recipe.handler.input

/**
 * レシピの入力となるスロットを表すクラスです。
 *
 * 参考 : [Mekanism - IInputHandler](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/recipes/inputs/IInputHandler.java)
 * @param INPUT 材料の種類を保持するインスタンスのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
interface HTInputSlot<INPUT : Any> : HTInputHandler<INPUT> {
    /**
     * 現在保持している入力を取得します。
     */
    fun getStoredInput(): INPUT
}
