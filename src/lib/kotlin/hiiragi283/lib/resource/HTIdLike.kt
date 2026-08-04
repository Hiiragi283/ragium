package hiiragi283.lib.resource

import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.HTHasTranslationKey
import net.minecraft.resources.Identifier

/**
 * [ID][Identifier]を提供するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 * @see SupplierWithId
 */
fun interface HTIdLike {
    /**
     * 保持している[ID][Identifier]を返します。
     */
    fun getId(): Identifier

    /**
     * 保持している[ID][Identifier]の[名前空間][Identifier.getNamespace]を返します。
     */
    val namespace: String get() = getId().namespace

    /**
     * 保持している[ID][Identifier]の[パス][Identifier.getPath]を返します。
     */
    val path: String get() = getId().path

    interface Translatable :
        HTIdLike,
        HTHasTranslationKey,
        HTHasText
}
