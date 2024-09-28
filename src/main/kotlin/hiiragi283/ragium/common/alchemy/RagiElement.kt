package hiiragi283.ragium.common.alchemy

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.block.HTBuddingCrystalBlock
import hiiragi283.ragium.common.init.RagiumMachineConditions
import hiiragi283.ragium.common.util.HTTranslationProvider
import hiiragi283.ragium.common.util.blockSettings
import hiiragi283.ragium.common.util.element
import hiiragi283.ragium.common.util.itemSettings
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.block.AbstractBlock
import net.minecraft.block.AmethystClusterBlock
import net.minecraft.block.MapColor
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.Item
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import java.util.function.IntFunction

enum class RagiElement(
    private val suitableBiome: TagKey<Biome>,
    override val enName: String,
    override val jaName: String,
    mapColor: MapColor,
    private val condition: (World, BlockPos) -> Boolean = RagiumMachineConditions.NONE,
) : HTTranslationProvider,
    StringIdentifiable {
    RAGIUM(ConventionalBiomeTags.IS_NETHER, "Ragium", "ラギウム", MapColor.RED, RagiumMachineConditions.HEAT),
    RIGIUM(ConventionalBiomeTags.IS_WASTELAND, "Rigium", "リギウム", MapColor.YELLOW),
    RUGIUM(ConventionalBiomeTags.IS_JUNGLE, "Rugium", "ルギウム", MapColor.GREEN),
    REGIUM(ConventionalBiomeTags.IS_OCEAN, "Regium", "レギウム", MapColor.BLUE),
    ROGIUM(ConventionalBiomeTags.IS_END, "Rogium", "ロギウム", MapColor.PURPLE),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<RagiElement> = StringIdentifiable.createCodec(RagiElement::values)

        @JvmField
        val INT_FUNCTION: IntFunction<RagiElement> =
            ValueLists.createIdToValueFunction(
                RagiElement::ordinal,
                RagiElement.entries.toTypedArray(),
                ValueLists.OutOfBoundsHandling.WRAP,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, RagiElement> =
            PacketCodecs.indexed(INT_FUNCTION, RagiElement::ordinal)
    }

    private val settings: AbstractBlock.Settings =
        blockSettings().mapColor(mapColor).strength(1.5f).sounds(BlockSoundGroup.AMETHYST_BLOCK)
    val buddingBlock: HTBuddingCrystalBlock = HTBuddingCrystalBlock(this, settings.ticksRandomly().requiresTool())
    val clusterBlock: AmethystClusterBlock = AmethystClusterBlock(
        7.0f,
        3.0f,
        settings
            .solid()
            .nonOpaque()
            .luminance { 5 }
            .pistonBehavior(PistonBehavior.DESTROY),
    )
    val dustItem = Item(itemSettings().element(this))

    val translationKey = "element.${asString()}"
    val text: Text = Text.translatable(translationKey)

    fun canGrow(world: World, pos: BlockPos): Boolean = world.getBiome(pos).isIn(suitableBiome) || condition(world, pos)

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
