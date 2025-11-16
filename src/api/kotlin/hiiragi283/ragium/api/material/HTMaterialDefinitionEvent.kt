package hiiragi283.ragium.api.material

import net.neoforged.bus.api.Event

/**
 * 素材の属性を改変するイベント
 */
class HTMaterialDefinitionEvent(val factory: (HTMaterialKey) -> HTMaterialDefinition.Builder) : Event() {
    /**
     * 指定された[key]に紐づく属性を改変します。
     * @param key 対象となる素材のキー
     * @param builderAction 属性の改変を行うブロック
     */
    inline fun modify(key: HTMaterialKey, builderAction: HTMaterialDefinition.Builder.() -> Unit) {
        factory(key).apply(builderAction)
    }

    /**
     * 指定された[keys]に紐づく属性を改変します。
     * @param T [HTMaterialLike]を継承したクラス
     * @param keys 対象となる素材の一覧
     * @param builderAction 属性の改変を行うブロック
     */
    inline fun <T : HTMaterialLike> modify(keys: Iterable<T>, builderAction: HTMaterialDefinition.Builder.(T) -> Unit) {
        for (material: T in keys) {
            val key: HTMaterialKey = material.asMaterialKey()
            factory(key).builderAction(material)
        }
    }
}
