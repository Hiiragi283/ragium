package hiiragi283.ragium.setup

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.function.partially2
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.registry.HTBasicDeferredBlockAndItem
import hiiragi283.core.common.registry.HTDeferredBlockAndItem
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.common.registry.register.HTDeferredBlockAndItemRegister
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.text.RagiumTranslation
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.HTMeatBlock
import hiiragi283.ragium.common.block.generator.HTBoilerBlock
import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTTankBlock
import hiiragi283.ragium.common.block.storage.HTUniversalChestBlock
import hiiragi283.ragium.common.item.block.HTBatteryBlockItem
import hiiragi283.ragium.common.item.block.HTCrateBlockItem
import hiiragi283.ragium.common.item.block.HTImitationSpawnerBlockItem
import hiiragi283.ragium.common.item.block.HTMachineBlockItem
import hiiragi283.ragium.common.item.block.HTTankBlockItem
import hiiragi283.ragium.common.item.block.HTUniversalChestBlockItem
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus

/**
 * @see hiiragi283.core.setup.HCBlocks
 */
object RagiumBlocks {
    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Materials    //

    @JvmField
    val MEAT_BLOCK: HTBasicDeferredBlockAndItem<HTMeatBlock> = REGISTER.registerSimple(
        "meat_block",
        copyOf(Blocks.MUD).mapColor(MapColor.COLOR_RED).requiredFeatures(HiiragiCoreAPI.EXPERIMENTAL),
        ::HTMeatBlock.partially1(Foods.BEEF),
    )

    @JvmField
    val COOKED_MEAT_BLOCK: HTBasicDeferredBlockAndItem<HTMeatBlock> = REGISTER.registerSimple(
        "cooked_meat_block",
        copyOf(Blocks.PACKED_MUD).mapColor(MapColor.COLOR_RED).requiredFeatures(HiiragiCoreAPI.EXPERIMENTAL),
        ::HTMeatBlock.partially1(Foods.COOKED_BEEF),
    )

    @JvmField
    val INDUSTRIAL_TNT: HTSimpleDeferredBlockAndItem =
        REGISTER.registerSimple("industrial_tnt", copyOf(Blocks.TNT).mapColor(MapColor.TERRACOTTA_ORANGE))

    //    Generator    //

    // Basic
    @JvmField
    val BOILER: HTDeferredBlockAndItem<HTBoilerBlock, HTMachineBlockItem> =
        REGISTER.register(RagiumConst.BOILER, machine(), ::HTBoilerBlock, ::HTMachineBlockItem)

    //    Machine    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.ALLOY_SMELTER, RagiumTranslation.ALLOY_SMELTER)

