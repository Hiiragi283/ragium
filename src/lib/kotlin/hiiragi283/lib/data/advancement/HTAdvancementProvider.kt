package hiiragi283.lib.data.advancement

import hiiragi283.lib.data.ExporterDataProvider
import net.minecraft.advancements.Advancement
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

/**
 * Hiiragi Seriesで使用される，進捗向けの[ExporterDataProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
abstract class HTAdvancementProvider(
    packOutput: PackOutput,
    future: CompletableFuture<HolderLookup.Provider>,
    modId: String
) : ExporterDataProvider<Advancement>(
    packOutput,
    future,
    Registries.ADVANCEMENT,
    modId,
    Advancement.CONDITIONAL_CODEC
)
