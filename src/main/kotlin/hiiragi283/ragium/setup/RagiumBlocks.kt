package hiiragi283.ragium.setup

import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.function.partially2
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.registry.HTBasicDeferredBlock
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.text.RagiumTranslation
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
        registerMachine(RagiumBlockEntityTypes.ALLOY_SMELTER, RagiumTranslation.ALLOY_SMELTER)

    @JvmField
    val CRUSHER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.CRUSHER, RagiumTranslation.CRUSHER)

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.CUTTING_MACHINE, RagiumTranslation.CUTTING_MACHINE)

    @JvmField
    val FORMING_PRESS: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.FORMING_PRESS, RagiumTranslation.FORMING_PRESS)

    // Heat
    @JvmField
    val DRYER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.DRYER, RagiumTranslation.DRYER)

    @JvmField
    val MELTER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.MELTER, RagiumTranslation.MELTER)

    @JvmField
    val PYROLYZER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.PYROLYZER, RagiumTranslation.PYROLYZER)

    @JvmField
    val SOLIDIFIER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.SOLIDIFIER, RagiumTranslation.SOLIDIFIER)

    // Chemical
    @JvmField
    val MIXER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.MIXER, RagiumTranslation.MIXER)

    // Matter

    //    Device    //

    // Basic
    @JvmField
    val FERMENTER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.FERMENTER, RagiumTranslation.FERMENTER)

    @JvmField
    val PLANTER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.PLANTER, RagiumTranslation.PLANTER)

    // Enchanting
    @JvmField
    val ENCHANTER: HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> =
        registerMachine(RagiumBlockEntityTypes.ENCHANTER, RagiumTranslation.ENCHANTER)

    //    Storages    //

    @JvmField
    val BATTERY: HTDeferredBlock<HTBatteryBlock, HTBatteryBlockItem> = REGISTER.register(
        "battery",
        machine().noOcclusion(),
        ::HTBatteryBlock.partially1(RagiumBlockEntityTypes.BATTERY),
        ::HTBatteryBlockItem,
    )

    @JvmField
    val CRATE: HTDeferredBlock<HTCrateBlock, HTCrateBlockItem> = REGISTER.register(
        "crate",
        machine().noOcclusion(),
        ::HTCrateBlock.partially1(RagiumBlockEntityTypes.CRATE),
        ::HTCrateBlockItem,
    )

    @JvmField
    val TANK: HTDeferredBlock<HTTankBlock, HTTankBlockItem> = REGISTER.register(
        "tank",
        machine().noOcclusion(),
        ::HTTankBlock.partially1(RagiumBlockEntityTypes.TANK),
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

    //    Creatives    //

    @JvmField
    val CREATIVE_BATTERY: HTDeferredBlock<HTBatteryBlock, HTBatteryBlockItem> = REGISTER.register(
        "creative_battery",
        machine().noOcclusion(),
        ::HTBatteryBlock.partially1(RagiumBlockEntityTypes.CREATIVE_BATTERY),
        ::HTBatteryBlockItem,
    )

    @JvmField
    val CREATIVE_CRATE: HTDeferredBlock<HTCrateBlock, HTCrateBlockItem> = REGISTER.register(
        "creative_crate",
        machine().noOcclusion(),
        ::HTCrateBlock.partially1(RagiumBlockEntityTypes.CREATIVE_CRATE),
        ::HTCrateBlockItem,
    )

    @JvmField
    val CREATIVE_TANK: HTDeferredBlock<HTTankBlock, HTTankBlockItem> = REGISTER.register(
        "creative_tank",
        machine().noOcclusion(),
        ::HTTankBlock.partially1(RagiumBlockEntityTypes.CREATIVE_TANK),
        ::HTTankBlockItem,
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
        properties: BlockBehaviour.Properties = machine(),
    ): HTDeferredBlock<HTMachineBlock, HTMachineBlockItem> = REGISTER.register(
        type.path,
        properties,
        ::HTMachineBlock.partially2(translation, type),
        ::HTMachineBlockItem,
    )
}