    @JvmField
    val ASSEMBLER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.ASSEMBLER, RagiumTranslation.ASSEMBLER)

    @JvmField
    val AUTO_CHISEL: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.AUTO_CHISEL, RagiumTranslation.AUTO_CHISEL)

    @JvmField
    val COMPRESSOR: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.COMPRESSOR, RagiumTranslation.COMPRESSOR)

    @JvmField
    val CRUSHER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.CRUSHER, RagiumTranslation.CRUSHER)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.CUTTING_MACHINE, RagiumTranslation.CUTTING_MACHINE)

    @JvmField
    val ELECTRIC_FURNACE: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.ELECTRIC_FURNACE, RagiumTranslation.ELECTRIC_FURNACE)

    @JvmField
    val PLANTER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.PLANTER, RagiumTranslation.PLANTER)

    // Advanced
    @JvmField
    val FREEZER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.FREEZER, RagiumTranslation.FREEZER)

    @JvmField
    val MELTER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.MELTER, RagiumTranslation.MELTER)

    @JvmField
    val PYROLYZER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.PYROLYZER, RagiumTranslation.PYROLYZER)

    @JvmField
    val REFINERY: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> = registerMachine(
        RagiumBlockEntityTypes.REFINERY,
        RagiumTranslation.REFINERY,
        machine().noOcclusion().requiredFeatures(HiiragiCoreAPI.EXPERIMENTAL),
    )

    @JvmField
    val WASHER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.WASHER, RagiumTranslation.WASHER)

    // Elite
    @JvmField
    val BREWERY: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.BREWERY, RagiumTranslation.BREWERY)

    @JvmField
    val MIXER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> = registerMachine(
        RagiumBlockEntityTypes.MIXER,
        RagiumTranslation.MIXER,
        machine().requiredFeatures(HiiragiCoreAPI.EXPERIMENTAL),
    )

    // Ultimate
    @JvmField
    val FLUID_DUPLICATOR: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.FLUID_DUPLICATOR, RagiumTranslation.FLUID_DUPLICATOR, machine().noOcclusion())

    //    Device    //

    // Ultimate
    @JvmField
    val ENCHANTER: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.ENCHANTER, RagiumTranslation.ENCHANTER)

    @JvmField
    val MASS_FABRICATOR: HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.MASS_FABRICATOR, RagiumTranslation.MASS_FABRICATOR)

    //    Storages    //

    @JvmField
    val UNIVERSAL_CHEST: HTDeferredBlockAndItem<HTUniversalChestBlock, HTUniversalChestBlockItem> = REGISTER.register(
        RagiumConst.UNIVERSAL_CHEST,
        machine(),
        ::HTUniversalChestBlock,
        ::HTUniversalChestBlockItem,
    ) { it.component(HCDataComponents.COLOR, HTDefaultColor.WHITE) }

    // Variable
    @JvmField
    val BATTERY: HTDeferredBlockAndItem<HTBatteryBlock, HTBatteryBlockItem> = REGISTER.register(
        "battery",
        machine().noOcclusion(),
        ::HTBatteryBlock.partially1(RagiumBlockEntityTypes.BATTERY),
        ::HTBatteryBlockItem,
    ) { prop: Item.Properties -> prop.component(RagiumDataComponents.CAPACITY_SCALE, 1) }

    @JvmField
    val CRATE: HTDeferredBlockAndItem<HTCrateBlock, HTCrateBlockItem> = REGISTER.register(
        "crate",
        machine().noOcclusion(),
        ::HTCrateBlock.partially1(RagiumBlockEntityTypes.CRATE),
        ::HTCrateBlockItem,
    ) { prop: Item.Properties -> prop.component(RagiumDataComponents.CAPACITY_SCALE, 1) }

    @JvmField
    val TANK: HTDeferredBlockAndItem<HTTankBlock, HTTankBlockItem> = REGISTER.register(
        "tank",
        machine().noOcclusion(),
        ::HTTankBlock.partially1(RagiumBlockEntityTypes.TANK),
        ::HTTankBlockItem,
    ) { prop: Item.Properties -> prop.component(RagiumDataComponents.CAPACITY_SCALE, 1) }

    // Void
    @JvmField
    val VOID_TANK: HTDeferredBlockAndItem<HTTankBlock, HTTankBlockItem> = REGISTER.register(
        "void_tank",
        machine().noOcclusion(),
        ::HTTankBlock.partially1(RagiumBlockEntityTypes.VOID_TANK),
        ::HTTankBlockItem,
    )

    // Creative
    @JvmField
    val CREATIVE_BATTERY: HTDeferredBlockAndItem<HTBatteryBlock, HTBatteryBlockItem> = REGISTER.register(
        "creative_battery",
        machine().noOcclusion(),
        ::HTBatteryBlock.partially1(RagiumBlockEntityTypes.CREATIVE_BATTERY),
        ::HTBatteryBlockItem,
    )

    @JvmField
    val CREATIVE_CRATE: HTDeferredBlockAndItem<HTCrateBlock, HTCrateBlockItem> = REGISTER.register(
        "creative_crate",
        machine().noOcclusion(),
        ::HTCrateBlock.partially1(RagiumBlockEntityTypes.CREATIVE_CRATE),
        ::HTCrateBlockItem,
    )

    @JvmField
    val CREATIVE_TANK: HTDeferredBlockAndItem<HTTankBlock, HTTankBlockItem> = REGISTER.register(
        "creative_tank",
        machine().noOcclusion(),
        ::HTTankBlock.partially1(RagiumBlockEntityTypes.CREATIVE_TANK),
        ::HTTankBlockItem,
    )

    //    Utilities    //

    @JvmField
    val IMITATION_SPAWNER: HTDeferredBlockAndItem<HTImitationSpawnerBlock, HTImitationSpawnerBlockItem> =
        REGISTER.register(
            "imitation_spawner",
            copyOf(Blocks.SPAWNER).requiredFeatures(HiiragiCoreAPI.EXPERIMENTAL),
            ::HTImitationSpawnerBlock,
            ::HTImitationSpawnerBlockItem,
        )

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties = BlockBehaviour.Properties.of().strength(hardness, resistance)

    @JvmStatic
    fun machine(): BlockBehaviour.Properties = properties(3.5f, 16f)
        .mapColor(MapColor.COLOR_BLACK)
        .requiresCorrectToolForDrops()
        .sound(SoundType.COPPER)

    @JvmStatic
    private fun registerMachine(
        type: HTDeferredBlockEntityType<*>,
        translation: HTTranslation,
        properties: BlockBehaviour.Properties = machine(),
    ): HTDeferredBlockAndItem<HTMachineBlock, HTMachineBlockItem> = REGISTER.register(
        type.path,
        properties,
        ::HTMachineBlock.partially2(translation, type),
        ::HTMachineBlockItem,
    )
}
