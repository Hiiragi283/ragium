package hiiragi283.ragium.api.block.type

import hiiragi283.ragium.api.block.attribute.HTBlockAttribute
import hiiragi283.ragium.api.block.attribute.HTShapeBlockAttribute
import hiiragi283.ragium.api.collection.AttributeMap
import hiiragi283.ragium.api.collection.MutableAttributeMap
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.BiFunction

/**
 * ブロックの属性を保持するクラス
 * @see mekanism.common.content.blocktype.BlockType
 */
open class HTBlockType(val description: HTTranslation, private val attributeMap: AttributeMap<HTBlockAttribute>) {
    companion object {
        @JvmStatic
        fun builder(): Builder.Impl<HTBlockType> = Builder.Impl(::HTBlockType)
    }

    /**
     * 指定された[clazz]の属性が，このタイプに含まれているかどうか判定します。
     * @return 含まれている場合は`true`
     */
    operator fun contains(clazz: Class<out HTBlockAttribute>): Boolean = clazz in attributeMap

    /**
     * 指定した[clazz]に対応する属性のインスタンスを返します。
     * @return 対応する属性がない場合は`null`
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T : HTBlockAttribute> get(clazz: Class<T>): T? = attributeMap[clazz] as? T

    inline fun <reified T : HTBlockAttribute> get(): T? = get(T::class.java)

    /**
     * このタイプが保持するすべての属性を返します。
     */
    fun getAll(): Collection<HTBlockAttribute> = attributeMap.values

    //    Builder    //

    /**
     * [HTBlockType]を作成するビルダークラス
     * @param TYPE 出力されるタイプのクラス
     * @param BUILDER ビルダーのクラス
     * @param factory [AttributeMap]から[TYPE]に変換するブロック
     */
    abstract class Builder<TYPE : HTBlockType, BUILDER : Builder<TYPE, BUILDER>>(
        protected val factory: BiFunction<HTTranslation, AttributeMap<HTBlockAttribute>, TYPE>,
    ) {
        private var hasBuilt = false
        private val attributeMap: MutableAttributeMap<HTBlockAttribute> = hashMapOf()

        protected fun checkHasBuilt() {
            check(!hasBuilt) { "Builder has already built" }
        }

        @Suppress("UNCHECKED_CAST")
        protected fun self(): BUILDER = this as BUILDER

        /**
         * 指定された[attributes]を追加します。
         * @throws IllegalStateException すでに同じクラスの属性が登録されている場合
         */
        fun add(vararg attributes: HTBlockAttribute): BUILDER {
            checkHasBuilt()
            for (attribute: HTBlockAttribute in attributes) {
                val clazz: Class<out HTBlockAttribute> = attribute::class.java
                check(attributeMap.put(clazz, attribute) == null) {
                    "Block attribute ${clazz.simpleName} has already exist"
                }
            }
            return self()
        }

        fun addShape(shape: VoxelShape): BUILDER = add(HTShapeBlockAttribute(shape))

        /**
         * 指定された[attributes]を追加します。
         *
         * [add]と異なり，同じクラスの属性が登録されていても上書きします。
         */
        fun set(vararg attributes: HTBlockAttribute): BUILDER {
            checkHasBuilt()
            for (attribute: HTBlockAttribute in attributes) {
                attributeMap[attribute::class.java] = attribute
            }
            return self()
        }

        /**
         * 指定された[classes]に対応する各属性を削除します。
         */
        fun remove(vararg classes: Class<out HTBlockAttribute>): BUILDER {
            checkHasBuilt()
            for (clazz: Class<out HTBlockAttribute> in classes) {
                attributeMap.remove(clazz)
            }
            return self()
        }

        /**
         * このビルダーを無効にし，タイプを作成します。
         */
        fun build(description: HTTranslation): TYPE {
            this.hasBuilt = true
            return factory.apply(description, attributeMap)
        }

        @Deprecated("Use `build(HTTranslation)` instead")
        fun build(): TYPE = build(RagiumTranslation.EMPTY_ENTRY)

        //    Impl    //

        /**
         * [Builder]の簡易的な実装
         */
        class Impl<TYPE : HTBlockType>(factory: BiFunction<HTTranslation, AttributeMap<HTBlockAttribute>, TYPE>) :
            Builder<TYPE, Impl<TYPE>>(factory)
    }
}
