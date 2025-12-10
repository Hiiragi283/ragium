package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture
import java.util.function.BooleanSupplier

/**
 * データ生成でよく使うインスタンスを束ねたデータクラス
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTRootDataGenerator private constructor(
    private val generator: DataGenerator,
    private val doRun: BooleanSupplier,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
) : HTDataGenerator {
    companion object {
        @JvmStatic
        fun withDataPack(
            event: GatherDataEvent,
            builderAction: RegistrySetBuilder.() -> Unit,
        ): Pair<HTRootDataGenerator, HTRootDataGenerator> {
            val generator: DataGenerator = event.generator
            val registries: CompletableFuture<HolderLookup.Provider> = generator
                .addProvider(event.includeServer()) { output: PackOutput ->
                    DatapackBuiltinEntriesProvider(
                        output,
                        event.lookupProvider,
                        RegistrySetBuilder().apply(builderAction),
                        RagiumConst.BUILTIN_IDS,
                    )
                }.registryProvider
            val fileHelper: ExistingFileHelper = event.existingFileHelper
            return Pair(
                HTRootDataGenerator(generator, event::includeServer, registries, fileHelper),
                HTRootDataGenerator(generator, event::includeClient, registries, fileHelper),
            )
        }
    }

    fun createDataPackGenerator(id: ResourceLocation): HTDataPackGenerator = HTDataPackGenerator(
        generator.getBuiltinDatapack(doRun.asBoolean, id.namespace, id.path),
        registries,
        fileHelper,
    )

    override fun <DATA : DataProvider> addProvider(factory: DataProvider.Factory<DATA>): DATA =
        generator.addProvider(doRun.asBoolean, factory)

    override fun <DATA : DataProvider> addProvider(factory: HTDataGenerator.Factory<DATA>): DATA =
        addProvider { output: PackOutput -> factory.create(HTDataGenContext(output, registries, fileHelper)) }
}
