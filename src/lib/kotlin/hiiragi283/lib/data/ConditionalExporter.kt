package hiiragi283.lib.data

import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun interface ConditionalExporter<T : Any> {
    /**
     * 受け取ったレシピを処理します。
     * @param id 受け取ったID
     * @param value 受け取った値
     * @param conditions 値を読み込む条件の一覧
     */
    fun accept(id: ResourceKey<T>, value: T, conditions: List<ICondition>)

    /**
     * 受け取ったレシピを処理します。
     * @param id 受け取ったID
     * @param value 受け取った値
     */
    fun accept(id: ResourceKey<T>, value: T) {
        accept(id, value, listOf())
    }
}
