package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.HTLanguageType
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.common.Tags

enum class HTOreVariant(
    val pattern: String,
    val stoneTex: String,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    override val tagKey: TagKey<Block>,
) : HTVariantKey.Tagged<Block> {
    STONE("%s_ore", "block/stone", "%s Ore", "%s鉱石", Tags.Blocks.ORES_IN_GROUND_STONE) {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
    },
    DEEP("deepslate_%s_ore", "block/deepslate", "Deepslate %s Ore", "深層%s鉱石", Tags.Blocks.ORES_IN_GROUND_DEEPSLATE) {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.DEEPSLATE)
            .requiresCorrectToolForDrops()
            .strength(4.5f, 3f)
            .sound(SoundType.DEEPSLATE)
    },
    NETHER("nether_%s_ore", "block/netherrack", "Nether %s Ore", "ネザー%s鉱石", Tags.Blocks.ORES_IN_GROUND_NETHERRACK) {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.NETHER)
            .requiresCorrectToolForDrops()
            .strength(3f, 3f)
            .sound(SoundType.NETHER_ORE)
    },
    END("end_%s_ore", "block/end_stone", "End %s Ore", "エンド%s鉱石", RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE) {
        override fun createProperties(): BlockBehaviour.Properties = BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.SAND)
            .requiresCorrectToolForDrops()
            .strength(3f, 9f)
            .sound(SoundType.AMETHYST)
    },
    ;

    abstract fun createProperties(): BlockBehaviour.Properties

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
