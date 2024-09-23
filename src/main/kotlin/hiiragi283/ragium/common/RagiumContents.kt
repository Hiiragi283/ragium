package hiiragi283.ragium.common

import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMaterials
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.HTTranslationFormatter
import hiiragi283.ragium.common.util.blockSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RagiumContents {
    //    Ores    //

    @JvmField
    val RAGINITE_ORE: Block =
        registerCopy("raginite_ore", Blocks.IRON_ORE)

    @JvmField
    val DEEPSLATE_RAGINITE_ORE: Block =
        registerCopy("deepslate_raginite_ore", Blocks.DEEPSLATE_IRON_ORE)

    //    Machines    //

    @JvmField
    val CREATIVE_SOURCE: Block =
        registerWithBE("creative_source", RagiumBlockEntityTypes.CREATIVE_SOURCE, Blocks.COMMAND_BLOCK)

    @JvmField
    val MANUAL_GRINDER: Block =
        register("manual_grinder", HTManualGrinderBlock)

    @JvmField
    val BURNING_BOX: Block =
        registerHorizontalWithBE("burning_box", RagiumBlockEntityTypes.BURNING_BOX, Blocks.BRICKS)

    @JvmField
    val WATER_GENERATOR: Block =
        registerHorizontalWithBE("water_generator", RagiumBlockEntityTypes.WATER_GENERATOR)

    @JvmField
    val WIND_GENERATOR: Block =
        registerHorizontalWithBE("wind_generator", RagiumBlockEntityTypes.WIND_GENERATOR)

    @JvmField
    val SHAFT: Block =
        register("shaft", HTShaftBlock)

    @JvmField
    val GEAR_BOX: Block =
        register("gear_box", HTGearBoxBlock)

    @JvmField
    val BLAZING_BOX: Block =
        registerHorizontalWithBE("blazing_box", RagiumBlockEntityTypes.BLAZING_BOX, Blocks.POLISHED_BLACKSTONE_BRICKS)

    @JvmField
    val ALCHEMICAL_INFUSER: Block =
        register("alchemical_infuser", HTAlchemicalInfuserBlock)

    @JvmField
    val ITEM_DISPLAY: Block =
        register("item_display", HTItemDisplayBlock)

    @JvmField
    val INFESTING: Block =
        register("infesting", HTInfectingBlock)

    @JvmStatic
    fun init() {
        initStorageBlocks()
        initHulls()
        initMachines()
        initElements()
    }

    //    Register    //

    @JvmStatic
    private fun <T : Block> register(name: String, block: T): T = Registry.register(Registries.BLOCK, Ragium.id(name), block)

    @JvmStatic
    private fun registerSimple(name: String, settings: AbstractBlock.Settings = blockSettings()): Block = register(name, Block(settings))

    @JvmStatic
    private fun registerCopy(name: String, parent: Block): Block = registerSimple(name, blockSettings(parent))

    @JvmStatic
    private fun registerWithBE(name: String, type: BlockEntityType<*>, parent: Block): Block =
        registerWithBE(name, type, blockSettings(parent))

    @JvmStatic
    private fun registerWithBE(name: String, type: BlockEntityType<*>, settings: AbstractBlock.Settings = blockSettings()): Block =
        register(name, HTBlockWithEntity.build(type, settings))

    @JvmStatic
    private fun registerHorizontalWithBE(name: String, type: BlockEntityType<*>, parent: Block): Block =
        registerHorizontalWithBE(name, type, blockSettings(parent))

    @JvmStatic
    private fun registerHorizontalWithBE(
        name: String,
        type: BlockEntityType<*>,
        settings: AbstractBlock.Settings = blockSettings(),
    ): Block = register(name, HTBlockWithEntity.buildHorizontal(type, settings))

    //    Storage Blocks    //

    enum class StorageBlocks(val material: RagiumMaterials) :
        ItemConvertible,
        HTTranslationFormatter {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        val block = Block(blockSettings(Blocks.IRON_BLOCK))

        override fun asItem(): Item = block.asItem()

        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
    }

    @JvmStatic
    fun initStorageBlocks() {
        StorageBlocks.entries.forEach { block: StorageBlocks ->
            register("${block.name.lowercase()}_block", block.block)
        }
    }

    //    Hulls    //

    enum class Hulls(val material: RagiumMaterials) :
        ItemConvertible,
        HTTranslationFormatter {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        val block = Block(blockSettings(material.tier.base))

        override fun asItem(): Item = block.asItem()

        override val enPattern: String = "%s Hull"
        override val jaPattern: String = "%s筐体"
    }

    @JvmStatic
    fun initHulls() {
        Hulls.entries.forEach { hull: Hulls ->
            register("${hull.name.lowercase()}_hull", hull.block)
        }
    }

    //    Machines    //

    @JvmStatic
    fun initMachines() {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            val id: Identifier = type.id
            val block: Block = type.block
            // Machine Block
            register(id.path, block)
            // BlockEntityType
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id, type.blockEntityType)
            type.blockEntityType.addSupportedBlock(block)
            // RecipeType
            Registry.register(Registries.RECIPE_TYPE, id, type)
        }
    }

    //    Elements    //

    @JvmStatic
    private fun initElements() {
        RagiElement.entries.forEach { element: RagiElement ->
            // Budding Block
            register("budding_${element.asString()}", element.buddingBlock)
            // Cluster Block
            register("${element.asString()}_cluster", element.clusterBlock)
        }
    }
}
