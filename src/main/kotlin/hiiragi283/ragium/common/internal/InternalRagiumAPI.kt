package hiiragi283.ragium.common.internal

import com.google.common.collect.HashBasedTable
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.asPairMap
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.toTable
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.api.util.collection.HTWrappedTable
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger
import java.util.function.Function
import java.util.function.Supplier

internal object InternalRagiumAPI : RagiumAPI {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    override lateinit var plugins: List<RagiumPlugin>
    override lateinit var machineRegistry: HTMachineRegistry
    override lateinit var materialRegistry: HTMaterialRegistry

    //    Init    //

    @JvmStatic
    fun collectPlugins() {
        LOGGER.info("Collecting RagiumPlugin ...")
        this.plugins =
            buildList<RagiumPlugin> {
                ModList.get().forEachModContainer { modId: String, container: ModContainer ->
                    container
                        .getCustomExtension(RagiumPlugin.Provider::class.java)
                        .map(RagiumPlugin.Provider::getPlugins)
                        .ifPresent(this::addAll)
                }
            }.sortedWith(compareBy(RagiumPlugin::priority).thenBy { it::class.java.canonicalName })
                .filter(RagiumPlugin::shouldLoad)
        LOGGER.info("RagiumPlugin collected!")

        LOGGER.info("=== Loaded Ragium Plugins ===")
        plugins.forEach { plugin: RagiumPlugin ->
            LOGGER.info("- Priority : ${plugin.priority} ... ${plugin.javaClass.canonicalName}")
        }
        LOGGER.info("=============================")
    }

    @JvmStatic
    fun registerMachines() {
        val keyCache: MutableMap<HTMachineKey, HTMachineType> = mutableMapOf()

        // collect keys from plugins
        fun addMachine(key: HTMachineKey, type: HTMachineType) {
            check(keyCache.put(key, type) == null) { "Machine; ${key.name} is already registered!" }
        }
        plugins.forEach { it.registerMachine(::addMachine) }

        // sort keys based on its type and id
        val sortedKeys: Map<HTMachineKey, HTMachineType> =
            keyCache
                .toList()
                .sortedWith(
                    compareBy(Pair<HTMachineKey, HTMachineType>::second)
                        .thenBy(Pair<HTMachineKey, HTMachineType>::first),
                ).toMap()

        // register properties
        val propertyCache: MutableMap<HTMachineKey, HTPropertyHolderBuilder> = mutableMapOf()
        val helper: Function<HTMachineKey, HTPropertyHolderBuilder> =
            Function { key: HTMachineKey -> propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() } }
        plugins.forEach { plugin: RagiumPlugin -> plugin.setupMachineProperties(helper) }

        // register blocks
        val blockMap: Map<HTMachineKey, DeferredBlock<HTMachineBlock>> = sortedKeys.keys
            .associateWith { key: HTMachineKey ->
                RagiumBlocks.REGISTER.registerBlock(
                    key.name,
                    { properties: BlockBehaviour.Properties -> HTMachineBlock(key, properties) },
                    blockProperty(Blocks.SMOOTH_STONE).noOcclusion(),
                )
            }.onEach { (_: HTMachineKey, holder: DeferredBlock<HTMachineBlock>) ->
                RagiumItems.REGISTER.registerSimpleBlockItem(holder)
            }

        // complete
        machineRegistry =
            HTMachineRegistry(
                sortedKeys,
                blockMap,
                propertyCache.mapValues { (_, builder: HTPropertyHolderBuilder) -> builder.build() },
            )
        LOGGER.info("Registered machine types and properties!")
    }

    @JvmStatic
    fun registerMaterials() {
        val keyCache: MutableMap<HTMaterialKey, HTMaterialType> = mutableMapOf()
        val altNameCache: MutableMap<String, HTMaterialKey> = mutableMapOf()

        // collect keys from plugins
        fun addMaterial(key: HTMaterialKey, type: HTMaterialType) {
            check(keyCache.put(key, type) == null) { "Material; ${key.name} is already registered!" }
        }

        fun addAltName(parent: HTMaterialKey, child: String) {
            check(parent.name != child) { "Could not register same alternative name!" }
            check(altNameCache.put(child, parent) == null) { "Alternative Name: $child already has redirect material!" }
        }
        plugins.forEach {
            it.registerMaterial(RagiumPlugin.MaterialHelper(::addMaterial, ::addAltName))
        }

        // sort keys based on its type and id
        val sortedKeys: Map<HTMaterialKey, HTMaterialType> =
            keyCache
                .toList()
                .sortedWith(
                    compareBy(Pair<HTMaterialKey, HTMaterialType>::second)
                        .thenBy(Pair<HTMaterialKey, HTMaterialType>::first),
                ).toMap()

        // register properties
        val propertyCache: MutableMap<HTMaterialKey, HTPropertyHolderBuilder> = mutableMapOf()
        val helper: Function<HTMaterialKey, HTPropertyHolderBuilder> =
            Function { key: HTMaterialKey -> propertyCache.computeIfAbsent(key) { HTPropertyHolderBuilder() } }
        plugins.forEach { plugin: RagiumPlugin -> plugin.setupMaterialProperties(helper) }
        // bind items
        val itemCache =
            HTWrappedTable.Mutable<HTTagPrefix, HTMaterialKey, MutableSet<Supplier<out ItemLike>>>(HashBasedTable.create())
        plugins.forEach {
            it.bindMaterialToItem { prefix: HTTagPrefix, key: HTMaterialKey, supplier: Supplier<out ItemLike> ->
                itemCache
                    .computeIfAbsent(prefix, key) { _: HTTagPrefix, _: HTMaterialKey -> mutableSetOf() }
                    .add(supplier)
            }
        }
        val itemTable: HTTable<HTTagPrefix, HTMaterialKey, MutableSet<Supplier<out ItemLike>>> =
            itemCache
                .asPairMap()
                .toSortedMap(
                    compareBy(Pair<HTTagPrefix, HTMaterialKey>::second)
                        .thenBy(Pair<HTTagPrefix, HTMaterialKey>::first),
                ).filter { (pair: Pair<HTTagPrefix, HTMaterialKey>, _) ->
                    val (_: HTTagPrefix, key: HTMaterialKey) = pair
                    val fixedKey: HTMaterialKey = altNameCache.getOrDefault(key.name, key)
                    if (fixedKey !in keyCache.keys) {
                        LOGGER.warn("Could not bind item with unregistered material: $fixedKey!")
                        false
                    } else {
                        true
                    }
                }.toTable()

        // complete
        materialRegistry =
            HTMaterialRegistry(
                sortedKeys,
                itemTable,
                propertyCache.mapValues { (_, builder: HTPropertyHolderBuilder) -> builder.build() },
            )
        LOGGER.info("Registered material types and properties!")
    }
}
