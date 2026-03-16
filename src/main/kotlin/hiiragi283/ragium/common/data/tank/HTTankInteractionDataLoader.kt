package hiiragi283.ragium.common.data.tank

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import hiiragi283.core.api.function.identity
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.tank.HTTankInteraction
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.neoforged.neoforge.common.conditions.ConditionalOps
import net.neoforged.neoforge.common.conditions.WithConditions
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @see net.neoforged.neoforge.common.loot.LootModifierManager
 */
class HTTankInteractionDataLoader : SimpleJsonResourceReloadListener(GSON, RagiumConst.TANK_INTERACTION) {
    companion object {
        @JvmStatic
        private val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

        @JvmStatic
        private val CONDITIONAL_CODEC: Codec<Optional<WithConditions<HTTankInteraction.Serializable>>> =
            ConditionalOps.createConditionalCodecWithConditions(HTTankInteraction.Serializable.CODEC)

        @JvmStatic
        var interactionMap: Map<ResourceLocation, HTTankInteraction.Serializable> = emptyMap()
            private set
    }

    override fun apply(map: Map<ResourceLocation, JsonElement>, resourceManager: ResourceManager, profiler: ProfilerFiller) {
        val dynamicOps: ConditionalOps<JsonElement> = makeConditionalOps()
        interactionMap = map
            .asSequence()
            .mapNotNull { (id: ResourceLocation, json: JsonElement) ->
                CONDITIONAL_CODEC
                    .parse(dynamicOps, json)
                    .resultOrPartial { RagiumAPI.LOGGER.warn("Could not decode HTTankInteraction with json id {} - error: {}", id, it) }
                    .flatMap(identity())
                    .map(WithConditions<HTTankInteraction.Serializable>::carrier)
                    .getOrNull()
                    ?.let { id to it }
            }.toMap()
    }
}
