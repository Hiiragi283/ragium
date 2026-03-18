package hiiragi283.ragium.common.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.Decoder
import hiiragi283.core.api.function.identity
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.neoforged.neoforge.common.conditions.ConditionalOps
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @see net.neoforged.neoforge.common.loot.LootModifierManager
 */
class HTJsonResourceReloadListener<T : Any>(directory: String, codec: Codec<T>, private val clazz: Class<T>) :
    SimpleJsonResourceReloadListener(GSON, directory) {
    companion object {
        @JvmStatic
        private val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

        @JvmStatic
        inline fun <reified T : Any> create(directory: String, codec: Codec<T>): HTJsonResourceReloadListener<T> =
            HTJsonResourceReloadListener(directory, codec, T::class.java)
    }

    private val decoder: Decoder<Optional<T>> = ConditionalOps.createConditionalCodec(codec)

    var resultMap: Map<ResourceLocation, T> = emptyMap()
        private set

    override fun apply(map: Map<ResourceLocation, JsonElement>, resourceManager: ResourceManager, profiler: ProfilerFiller) {
        val dynamicOps: ConditionalOps<JsonElement> = makeConditionalOps()
        resultMap = map
            .asSequence()
            .mapNotNull { (id: ResourceLocation, json: JsonElement) ->
                decoder
                    .parse(dynamicOps, json)
                    .resultOrPartial {
                        RagiumAPI.LOGGER.warn(
                            "Could not decode {} with json id {} - error: {}",
                            clazz::class.java.simpleName,
                            id,
                            it,
                        )
                    }.flatMap(identity())
                    .getOrNull()
                    ?.let { id to it }
            }.toMap()
    }
}
