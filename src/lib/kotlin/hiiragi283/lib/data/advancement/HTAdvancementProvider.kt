package hiiragi283.lib.data.advancement

import hiiragi283.lib.data.ConditionalExporter
import hiiragi283.lib.data.ExporterDataProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.advancements.Advancement
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.WithConditions

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
typealias HTAdvancementExporter = ConditionalExporter<Advancement>

/**
 * Hiiragi Seriesで使用される，進捗向けの[ExporterDataProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
abstract class HTAdvancementProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>, modId: String) : ExporterDataProvider<Advancement>(packOutput, future, Registries.ADVANCEMENT, modId, Advancement.CONDITIONAL_CODEC) {
    override fun createExporter(map: MutableMap<AdvancementKey, WithConditions<Advancement>>): HTAdvancementExporter = HTAdvancementExporter { id: AdvancementKey, advancement: Advancement, conditions: List<ICondition> -> check(map.put(id, WithConditions(conditions, advancement)) == null) { "Duplicate advancement $id" } }
}
