package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger

internal object HTMachineRegistryImpl : HTMachineRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Init    //

    override lateinit var keys: Set<HTMachineKey>
    private lateinit var typeMap: Map<HTMachineKey, HTMachineType>
    private lateinit var propertyMap: Map<HTMachineKey, HTPropertyHolder>
    override lateinit var entryMap: Map<HTMachineKey, HTMachineRegistry.Entry>

    fun initRegistry() {
        registerMachines()
        modifyProperties()

        entryMap = typeMap
            .mapValues { (key: HTMachineKey, type: HTMachineType) ->
                val property: HTPropertyHolder = propertyMap[key] ?: HTPropertyHolder.Empty
                val holder: DeferredBlock<out Block> = RagiumBlocks.REGISTER.registerBlock(
                    key.name,
                    ::HTMachineBlock,
                    blockProperty(Blocks.SMOOTH_STONE).noOcclusion(),
                )
                Entry(type, holder, property)
            }.onEach { (_, entry: Entry) ->
                RagiumItems.REGISTER.registerSimpleBlockItem(entry.holder)
            }
        LOGGER.info("Loaded machine registry!")
    }

    private fun registerMachines() {
        LOGGER.info("Invoke material events...")
        val typeCache: MutableMap<HTMachineKey, HTMachineType> = mutableMapOf()

        DefaultMachinePlugin.registerMachine { key: HTMachineKey, type: HTMachineType ->
            check(typeCache.put(key, type) == null) { "Duplicated material registration: ${key.name}" }
        }

        this.typeMap = typeCache
        this.keys = this.typeMap.keys
        LOGGER.info("Registered new machines!")
    }

    private fun modifyProperties() {
        val propertyCache: MutableMap<HTMachineKey, HTPropertyHolderBuilder> = mutableMapOf()
        DefaultMachinePlugin.setupMachineProperties { propertyCache.computeIfAbsent(it) { HTPropertyHolderBuilder() } }
        this.propertyMap = propertyCache.mapValues { (_, builder: HTPropertyHolderBuilder) -> builder.build() }
        LOGGER.info("Modified machine properties!")
    }

    //    HTMachineRegistry    //

    override fun getBlock(key: HTMachineKey): HTBlockContent? = entryMap[key]

    override fun getEntryData(key: HTMachineKey): DataResult<HTMachineRegistry.Entry> =
        entryMap[key].toDataResult { "Unknown machine key: $key" }

    //    Entry    //

    class Entry(override val type: HTMachineType, override val holder: DeferredBlock<out Block>, property: HTPropertyHolder) :
        HTMachineRegistry.Entry,
        HTPropertyHolder by property
}
