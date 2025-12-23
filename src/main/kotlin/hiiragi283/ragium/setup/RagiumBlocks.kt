package hiiragi283.ragium.setup

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.storage.HTBatteryBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTTankBlock
import hiiragi283.ragium.common.item.block.HTBatteryBlockItem
import hiiragi283.ragium.common.item.block.HTCrateBlockItem
import hiiragi283.ragium.common.item.block.HTTankBlockItem
import hiiragi283.ragium.common.material.RagiumMaterial
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
}
