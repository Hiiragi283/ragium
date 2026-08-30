package hiiragi283.lib.property

import hiiragi283.lib.resource.HTIdLike
import net.minecraft.resources.Identifier

/**
 * [HTPropertyGetter]でキーとして使用されるクラスです。
 * @param T 対応する値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
sealed class HTPropertyKey<T : Any>(private val id: Identifier) : HTIdLike {
    /**
     * デフォルト値を取得します。
     * @return デフォルト値がない場合は`null`
     */
    abstract fun getDefaultOrNull(): T?

    final override fun getId(): Identifier = id

    /**
     * デフォルト値を持たない[HTPropertyKey]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    class Simple<T : Any>(id: Identifier) : HTPropertyKey<T>(id) {
        override fun getDefaultOrNull(): T? = null
    }

    /**
     * デフォルト値を持つ[HTPropertyKey]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.2
     */
    class Defaulted<T : Any>(id: Identifier, private val defaultValue: () -> T) : HTPropertyKey<T>(id) {
        constructor(id: Identifier, defaultValue: T) : this(id, { defaultValue })

        override fun getDefaultOrNull(): T = defaultValue()
    }
}
