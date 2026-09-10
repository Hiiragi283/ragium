package hiiragi283.ragium.common.block

import hiiragi283.lib.collection.ListMultiMap
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.collection.buildSetMultiMap
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
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

    //    Ingredient    //

    @JvmField
    val MATERIAL_BLOCKS: Table<HTBlockPart, RagiumMaterial, HTSimpleDeferredBlockAndItem> = buildTable {
        fun register(part: HTBlockPart, material: RagiumMaterial, blockProp: BlockBehaviour.Properties) {
            this[part, material] = REGISTER.registerSimple(part.createName(material), blockProp)
        }

        // Ore
        buildSetMultiMap {
            putAll(RagiumMaterial.Mineral.SULFUR, HTBlockPart.ORE, HTBlockPart.DEEPSLATE_ORE, HTBlockPart.NETHER_ORE)
        }.flatEntries.forEach { (material: RagiumMaterial, part: HTBlockPart) ->
            val properties: BlockBehaviour.Properties = when (part) {
                HTBlockPart.ORE -> Blocks.COAL_ORE
                HTBlockPart.DEEPSLATE_ORE -> Blocks.DEEPSLATE_COAL_ORE
                HTBlockPart.NETHER_ORE -> Blocks.NETHER_QUARTZ_ORE
                HTBlockPart.END_ORE -> Blocks.END_STONE
                else -> return@forEach
            }.let(::copyOf)
            register(part, material, properties)
        }

        // Storage Block
        setOf(
            RagiumMaterial.Fuel.CHARCOAL to copyOf(Blocks.COAL_BLOCK).sound(SoundType.TUFF),
            RagiumMaterial.Fuel.COAL_COKE to copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_GRAY),
            RagiumMaterial.Gem.ECHO to copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_CYAN),
            RagiumMaterial.Metal.SOOTY_IRON to copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_GRAY),
            RagiumMaterial.Metal.BLACK_STEEL to copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK),
            RagiumMaterial.Metal.VOID_METAL to copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE)
        ).forEach { (material: RagiumMaterial, properties: BlockBehaviour.Properties) ->
            register(HTBlockPart.STORAGE_BLOCK, material, properties)
        }
    }

    @JvmStatic
    fun getOrThrow(part: HTBlockPart, material: RagiumMaterial): HTSimpleDeferredBlockAndItem =
        MATERIAL_BLOCKS[part, material] ?: error("Unregistered block: ${part.createName(material)}")

    //    Machine    //

    // Mechanical
    @JvmField
    val ASSEMBLER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.ASSEMBLER)

    @JvmField
    val CRUSHER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.CRUSHER)

    @JvmField
    val COMPRESSOR: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.COMPRESSOR)

    @JvmField
    val CUTTING_MACHINE: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.CUTTING_MACHINE)

    // Heat
    @JvmField
    val FREEZER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.FREEZER)

    @JvmField
    val MELTER: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.MELTER)

    // Chemical
    @JvmField
    val CHEMICAL_BATH: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.CHEMICAL_BATH)

    // Bio
    @JvmField
    val BREWERY: HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(RagiumBlockEntityTypes.BREWERY)

    // Electronics
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

            put(HTMachineType.CHEMICAL, CHEMICAL_BATH)

            put(HTMachineType.BIO, BREWERY)
        }
}
