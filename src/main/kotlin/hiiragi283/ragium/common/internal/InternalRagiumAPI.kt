package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlugin
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger
import java.util.function.Function

internal object InternalRagiumAPI : RagiumAPI {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    override lateinit var plugins: List<RagiumPlugin>
    override lateinit var machineRegistry: HTMachineRegistry
    override val materialRegistry: HTMaterialRegistry = HTMaterialRegistryImpl

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
}
