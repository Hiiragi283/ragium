package hiiragi283.ragium.setup

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTTankBlock
import hiiragi283.ragium.common.block.storage.HTUniversalChestBlock
import hiiragi283.ragium.common.item.block.HTBatteryBlockItem
import hiiragi283.ragium.common.item.block.HTCrateBlockItem
import hiiragi283.ragium.common.item.block.HTMachineBlockItem
import hiiragi283.ragium.common.item.block.HTTankBlockItem
import hiiragi283.ragium.common.item.block.HTUniversalChestBlockItem
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.common.text.RagiumTranslation
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus
import java.util.function.UnaryOperator

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

        for (material: RagiumMaterial in RagiumMaterial.entries) {
            // Storage Block
            val operator: UnaryOperator<BlockBehaviour.Properties> = when (material) {
                RagiumMaterial.RAGINITE -> UnaryOperator { it.mapColor(MapColor.COLOR_RED) }
                RagiumMaterial.RAGI_CRYSTAL -> UnaryOperator { it.mapColor(MapColor.COLOR_RED).sound(SoundType.AMETHYST) }
                RagiumMaterial.RAGI_ALLOY -> UnaryOperator { it.mapColor(MapColor.COLOR_RED).sound(SoundType.COPPER) }
                RagiumMaterial.ADVANCED_RAGI_ALLOY -> UnaryOperator { it.mapColor(MapColor.COLOR_RED).sound(SoundType.COPPER) }
            }
            val properties: BlockBehaviour.Properties = operator.apply(BlockBehaviour.Properties.of().strength(5f, 9f))
            val prefix: HTMaterialPrefix = HCMaterialPrefixes.STORAGE_BLOCK
            register(prefix, material, properties)
        }
    }.let(::HTMaterialTable)

    //    Processors    //

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
    val UNIVERSAL_CHEST: HTDeferredBlock<HTUniversalChestBlock, HTUniversalChestBlockItem> = REGISTER.register(
        RagiumConst.UNIVERSAL_CHEST,
        machine(),
        ::HTUniversalChestBlock,
        ::HTUniversalChestBlockItem,
    ) { it.component(RagiumDataComponents.COLOR, DyeColor.WHITE) }

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    fun machine(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .mapColor(MapColor.COLOR_BLACK)
        .requiresCorrectToolForDrops()
        .sound(SoundType.COPPER)
        .strength(3.5f, 16f)

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
