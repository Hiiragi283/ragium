package hiiragi283.ragium.setup

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTBasicDeferredBlock
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.HTMeatBlock
import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTResonantInterfaceBlock
import hiiragi283.ragium.common.block.storage.HTTankBlock
import hiiragi283.ragium.common.block.storage.HTUniversalChestBlock
import hiiragi283.ragium.common.item.block.HTBatteryBlockItem
import hiiragi283.ragium.common.item.block.HTCrateBlockItem
import hiiragi283.ragium.common.item.block.HTImitationSpawnerBlockItem
import hiiragi283.ragium.common.item.block.HTMachineBlockItem
import hiiragi283.ragium.common.item.block.HTResonantInterfaceBlockItem
import hiiragi283.ragium.common.item.block.HTTankBlockItem
import hiiragi283.ragium.common.item.block.HTUniversalChestBlockItem
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.text.RagiumTranslation
import net.minecraft.world.food.Foods
import net.minecraft.world.item.DyeColor
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
    val REGISTER = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Materials    //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredBlock> = buildTable {
        fun register(prefix: HTPrefixLike, material: HTMaterialLike, properties: BlockBehaviour.Properties) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = REGISTER.registerSimple(prefix.createPath(material), properties)
        }

        // Ore
        val ore: BlockBehaviour.Properties =
            properties(3f).mapColor(MapColor.STONE).requiresCorrectToolForDrops()
        val deepOre: BlockBehaviour.Properties =
            properties(4.5f, 3f).mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()

        register(HCMaterialPrefixes.ORE, RagiumMaterialKeys.RAGINITE, ore)
        register(HCMaterialPrefixes.ORE_DEEPSLATE, RagiumMaterialKeys.RAGINITE, deepOre)

        register(HCMaterialPrefixes.ORE, RagiumMaterialKeys.RAGI_CRYSTAL, ore)
        register(HCMaterialPrefixes.ORE_DEEPSLATE, RagiumMaterialKeys.RAGI_CRYSTAL, deepOre)

        // Storage Block
        val redProp: () -> BlockBehaviour.Properties = { properties(5f, 9f).mapColor(MapColor.COLOR_RED) }
        arrayOf(
            RagiumMaterialKeys.RAGINITE to redProp(),
            RagiumMaterialKeys.RAGI_CRYSTAL to redProp().sound(SoundType.AMETHYST),
            RagiumMaterialKeys.RAGI_ALLOY to redProp().sound(SoundType.COPPER),
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY to redProp().sound(SoundType.METAL),
        ).forEach { (key: HTMaterialKey, properties: BlockBehaviour.Properties) ->
            register(HCMaterialPrefixes.STORAGE_BLOCK, key, properties)
        }
    }.let(::HTMaterialTable)

    @JvmField
    val MEAT_BLOCK: HTBasicDeferredBlock<HTMeatBlock> = REGISTER.registerSimple(
        "meat_block",
        copyOf(Blocks.MUD).mapColor(MapColor.COLOR_RED),
        ::HTMeatBlock.partially1(Foods.BEEF),
    )

    @JvmField
    val COOKED_MEAT_BLOCK: HTBasicDeferredBlock<HTMeatBlock> = REGISTER.registerSimple(
        "cooked_meat_block",
        copyOf(Blocks.PACKED_MUD).mapColor(MapColor.COLOR_RED),
        ::HTMeatBlock.partially1(Foods.COOKED_BEEF),
    )

    //    Machine    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.ALLOY_SMELTER, RagiumTranslation.ALLOY_SMELTER, RagiumMenuTypes.ALLOY_SMELTER)

    @JvmField
    val CRUSHER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.CRUSHER, RagiumTranslation.CRUSHER, RagiumMenuTypes.CRUSHER)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.CUTTING_MACHINE, RagiumTranslation.CUTTING_MACHINE, RagiumMenuTypes.CUTTING_MACHINE)

    // Advanced
    @JvmField
    val DRYER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.DRYER, RagiumTranslation.DRYER, RagiumMenuTypes.DRYER)

    @JvmField
    val MELTER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.MELTER, RagiumTranslation.MELTER, RagiumMenuTypes.MELTER)

    @JvmField
    val MIXER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.MIXER, RagiumTranslation.MIXER, RagiumMenuTypes.MIXER)

    @JvmField
    val PYROLYZER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.PYROLYZER, RagiumTranslation.PYROLYZER, RagiumMenuTypes.PYROLYZER)

    //    Device    //

    // Basic
    @JvmField
    val PLANTER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.PLANTER, RagiumTranslation.PLANTER, RagiumMenuTypes.PLANTER)

    //    Storages    //

    @JvmField
    val BATTERY: HTDeferredBlock<HTBatteryBlock, HTBatteryBlockItem> = REGISTER.register(
        "battery",
        machine().noOcclusion(),
        ::HTBatteryBlock,
        ::HTBatteryBlockItem,
    )

    @JvmField
    val CRATE: HTDeferredBlock<HTCrateBlock, HTCrateBlockItem> = REGISTER.register(
        "crate",
        machine().noOcclusion(),
        ::HTCrateBlock,
        ::HTCrateBlockItem,
    )

    @JvmField
    val TANK: HTDeferredBlock<HTTankBlock, HTTankBlockItem> = REGISTER.register(
        "tank",
        machine().noOcclusion(),
        ::HTTankBlock,
        ::HTTankBlockItem,
    )

    @JvmField
    val RESONANT_INTERFACE: HTDeferredBlock<HTResonantInterfaceBlock, HTResonantInterfaceBlockItem> = REGISTER.register(
        "resonant_interface",
        machine(),
        ::HTResonantInterfaceBlock,
        ::HTResonantInterfaceBlockItem,
    )

    @JvmField
    val UNIVERSAL_CHEST: HTDeferredBlock<HTUniversalChestBlock, HTUniversalChestBlockItem> = REGISTER.register(
        RagiumConst.UNIVERSAL_CHEST,
        machine(),
        ::HTUniversalChestBlock,
        ::HTUniversalChestBlockItem,
    ) { it.component(RagiumDataComponents.COLOR, DyeColor.WHITE) }

    //    Utilities    //

    @JvmField
    val IMITATION_SPAWNER: HTDeferredBlock<HTImitationSpawnerBlock, HTImitationSpawnerBlockItem> =
        REGISTER.register(
            "imitation_spawner",
            copyOf(Blocks.SPAWNER),
            ::HTImitationSpawnerBlock,
            ::HTImitationSpawnerBlockItem,
        )

    //    Extensions    //

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
        translation: HTTranslation,
        menuType: HTDeferredMenuType.WithContext<*, *>? = null,
        properties: BlockBehaviour.Properties = machine(),
    ): HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> = REGISTER.register(
        type.getPath(),
        properties,
        { properties: BlockBehaviour.Properties ->
            object : HTMachineBlock(type, properties) {
                override fun getDescription(): HTTranslation = translation

                override fun getMenuType(): HTDeferredMenuType.WithContext<*, *>? = menuType
            }
        },
        ::HTMachineBlockItem,
    )
}
