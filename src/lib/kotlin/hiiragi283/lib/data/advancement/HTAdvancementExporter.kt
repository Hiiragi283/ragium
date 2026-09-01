package hiiragi283.lib.data.advancement

import net.minecraft.advancements.Advancement
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun interface HTAdvancementExporter {
    /**
     * 受け取った進捗を処理します。
     * @param id 受け取った進捗のID
     * @param advancement 受け取った進捗の値
     * @param conditions 進捗を読み込む条件の一覧
     */
    fun accept(id: AdvancementKey, advancement: Advancement, conditions: List<ICondition>)

    /**
     * 受け取ったレシピを処理します。
     * @param id 受け取ったレシピのID
     * @param advancement 受け取った進捗の値
     */
    fun accept(id: AdvancementKey, advancement: Advancement) {
        accept(id, advancement, listOf())
    }
}
