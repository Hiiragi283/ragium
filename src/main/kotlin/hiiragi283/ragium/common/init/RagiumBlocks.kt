package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTCreativePowerSourceBlock
import hiiragi283.ragium.common.block.HTManualGrinderBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.ExperienceDroppingBlock
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
    val MANUAL_GRINDER: Block = register("manual_grinder", HTManualGrinderBlock)

    private fun <T : Block> register(name: String, block: T): T =
        Registry.register(Registries.BLOCK, Ragium.id(name), block)

    private fun register(name: String, settings: AbstractBlock.Settings = AbstractBlock.Settings.create()): Block =
        register(name, Block(settings))

    //    Properties    //

    object Properties {
        @JvmField
        val LEVEL_7: IntProperty = IntProperty.of("level", 0, 7)
    }

}