package hiiragi283.ragium.data

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import java.util.concurrent.CompletableFuture

/**
 * @see net.minecraft.data.info.RegistryDumpReport
 */
class RagiumRegistryDumpReport(val output: PackOutput, val future: CompletableFuture<HolderLookup.Provider>) :
    DataProvider {
    override fun run(cache: CachedOutput): CompletableFuture<*> =
        this.future.thenCompose { provider: HolderLookup.Provider ->
            provider.listRegistries()
                .toList()
                .mapNotNull { lookup ->
                    dumpRegistry(lookup)?.let {
                        DataProvider.saveStable(
                            cache,
                            it,
                            output
                                .createPathProvider(PackOutput.Target.REPORTS, Registries.elementsDirPath(lookup.key()))
                                .json(RagiumAPI.id("entries"))
                        )
                    }
                }.toTypedArray().let { CompletableFuture.allOf(*it) }
        }

    private fun dumpRegistry(lookup: HolderLookup.RegistryLookup<*>): JsonElement? {
        val entries = JsonArray()
        lookup.listElementIds()
            .map(ResourceKey<*>::identifier)
            .filter { id: Identifier -> id.namespace == RagiumAPI.MOD_ID }
            .map(Identifier::getPath)
            .sorted()
            .forEach(entries::add)
        return when {
            entries.isEmpty -> null
            else -> entries
        }
    }

    override fun getName(): String = "Registry Dump"
}
