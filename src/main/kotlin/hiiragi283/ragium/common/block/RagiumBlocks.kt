package hiiragi283.ragium.common.block

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.collection.buildSetMultiMap
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.collection.mutableEnumMapOf
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.ItemWithContextFactory
import hiiragi283.lib.transfer.fluid.HTItemAccessFluidHandler
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.api.data.chemical.RagiumChemicals
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.HTOreBlockPart
import hiiragi283.ragium.api.material.HTStorageBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.bus.HTFluidBusBlock
import hiiragi283.ragium.common.block.storage.HTTankBlock
import hiiragi283.ragium.common.block.storage.HTVoidTankBlock
import hiiragi283.ragium.common.item.block.HTCreativeTankBlockItem
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DropExperienceBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.transfer.InfiniteResourceHandler
import net.neoforged.neoforge.transfer.energy.InfiniteEnergyHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource

data object RagiumBlocks {
    @JvmStatic
    private val BLOCK_ONLY = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(BLOCK_ONLY)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.addAlias("steel_block", "sooty_iron_block")

        eventBus.addListener(::registerCapabilities)
        eventBus.addListener(::modifyDefaultComponents)

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
    private fun <BLOCK : Block> registerMachine(
        type: HTDeferredBlockEntityType<*>,
        factory: (HTDeferredBlockEntityType<*>, BlockBehaviour.Properties) -> BLOCK,
        properties: BlockBehaviour.Properties = machine()
    ): HTBasicDeferredBlockAndItem<BLOCK> =
        REGISTER.registerSimple(type.idOrThrow.path, { factory(type, properties.setId(it)) })

    @JvmStatic
    private fun <BLOCK : Block, ITEM : Item> registerMachine(
        type: HTDeferredBlockEntityType<*>,
        factory: (HTDeferredBlockEntityType<*>, BlockBehaviour.Properties) -> BLOCK,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        properties: BlockBehaviour.Properties = machine()
    ): HTDeferredBlockAndItem<BLOCK, ITEM> =
        REGISTER.register(type.idOrThrow.path, { factory(type, properties.setId(it)) }, itemFactory)

    @JvmStatic
    private fun registerMachine(
        type: HTDeferredBlockEntityType<*>,
        properties: BlockBehaviour.Properties = machine()
    ): HTBasicDeferredBlockAndItem<HTMachineBlock> = registerMachine(type, ::HTMachineBlock, properties)

    @JvmStatic
    private fun registerFakeMachine(
        name: String,
        properties: BlockBehaviour.Properties = machine()
    ): HTBasicDeferredBlockAndItem<HTMachineBlock> = REGISTER.registerSimple(
        name,
        { HTMachineBlock(RagiumBlockEntityTypes.CRUSHER, properties.setId(it)) } // TODO
    )

    //    Ingredient    //

