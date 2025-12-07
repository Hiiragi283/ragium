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

/**
 * データ生成でよく使うインスタンスを束ねたデータクラス
 */
@JvmRecord
data class HTRootDataGenerator(
    private val generator: DataGenerator,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
) : HTDataGenerator {
    companion object {
        @JvmStatic
        fun withDataPack(event: GatherDataEvent, builderAction: RegistrySetBuilder.() -> Unit): HTRootDataGenerator {
            val generator: DataGenerator = event.generator
            val registries: CompletableFuture<HolderLookup.Provider> = generator
                .addProvider(
                    event.includeServer(),
                    { output: PackOutput ->
                        DatapackBuiltinEntriesProvider(
                            output,
                            event.lookupProvider,
                            RegistrySetBuilder().apply(builderAction),
                            RagiumConst.BUILTIN_IDS,
                        )
                    },
                ).registryProvider
            return HTRootDataGenerator(generator, registries, event.existingFileHelper)
        }
    }

    fun createDataPackGenerator(toRun: Boolean, id: ResourceLocation): HTDataPackGenerator = HTDataPackGenerator(
        generator.getBuiltinDatapack(toRun, id.namespace, id.path),
        registries,
        fileHelper,
    )

    override fun <DATA : DataProvider> addProvider(run: Boolean, factory: DataProvider.Factory<DATA>): DATA =
        generator.addProvider(run, factory)

    override fun <DATA : DataProvider> addProvider(run: Boolean, factory: HTDataGenerator.Factory<DATA>): DATA =
        addProvider(run) { output: PackOutput -> factory.create(HTDataGenContext(output, registries, fileHelper)) }
}
