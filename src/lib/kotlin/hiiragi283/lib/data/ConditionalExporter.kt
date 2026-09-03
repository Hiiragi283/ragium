package hiiragi283.lib.data

import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * [ICondition]とともに値を登録する処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun interface ConditionalExporter<T : Any> {
    /**
     * 値を登録します。
     * @param id 受け取ったID
     * @param value 受け取った値
     * @param conditions 値を読み込む条件の一覧
     */
    fun accept(id: ResourceKey<T>, value: T, conditions: List<ICondition>)

    /**
     * 値を登録します。
     * @param id 受け取ったID
     * @param value 受け取った値
     */
    fun accept(id: ResourceKey<T>, value: T) {
        accept(id, value, listOf())
    }
}
