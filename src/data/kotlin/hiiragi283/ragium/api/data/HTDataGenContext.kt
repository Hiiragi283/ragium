package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

data class HTDataGenContext(
    private val generator: DataGenerator,
    val output: PackOutput,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
) {
    companion object {
        @JvmStatic
        fun withDataPack(event: GatherDataEvent, builderAction: RegistrySetBuilder.() -> Unit): HTDataGenContext {
            val generator: DataGenerator = event.generator
            val output: PackOutput = generator.packOutput
            val registries: CompletableFuture<HolderLookup.Provider> = generator
                .addProvider(
                    event.includeServer(),
                    DatapackBuiltinEntriesProvider(
                        output,
                        event.lookupProvider,
                        RegistrySetBuilder().apply(builderAction),
                        RagiumConst.BUILTIN_IDS,
                    ),
                ).registryProvider
            return HTDataGenContext(generator, output, registries, event.existingFileHelper)
        }
    }

    fun <DATA : DataProvider> addProvider(run: Boolean, provider: DATA): DATA = generator.addProvider(run, provider)

    fun <DATA : DataProvider> addProvider(run: Boolean, factory: DataProvider.Factory<DATA>): DATA =
        addProvider(run, factory.create(output))

    fun <DATA : DataProvider> addProvider(run: Boolean, factory: Factory<DATA>): DATA = addProvider(run, factory.create(this))

    fun interface Factory<DATA : DataProvider> {
        fun create(context: HTDataGenContext): DATA
    }
}