    @JvmField
    val MATERIAL_ORES: Table<HTOreBlockPart, RagiumMaterial, HTSimpleDeferredBlockAndItem> = buildSetMultiMap(
        {
            putAll(RagiumMaterial.Mineral.SULFUR, HTOreBlockPart.STONE, HTOreBlockPart.DEEPSLATE, HTOreBlockPart.NETHER)
            putAll(RagiumMaterial.Mineral.NITER, HTOreBlockPart.STONE, HTOreBlockPart.DEEPSLATE, HTOreBlockPart.NETHER)
        },
        ::sortedSetOf
    ).flatMapTable { (material: RagiumMaterial, parts: Collection<HTOreBlockPart>) ->
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
                    blockFactory = { DropExperienceBlock(UniformInt.of(0, 2), properties.setId(it)) },
                    itemProp = { prop: Item.Properties ->
                        material.chemicalKey?.let { prop.delayedHolderComponent(RagiumDataComponents.CHEMICAL, it) }
                        prop
                    }
                )
            )
        }
    }

    @JvmField
    val STORAGE_BLOCKS: Map<RagiumMaterial, HTSimpleDeferredBlockAndItem> = setOf(
        RagiumMaterial.Fuel.CHARCOAL to copyOf(Blocks.COAL_BLOCK).sound(SoundType.TUFF),
        RagiumMaterial.Fuel.COAL_COKE to copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_GRAY),
        RagiumMaterial.Fuel.PITCH_COKE to copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_BLUE),
        RagiumMaterial.Metal.ALUMINUM to copyOf(Blocks.COPPER_BLOCK).mapColor(MapColor.COLOR_PINK),
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

    // Echo
    @JvmField
    val ECHO_BLOCK: HTSimpleDeferredBlockAndItem =
        REGISTER.registerSimple("echo_block", copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_CYAN))

    // Fluorite
    @JvmField
    val FLUORITE_BLOCK: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple(
        "fluorite_block",
        copyOf(Blocks.QUARTZ_BLOCK).mapColor(MapColor.COLOR_GREEN),
        itemProp = { it.delayedHolderComponent(RagiumDataComponents.CHEMICAL, RagiumChemicals.FLUORITE) }
    )

    @JvmField
    val FLUORITE_SLAB: HTBasicDeferredBlockAndItem<SlabBlock> = REGISTER.registerSimple(
        "fluorite_slab",
        copyOf(Blocks.QUARTZ_SLAB),
        ::SlabBlock
    )

    @JvmField
    val FLUORITE_STAIRS: HTBasicDeferredBlockAndItem<StairBlock> = REGISTER.registerSimple(
        "fluorite_stairs",
        blockFactory = { key: ResourceKey<Block> ->
            StairBlock(FLUORITE_BLOCK.defaultState, copyOf(FLUORITE_BLOCK.getOrThrow()).setId(key))
        }
    )

    // Cryolite
    @JvmField
    val CRYOLITE_BLOCK: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple(
        "cryolite_block",
        copyOf(Blocks.QUARTZ_BLOCK),
        itemProp = { it.delayedHolderComponent(RagiumDataComponents.CHEMICAL, RagiumChemicals.CRYOLITE) }
    )

    @JvmField
    val CRYOLITE_SLAB: HTBasicDeferredBlockAndItem<SlabBlock> = REGISTER.registerSimple(
        "cryolite_slab",
        copyOf(Blocks.QUARTZ_SLAB),
        ::SlabBlock
    )

    @JvmField
    val CRYOLITE_STAIRS: HTBasicDeferredBlockAndItem<StairBlock> = REGISTER.registerSimple(
        "cryolite_stairs",
        blockFactory = { key: ResourceKey<Block> ->
            StairBlock(CRYOLITE_BLOCK.defaultState, copyOf(CRYOLITE_BLOCK.getOrThrow()).setId(key))
        }
    )

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
    val ALLOY_SMELTER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.ALLOY_SMELTER)

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

    @JvmField
    val SMELTER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.SMELTER)

    // Chemical
    @JvmField
    val CHEMICAL_BATH: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.CHEMICAL_BATH)

    @JvmField
    val CHEMICAL_REACTOR: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.CHEMICAL_REACTOR)

    @JvmField
    val MIXER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.MIXER)

    // Bio
    @JvmField
    val BREWERY: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.BREWERY)

    @JvmField
    val PLANTER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.PLANTER)

    // Electronics
    @JvmField
    val ELECTROLYZER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.ELECTROLYZER)

    @JvmField
    val PRECISION_ASSEMBLER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.PRECISION_ASSEMBLER)

    @JvmField
    val SCANNER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerFakeMachine(RagiumConstants.SCANNER)

    // Arcane
    @JvmField
    val ENCHANTER: HTBasicDeferredBlockAndItem<HTMachineBlock> =
        registerMachine(RagiumBlockEntityTypes.ENCHANTER)

    @JvmField
    val MACHINES: Map<HTMachineType, List<HTBasicDeferredBlockAndItem<HTMachineBlock>>> =
        buildListMultiMap(
            sortedMapOf(),
            {
                put(HTMachineType.MECHANICAL, ASSEMBLER)
                put(HTMachineType.MECHANICAL, CRUSHER)
                put(HTMachineType.MECHANICAL, COMPRESSOR)
                put(HTMachineType.MECHANICAL, CUTTING_MACHINE)

                put(HTMachineType.HEAT, ALLOY_SMELTER)
                put(HTMachineType.HEAT, FREEZER)
                put(HTMachineType.HEAT, MELTER)
                put(HTMachineType.HEAT, PYROLYZER)
                put(HTMachineType.HEAT, REFINERY)
                put(HTMachineType.HEAT, SMELTER)

                put(HTMachineType.CHEMICAL, CHEMICAL_BATH)
                put(HTMachineType.CHEMICAL, CHEMICAL_REACTOR)
                put(HTMachineType.CHEMICAL, MIXER)

                put(HTMachineType.BIO, BREWERY)
                put(HTMachineType.BIO, PLANTER)

                put(HTMachineType.ELECTRONICS, ELECTROLYZER)
                put(HTMachineType.ELECTRONICS, PRECISION_ASSEMBLER)
                put(HTMachineType.ELECTRONICS, SCANNER)

                put(HTMachineType.ARCANE, ENCHANTER)
            }
        )

    //    Bus    //

    @JvmField
    val FLUID_OUTPUT_BUS: HTBasicDeferredBlockAndItem<HTFluidBusBlock> = registerMachine(
        RagiumBlockEntityTypes.FLUID_OUTPUT_BUS,
        ::HTFluidBusBlock
    )

    //    Storage    //

    @JvmField
    val TANK: HTBasicDeferredBlockAndItem<HTTankBlock> = registerMachine(
        RagiumBlockEntityTypes.TANK,
        ::HTTankBlock,
        machine().noOcclusion()
    )

    // Void
    @JvmField
    val VOID_TANK: HTBasicDeferredBlockAndItem<HTVoidTankBlock> = REGISTER.registerSimple(
        "void_tank",
        machine().noOcclusion(),
        ::HTVoidTankBlock
    )

    // Creative
    @JvmField
    val CREATIVE_BATTERY: HTBasicDeferredBlockAndItem<HTBasicEntityBlock> =
        registerMachine(RagiumBlockEntityTypes.CREATIVE_BATTERY, ::HTBasicEntityBlock)

    @JvmField
    val CREATIVE_TANK: HTDeferredBlockAndItem<HTTankBlock, HTCreativeTankBlockItem> = registerMachine(
        RagiumBlockEntityTypes.CREATIVE_TANK,
        ::HTTankBlock,
        ::HTCreativeTankBlockItem,
        machine().noOcclusion()
    )

    //    Decoration    //

    @JvmField
    val MACHINE_CASING: HTSimpleDeferredBlockAndItem = REGISTER.registerSimple("machine_casing", machine())

    @JvmField
    val MACHINE_CASINGS: Map<HTMachineType, HTSimpleDeferredBlockAndItem> = HTMachineType.entries
        .associateWithTo(mutableEnumMapOf()) { machineType: HTMachineType ->
            REGISTER.registerSimple("${machineType.materialName}_machine_casing", machine())
        }

    @JvmStatic
    fun getCasing(machineType: HTMachineType): HTSimpleDeferredBlockAndItem = MACHINE_CASINGS[machineType]!!

    //    Events    //

    @JvmStatic
    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        // Block
        event.registerItem(
            HTFluidCapabilities.item,
            { _, access ->
                HTItemAccessFluidHandler.output(access, RagiumConfig.SERVER.tankCapacity.asInt)
            },
            FLUID_OUTPUT_BUS
        )

        event.registerItem(
            HTFluidCapabilities.item,
            { _, access ->
                HTItemAccessFluidHandler.create(access, RagiumConfig.SERVER.tankCapacity.asInt)
            },
            TANK
        )
        event.registerItem(
            HTFluidCapabilities.item,
            { _, _ -> HTVoidTankBlock.VOIDING_HANDLER },
            VOID_TANK
        )
        event.registerItem(
            HTEnergyCapabilities.item,
            { _, _ -> InfiniteEnergyHandler.INSTANCE },
            CREATIVE_BATTERY
        )
        event.registerItem(
            HTFluidCapabilities.item,
            { stack: ItemStack, _ ->
                HTStorageHelper.getFluid(stack)
                    .let(FluidResource::of)
                    .takeUnless(FluidResource::isEmpty)
                    ?.let(::InfiniteResourceHandler)
            },
            CREATIVE_TANK
        )
    }

    @JvmStatic
    private fun modifyDefaultComponents(event: ModifyDefaultComponentsEvent) {
        // Block
        setOf(
            RagiumMaterial.Metal.BLACK_STEEL to Rarity.UNCOMMON,
            RagiumMaterial.Metal.VOID_METAL to Rarity.RARE
        ).forEach { (material: RagiumMaterial, rarity: Rarity) ->
            for (part: HTBlockPart in HTBlockPart.entries) {
                val block: ItemLike = MATERIAL_BLOCKS[part, material] ?: continue
                event.modify(block) { builder: DataComponentMap.Builder, _, _ ->
                    builder.set(DataComponents.RARITY, rarity)
                }
            }
        }
        event.modify(CREATIVE_BATTERY) { builder: DataComponentMap.Builder, _, _ ->
            builder.set(DataComponents.RARITY, Rarity.EPIC)
        }
    }
}
