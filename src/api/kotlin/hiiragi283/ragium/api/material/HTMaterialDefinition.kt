package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.material.attribute.HTMaterialAttribute

/**
 * 素材の属性を保持するインターフェース
 * @see hiiragi283.ragium.api.block.type.HTBlockType
 */
interface HTMaterialDefinition {
    /**
     * 指定された[clazz]の属性が，このタイプに含まれているかどうか判定します。
     * @return 含まれている場合は`true`
     */
    operator fun contains(clazz: Class<out HTMaterialAttribute>): Boolean

    /**
     * 指定した[clazz]に対応する属性のインスタンスを返します。
     * @return 対応する属性がない場合は`null`
     */
    operator fun <T : HTMaterialAttribute> get(clazz: Class<T>): T?

    /**
     * このタイプが保持するすべての属性を返します。
     */
    fun getAllAttributes(): Collection<HTMaterialAttribute>

    /**
     * [HTMaterialDefinition]を作成するビルダーのインターフェース
     */
    interface Builder {
        /**
         * 指定した[clazz]に対応する属性のインスタンスを返します。
         * @return 対応する属性がない場合は`null`
         */
        operator fun <T : HTMaterialAttribute> get(clazz: Class<T>): T?

        /**
         * 指定された[attributes]を追加します。
         * @throws IllegalStateException すでに同じクラスの属性が登録されている場合
         */
        fun add(vararg attributes: HTMaterialAttribute)

        /**
         * 指定された[attributes]を追加します。
         *
         * [add]と異なり，同じクラスの属性が登録されていても上書きします。
         */
        fun set(vararg attributes: HTMaterialAttribute)

        /**
         * 指定された[classes]に対応する各属性を削除します。
         */
        fun remove(vararg classes: Class<out HTMaterialAttribute>)
    }

    /**
     * 空の[HTMaterialDefinition]の実装
     */
    data object Empty : HTMaterialDefinition {
        override fun contains(clazz: Class<out HTMaterialAttribute>): Boolean = false

        override fun <T : HTMaterialAttribute> get(clazz: Class<T>): T? = null

        override fun getAllAttributes(): Collection<HTMaterialAttribute> = listOf()
    }
}
