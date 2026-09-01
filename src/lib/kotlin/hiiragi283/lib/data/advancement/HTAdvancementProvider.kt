package hiiragi283.lib.data.advancement

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import hiiragi283.lib.data.RegistryDataProvider
import java.util.Optional
import java.util.concurrent.CompletableFuture
import net.minecraft.advancements.Advancement
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.RegistryOps
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.WithConditions

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
abstract class HTAdvancementProvider(packOutput: PackOutput, private val future: CompletableFuture<HolderLookup.Provider>, protected val modId: String) :
    RegistryDataProvider(),
    DataProvider {
    private val pathProvider: PackOutput.PathProvider = packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT)

    /**
     * 進捗の出力先
     *
     * [buildAdvancements]の前に初期化されます。
     */
    protected lateinit var exporter: HTAdvancementExporter
        private set

    /**
     * レジストリへのアクセス
     *
     * [buildAdvancements]の前に初期化されます。
     */
    override lateinit var registries: HolderLookup.Provider

    final override fun run(output: CachedOutput): CompletableFuture<*> = future.thenCompose { registries: HolderLookup.Provider ->
        val advancements: MutableMap<AdvancementKey, WithConditions<Advancement>> = hashMapOf()
        this.registries = registries
        this.exporter = HTAdvancementExporter { id: AdvancementKey, advancement: Advancement, conditions: List<ICondition> -> check(advancements.put(id, WithConditions(conditions, advancement)) == null) { "Duplicate advancement $id" } }
        buildAdvancements()

        val dynamicOps: RegistryOps<JsonElement> = registries.createSerializationContext(JsonOps.INSTANCE)
        DataProvider.saveAll(
            output,
            { conditions: WithConditions<Advancement> -> Advancement.CONDITIONAL_CODEC.encodeStart(dynamicOps, Optional.of(conditions)).orThrow },
            pathProvider::json,
            advancements,
        )
    }

    /**
     * 進捗を生成します。
     */
    protected abstract fun buildAdvancements()
}
