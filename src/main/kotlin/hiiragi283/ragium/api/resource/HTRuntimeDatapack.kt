package hiiragi283.ragium.api.resource

import com.google.gson.JsonElement
import com.mojang.logging.LogUtils
import com.mojang.serialization.JsonOps
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackSelectionConfig
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.metadata.MetadataSectionSerializer
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.PackCompatibility
import net.minecraft.server.packs.repository.PackSource
import net.minecraft.server.packs.resources.IoSupplier
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import org.slf4j.Logger
import java.io.InputStream
import java.util.*

object HTRuntimeDatapack : PackResources {
    private val resources: MutableMap<ResourceLocation, JsonElement> = mutableMapOf()
    private val namespaceCache: MutableSet<String> = mutableSetOf()

    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val PACK_INFO = PackLocationInfo(
        "ragium_runtime_datapack",
        Component.literal("Runtime Datapack"),
        PackSource.SERVER,
        Optional.empty(),
    )

    @JvmField
    val PACK = Pack(
        PACK_INFO,
        object : Pack.ResourcesSupplier {
            override fun openPrimary(location: PackLocationInfo): PackResources = this@HTRuntimeDatapack

            override fun openFull(location: PackLocationInfo, metadata: Pack.Metadata): PackResources = openPrimary(location)
        },
        Pack.Metadata(
            Component.literal(""),
            PackCompatibility.COMPATIBLE,
            FeatureFlagSet.of(),
            listOf(),
            false,
        ),
        PackSelectionConfig(true, Pack.Position.BOTTOM, false),
    )

    //    Helper    //

    @JvmStatic
    fun hasResource(id: ResourceLocation): Boolean = id in resources

    @JvmStatic
    fun addResource(id: ResourceLocation, json: JsonElement) {
        if (hasResource(id)) {
            LOGGER.warn("Duplicated resource: {}", id)
            return
        }
        LOGGER.info("Registered resource {}", id)
        resources[id] = json
        val namespace: String = id.namespace
        if (namespace !in namespaceCache) {
            namespaceCache.add(namespace)
        }
    }

    @JvmField
    val RUNTIME_OUTPUT: RecipeOutput = object : RecipeOutput {
        override fun accept(
            id: ResourceLocation,
            recipe: Recipe<*>,
            advancement: AdvancementHolder?,
            vararg conditions: ICondition,
        ) {
            if (conditions.isNotEmpty()) {
                LOGGER.warn("Runtime recipe registration not supports ICondition!")
            }
            // encode recipe
            Recipe.CODEC
                .encodeStart(JsonOps.INSTANCE, recipe)
                .ifSuccess { addResource(id.withPrefix("recipe/"), it) }
                .ifError { LOGGER.error(it.message()) }
            // encode advancement if present
            if (advancement == null) return
            Advancement.CODEC
                .encodeStart(JsonOps.INSTANCE, advancement.value)
                .ifSuccess { addResource(advancement.id.withPrefix("advancement/"), it) }
                .ifError { LOGGER.error(it.message()) }
        }

        @Suppress("removal", "DEPRECATION")
        override fun advancement(): Advancement.Builder = Advancement.Builder
            .recipeAdvancement()
            .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
    }

    @JvmStatic
    private fun getIoSupplier(id: ResourceLocation): IoSupplier<InputStream>? {
        val json: JsonElement = resources[id] ?: return null
        return createIoSupplier(json)
    }

    @JvmStatic
    private fun createIoSupplier(json: JsonElement): IoSupplier<InputStream> = IoSupplier { json.toString().toByteArray().inputStream() }

    //    PackResources    //

    override fun getRootResource(vararg elements: String): IoSupplier<InputStream>? = null

    override fun getResource(packType: PackType, location: ResourceLocation): IoSupplier<InputStream>? = when (packType) {
        PackType.CLIENT_RESOURCES -> null
        PackType.SERVER_DATA -> getIoSupplier(location)
    }

    override fun listResources(
        packType: PackType,
        namespace: String,
        path: String,
        resourceOutput: PackResources.ResourceOutput,
    ) {
        if (packType == PackType.CLIENT_RESOURCES) return
        LOGGER.info("Finding resource; $namespace, $path")
        resources.forEach { (id: ResourceLocation, json: JsonElement) ->
            if (id.namespace != namespace) return@forEach
            val fixedId: ResourceLocation = id.withPath { it.removePrefix("$path/") }
            if (fixedId == id) return@forEach
            LOGGER.info("Found resource; {}", fixedId)
            resourceOutput.accept(fixedId, createIoSupplier(json))
        }
    }

    override fun getNamespaces(type: PackType): Set<String> = when (type) {
        PackType.CLIENT_RESOURCES -> setOf()
        PackType.SERVER_DATA -> namespaceCache
    }

    override fun <T : Any> getMetadataSection(deserializer: MetadataSectionSerializer<T>): T? = null

    override fun location(): PackLocationInfo = PACK_INFO

    override fun close() {}
}
