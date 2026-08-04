package hiiragi283.lib.resource

import java.util.function.Supplier
import net.minecraft.resources.Identifier

/**
 * [ID][Identifier]を提供する[Supplier]の拡張インターフェースです。
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface SupplierWithId<out T> :
    Supplier<@UnsafeVariance T>,
    HTIdLike
