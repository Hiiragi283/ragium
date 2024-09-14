package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.block.HTCreativePowerSourceBlock
import hiiragi283.ragium.common.block.HTManualGrinderBlock
import hiiragi283.ragium.common.block.entity.HTBurningBoxBlockEntity
import hiiragi283.ragium.common.block.entity.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.util.getFilteredInstances
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.ExperienceDroppingBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.intprovider.ConstantIntProvider

object RagiumBlocks {

    @JvmField
    val RAGINITE_ORE: Block = register(
        "raginite_ore", ExperienceDroppingBlock(
            ConstantIntProvider.create(0),
            AbstractBlock.Settings.copy(Blocks.IRON_ORE)
        )
    )

    @JvmField
    val DEEPSLATE_RAGINITE_ORE: Block = register(
        "deepslate_raginite_ore",
        ExperienceDroppingBlock(
            ConstantIntProvider.create(0),
            AbstractBlock.Settings.copy(Blocks.DEEPSLATE_IRON_ORE)
        )
    )

    @JvmField
    val CREATIVE_SOURCE: Block = register("creative_source", HTCreativePowerSourceBlock)

    // tier1
    @JvmField
    val RAGI_ALLOY_BLOCK: Block = register("ragi_alloy_block")

    @JvmField
    val RAGI_ALLOY_HULL: Block = register("ragi_alloy_hull")

    @JvmField
    val MANUAL_GRINDER: Block = register("manual_grinder", HTManualGrinderBlock)

    @JvmField
    val WATER_COLLECTOR: Block = register(
        "water_collector",
        HTBlockWithEntity.Builder<HTWaterCollectorBlockEntity>()
            .type(RagiumBlockEntityTypes.WATER_COLLECTOR)
            .ticker(HTWaterCollectorBlockEntity.TICKER)
            .build()
    )

    @JvmField
    val BURNING_BOX: Block = register(
        "burning_box",
        HTBlockWithEntity.Builder<HTBurningBoxBlockEntity>()
            .type(RagiumBlockEntityTypes.BURNING_BOX)
            .ticker(HTBurningBoxBlockEntity.TICKER)
            .build()
    )

    // tier2
    @JvmField
    val RAGI_STEEL_BLOCK: Block = register("ragi_steel_block")

    @JvmField
    val RAGI_STEEL_HULL: Block = register("ragi_steel_hull")

    // tier3
    @JvmField
    val REFINED_RAGI_STEEL_BLOCK: Block = register("refined_ragi_steel_block")

    @JvmField
    val REFINED_RAGI_STEEL_HULL: Block = register("refined_ragi_steel_hull")

    // tier4
    // tier5

    init {
        RagiumBlockEntityTypes.MANUAL_GRINDER.addSupportedBlock(MANUAL_GRINDER)
        RagiumBlockEntityTypes.WATER_COLLECTOR.addSupportedBlock(WATER_COLLECTOR)
        RagiumBlockEntityTypes.BURNING_BOX.addSupportedBlock(BURNING_BOX)

        RagiumBlocks.getFilteredInstances<Block>().forEach(::registerItem)
    }

    private fun <T : Block> register(name: String, block: T): T =
        Registry.register(Registries.BLOCK, Ragium.id(name), block)

    private fun register(name: String, settings: AbstractBlock.Settings = AbstractBlock.Settings.create()): Block =
        register(name, Block(settings))

    private fun registerItem(block: Block, builder: Item.Settings.() -> Unit = {}): Item =
        Registries.BLOCK.getKey(block).map {
            Registry.register(Registries.ITEM, it.value, BlockItem(block, Item.Settings().apply(builder)))
        }.orElseThrow()

    //    Properties    //

    object Properties {
        @JvmField
        val LEVEL_7: IntProperty = IntProperty.of("level", 0, 7)
    }

}