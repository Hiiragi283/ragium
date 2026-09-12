package hiiragi283.ragium.common.block

import hiiragi283.lib.collection.ListMultiMap
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.collection.buildSortedSetMultiMap
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTOreBlockPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DropExperienceBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus

data object RagiumBlocks {
    @JvmStatic
    private val BLOCK_ONLY = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(BLOCK_ONLY)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.addAlias("steel_block", "sooty_iron_block")

        REGISTER.register(eventBus)
    }

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of().strength(hardness, resistance)

    @JvmStatic
    fun machine(): BlockBehaviour.Properties = properties(3.5f, 16f)
        .mapColor(MapColor.COLOR_BLACK)
        .requiresCorrectToolForDrops()
        .sound(SoundType.COPPER)

    @JvmStatic
    private fun registerMachine(
        type: HTDeferredBlockEntityType<*>,
        properties: BlockBehaviour.Properties = machine()
    ): HTBasicDeferredBlockAndItem<HTMachineBlock> = REGISTER.registerSimple(
        type.idOrThrow.path,
        properties,
        { prop: BlockBehaviour.Properties -> HTMachineBlock(type, prop) }
    )

    @JvmStatic
    private fun registerFakeMachine(
        name: String,
        properties: BlockBehaviour.Properties = machine()
    ): HTBasicDeferredBlockAndItem<HTMachineBlock> = REGISTER.registerSimple(
        name,
        properties,
        { prop: BlockBehaviour.Properties -> HTMachineBlock(RagiumBlockEntityTypes.CRUSHER, prop) } // TODO
    )

    //    Ingredient    //

    @JvmField
    val MATERIAL_ORES: Table<HTOreBlockPart, RagiumMaterial, HTSimpleDeferredBlockAndItem> = buildSortedSetMultiMap {
        putAll(RagiumMaterial.Mineral.SULFUR, HTOreBlockPart.STONE, HTOreBlockPart.DEEPSLATE, HTOreBlockPart.NETHER)
        putAll(RagiumMaterial.Mineral.NITER, HTOreBlockPart.STONE, HTOreBlockPart.DEEPSLATE, HTOreBlockPart.NETHER)
    }.flatMapTable { (material: RagiumMaterial, parts: Collection<HTOreBlockPart>) ->
        parts.map { part: HTOreBlockPart ->
            val properties: BlockBehaviour.Properties = when (part) {
                HTOreBlockPart.STONE -> Blocks.COAL_ORE
                HTOreBlockPart.DEEPSLATE -> Blocks.DEEPSLATE_COAL_ORE
                HTOreBlockPart.NETHER -> Blocks.NETHER_QUARTZ_ORE
                HTOreBlockPart.END -> Blocks.END_STONE
            }.let(::copyOf)
            Triple(
                part,
                material,
                REGISTER.registerSimple(
                    part.createName(material),
                    properties,
                    blockFactory = { DropExperienceBlock(UniformInt.of(0, 2), it) }
                )
            )
        }
    }

    @JvmField
    val STORAGE_BLOCKS: Map<RagiumMaterial, HTSimpleDeferredBlockAndItem> = setOf(
        RagiumMaterial.Fuel.CHARCOAL to copyOf(Blocks.COAL_BLOCK).sound(SoundType.TUFF),
        RagiumMaterial.Fuel.COAL_COKE to copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_GRAY),
        RagiumMaterial.Gem.ECHO to copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_CYAN),
        RagiumMaterial.Metal.SOOTY_IRON to copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_GRAY),
        RagiumMaterial.Metal.BLACK_STEEL to copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK),
        RagiumMaterial.Metal.VOID_METAL to copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE)
    ).associateTo(
        sortedMapOf(RagiumMaterial.COMPARATOR)
    ) { (material: RagiumMaterial, properties: BlockBehaviour.Properties) ->
        material to REGISTER.registerSimple(HTStorageBlockPart.DEFAULT.createName(material), properties)
    }

    @JvmField
    val MATERIAL_BLOCKS: Table<HTBlockPart, RagiumMaterial, HTSimpleDeferredBlockAndItem> = buildTable {
        putAll(MATERIAL_ORES)
        STORAGE_BLOCKS.forEach { (material: RagiumMaterial, blockItem: HTSimpleDeferredBlockAndItem) ->
            put(HTStorageBlockPart.DEFAULT, material, blockItem)
        }
    }

    @JvmStatic
    fun getOreOrThrow(part: HTOreBlockPart, material: RagiumMaterial): HTSimpleDeferredBlockAndItem =
        MATERIAL_ORES[part, material] ?: error("Unregistered block: ${part.createName(material)}")

    @JvmStatic
    fun getStorageOrThrow(material: RagiumMaterial): HTSimpleDeferredBlockAndItem =
        STORAGE_BLOCKS[material] ?: error("Unregistered block: ${HTStorageBlockPart.DEFAULT.createName(material)}")

    @JvmStatic
    fun getOrThrow(part: HTBlockPart, material: RagiumMaterial): HTSimpleDeferredBlockAndItem =
        MATERIAL_BLOCKS[part, material] ?: error("Unregistered block: ${part.createName(material)}")

    //    Machine    //

    // Mechanical
    @JvmField
    val ASSEMBLER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.ASSEMBLER)

    @JvmField
    val CRUSHER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.CRUSHER)

    @JvmField
    val COMPRESSOR: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.COMPRESSOR)

    @JvmField
    val CUTTING_MACHINE: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.CUTTING_MACHINE)

    // Heat
    @JvmField
    val FREEZER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.FREEZER)

    @JvmField
    val MELTER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.MELTER)

    @JvmField
    val PYROLYZER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.PYROLYZER)

    @JvmField
    val REFINERY: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.REFINERY)

    // Chemical
    @JvmField
    val CHEMICAL_BATH: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.CHEMICAL_BATH)

    @JvmField
    val CHEMICAL_REACTOR: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.CHEMICAL_REACTOR)

    @JvmField
    val ELECTROLYZER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.ELECTROLYZER)

    @JvmField
    val MIXER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.MIXER)

    // Bio
    @JvmField
    val BREWERY: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.BREWERY)

    @JvmField
    val PLANTER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.PLANTER)

    // Electronics
    @JvmField
    val SCANNER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.SCANNER)

    // Arcane

    @JvmField
    val MACHINES: ListMultiMap<HTMachineType, HTBasicDeferredBlockAndItem<HTMachineBlock>> =
        buildListMultiMap(sortedMapOf()) {
            put(HTMachineType.MECHANICAL, ASSEMBLER)
            put(HTMachineType.MECHANICAL, CRUSHER)
            put(HTMachineType.MECHANICAL, COMPRESSOR)
            put(HTMachineType.MECHANICAL, CUTTING_MACHINE)

            put(HTMachineType.HEAT, FREEZER)
            put(HTMachineType.HEAT, MELTER)
            put(HTMachineType.HEAT, PYROLYZER)
            put(HTMachineType.HEAT, REFINERY)

            put(HTMachineType.CHEMICAL, CHEMICAL_BATH)
            put(HTMachineType.CHEMICAL, CHEMICAL_REACTOR)
            put(HTMachineType.CHEMICAL, ELECTROLYZER)
            put(HTMachineType.CHEMICAL, MIXER)

            put(HTMachineType.BIO, BREWERY)
            put(HTMachineType.BIO, PLANTER)

            put(HTMachineType.ELECTRONICS, SCANNER)
        }
}
