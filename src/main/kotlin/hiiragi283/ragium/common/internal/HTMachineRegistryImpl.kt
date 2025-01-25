package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.event.HTModifyPropertyEvent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.fml.ModLoader
import net.neoforged.neoforge.registries.DeferredBlock
import org.slf4j.Logger

internal object HTMachineRegistryImpl : HTMachineRegistry {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Init    //

    override lateinit var blockMap: Map<HTMachineKey, HTBlockContent>
    private lateinit var propertyMap: Map<HTMachineKey, HTPropertyHolder>

    fun registerBlocks() {
        blockMap = HTMachineKey.allKeys.associateWith { key: HTMachineKey ->
            val holder: DeferredBlock<out Block> = RagiumBlocks.REGISTER.registerBlock(
                key.name,
                ::HTMachineBlock,
                blockProperty(Blocks.SMOOTH_STONE).noOcclusion(),
            )
            RagiumItems.REGISTER.registerSimpleBlockItem(holder)
            HTBlockContent.of(holder)
        }
        LOGGER.info("Registered machine blocks!")
    }

    fun modifyProperties() {
        val propertyCache: MutableMap<HTMachineKey, HTPropertyHolderBuilder> = mutableMapOf()
        ModLoader.postEvent(HTModifyPropertyEvent.Machine { propertyCache.computeIfAbsent(it) { HTPropertyHolderBuilder() } })
        this.propertyMap = propertyCache.mapValues { (_, builder: HTPropertyHolderBuilder) -> builder.build() }
        LOGGER.info("Modified machine properties!")
    }

    //    HTMachineRegistry    //

    override fun getProperty(key: HTMachineKey): HTPropertyHolder = propertyMap[key] ?: HTPropertyHolder.Empty
}
