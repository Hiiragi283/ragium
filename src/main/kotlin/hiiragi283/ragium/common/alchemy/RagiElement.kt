package hiiragi283.ragium.common.alchemy

import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import com.mojang.serialization.Codec
import hiiragi283.ragium.common.block.HTBuddingCrystalBlock
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.util.blockSettings
import hiiragi283.ragium.common.util.disableTooltips
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
    mapColor: MapColor,
    val color: Int,
    private val condition: (World, BlockPos) -> Boolean = HTMachineTier.NONE.condition,
) : StringIdentifiable {
    RAGIUM(ConventionalBiomeTags.IS_NETHER, MapColor.RED, 0xffcfcc, HTMachineTier.HEAT.condition),
    RIGIUM(ConventionalBiomeTags.IS_WASTELAND, MapColor.YELLOW, 0xf4ffcf),
    RUGIUM(ConventionalBiomeTags.IS_JUNGLE, MapColor.GREEN, 0xcfffcc),
    REGIUM(ConventionalBiomeTags.IS_OCEAN, MapColor.BLUE, 0xcfdaff),
    ROGIUM(ConventionalBiomeTags.IS_END, MapColor.PURPLE, 0xffcff4),
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

        @JvmField
        val TRANSLATION_TABLE: Table<RagiElement, HTLangType, String> =
            ImmutableTable
                .builder<RagiElement, HTLangType, String>()
                .put(RAGIUM, HTLangType.EN_US, "Ragium")
                .put(RIGIUM, HTLangType.EN_US, "Rigium")
                .put(RUGIUM, HTLangType.EN_US, "Rugium")
                .put(REGIUM, HTLangType.EN_US, "Regium")
                .put(ROGIUM, HTLangType.EN_US, "Rogium")
                .put(RAGIUM, HTLangType.JA_JP, "ラギウム")
                .put(RIGIUM, HTLangType.JA_JP, "リギウム")
                .put(RUGIUM, HTLangType.JA_JP, "ルギウム")
                .put(REGIUM, HTLangType.JA_JP, "レギウム")
                .put(ROGIUM, HTLangType.JA_JP, "ロギウム")
                .build()
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
    val dustItem = Item(itemSettings().element(this).disableTooltips())

    val translationKey = "element.${asString()}"
    val text: Text = Text.translatable(translationKey)

    fun getTranslatedName(type: HTLangType): String = TRANSLATION_TABLE.get(this, type)!!

    fun canGrow(world: World, pos: BlockPos): Boolean = world.getBiome(pos).isIn(suitableBiome) || condition(world, pos)

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()

    //    Condition    //

    private object Condition {
        @JvmField
        val RAGIUM: (World, BlockPos) -> Boolean = HTMachineTier.HEAT.condition
    }
}
