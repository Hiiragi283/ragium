package hiiragi283.ragium.data

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.HolderLookup
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
            val root = JsonObject()
            provider.listRegistries().forEach { lookup: HolderLookup.RegistryLookup<*> ->
                dumpRegistry(lookup)?.let { root.add(lookup.key().identifier().toString(), it) }
            }
            DataProvider.saveStable(
                cache,
                root,
                output.getOutputFolder(PackOutput.Target.REPORTS).resolve(RagiumAPI.MOD_ID, "registries.json")
            )
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
