package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.variant.HTVariantKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

enum class HTOreVariant(private val enPattern: String, private val jaPattern: String) : HTVariantKey {
    DEFAULT("%s Ore", "%s鉱石"),
    DEEP("Deepslate %s Ore", "深層%s鉱石"),
    NETHER("Nether %s Ore", "ネザー%s鉱石"),
    END("End %s Ore", "エンド%s鉱石"),
    ;

    val baseStone: Block get() = when (this) {
        DEFAULT -> Blocks.STONE
        DEEP -> Blocks.DEEPSLATE
        NETHER -> Blocks.NETHERRACK
        END -> Blocks.END_STONE
    }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
