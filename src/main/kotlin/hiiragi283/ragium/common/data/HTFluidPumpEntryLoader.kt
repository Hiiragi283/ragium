package hiiragi283.ragium.common.data

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import hiiragi283.ragium.common.item.HTFluidCubeItem
import hiiragi283.ragium.common.recipe.machine.HTFluidPumpEntry
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.registry.RegistryKey
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.world.biome.Biome

object HTFluidPumpEntryLoader : SimpleSynchronousResourceReloadListener {
    @JvmStatic
    val registry: Map<RegistryKey<Biome>, HTFluidCubeItem>
        get() = registry1.toSortedMap(compareBy { it.value })

    private val registry1: MutableMap<RegistryKey<Biome>, HTFluidCubeItem> = mutableMapOf()

    override fun reload(manager: ResourceManager) {
        registry1.clear()
        manager
            .findResources(HTFluidPumpEntry.KEY) { it.path.endsWith(".json") }
            .values
            .forEach { resource: Resource ->
                val json: JsonObject = resource.reader.use(JsonHelper::deserialize)
                HTFluidPumpEntry.CODEC
                    .parse(JsonOps.INSTANCE, json)
                    .result()
                    .filter { it.fluidCube != null }
                    .ifPresent { entry: HTFluidPumpEntry ->
                        registry1[entry.biomeKey] = entry.fluidCube!!
                    }
            }
    }

    override fun getFabricId(): Identifier = HTFluidPumpEntry.REGISTRY_KEY.value
}
