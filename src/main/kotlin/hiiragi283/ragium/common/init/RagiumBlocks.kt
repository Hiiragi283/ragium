package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent
import net.minecraft.block.Block

object RagiumBlocks {
    //    Creatives    //

    @JvmField
    val CREATIVE_CRATE: HTBlockContent = HTContent.ofBlock("creative_crate")

    @JvmField
    val CREATIVE_DRUM: HTBlockContent = HTContent.ofBlock("creative_drum")

    @JvmField
    val CREATIVE_EXPORTER: HTBlockContent = HTContent.ofBlock("creative_exporter")

    @JvmField
    val CREATIVE_SOURCE: HTBlockContent = HTContent.ofBlock("creative_source")

    @JvmField
    val CREATIVES: List<HTBlockContent> = listOf(
        CREATIVE_CRATE,
        CREATIVE_DRUM,
        CREATIVE_EXPORTER,
        CREATIVE_SOURCE,
    )

    //    Minerals    //
    @JvmField
    val MUTATED_SOIL: HTBlockContent = HTContent.ofBlock("mutated_soil")

    @JvmField
    val POROUS_NETHERRACK: HTBlockContent = HTContent.ofBlock("porous_netherrack")

    @JvmField
    val NATURAL: List<HTBlockContent> = listOf(
        MUTATED_SOIL,
        POROUS_NETHERRACK,
    )

    //    Buildings    //

    enum class Stones : HTBlockContent {
        ASPHALT,
        POLISHED_ASPHALT,
        GYPSUM,
        POLISHED_GYPSUM,
        SLATE,
        POLISHED_SLATE,
        ;

        override val delegated: HTContent<Block> = HTContent.ofBlock(name.lowercase())
    }

    enum class Slabs : HTBlockContent {
        ASPHALT,
        POLISHED_ASPHALT,
        GYPSUM,
        POLISHED_GYPSUM,
        SLATE,
        POLISHED_SLATE,
        ;

        val baseStone: Stones
            get() = Stones.entries.first { it.name == this.name }

        override val delegated: HTContent<Block> = HTContent.ofBlock("${name.lowercase()}_slab")
    }

    enum class Stairs : HTBlockContent {
        ASPHALT,
        POLISHED_ASPHALT,
        GYPSUM,
        POLISHED_GYPSUM,
        SLATE,
        POLISHED_SLATE,
        ;

        val baseStone: Stones
            get() = Stones.entries.first { it.name == this.name }

        override val delegated: HTContent<Block> = HTContent.ofBlock("${name.lowercase()}_stairs")
    }

    @JvmField
    val WHITE_LINE: HTBlockContent = HTContent.ofBlock("white_line")

    @JvmField
    val T_WHITE_LINE: HTBlockContent = HTContent.ofBlock("t_white_line")

    @JvmField
    val CROSS_WHITE_LINE: HTBlockContent = HTContent.ofBlock("cross_white_line")

    @JvmField
    val STEEL_GLASS: HTBlockContent = HTContent.ofBlock("steel_glass")

    @JvmField
    val RAGIUM_GLASS: HTBlockContent = HTContent.ofBlock("ragium_glass")

    @JvmField
    val BUILDINGS: List<HTBlockContent> = listOf(
        WHITE_LINE,
        T_WHITE_LINE,
        CROSS_WHITE_LINE,
        STEEL_GLASS,
        RAGIUM_GLASS,
    )

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: HTBlockContent = HTContent.ofBlock("sponge_cake")

    @JvmField
    val SWEET_BERRIES_CAKE: HTBlockContent = HTContent.ofBlock("sweet_berries_cake")

    @JvmField
    val FOODS: List<HTBlockContent> = listOf(
        SPONGE_CAKE,
        SWEET_BERRIES_CAKE,
    )

    //    Mechanics    //

    @JvmField
    val AUTO_ILLUMINATOR: HTBlockContent = HTContent.ofBlock("auto_illuminator")

    @JvmField
    val EXTENDED_PROCESSOR: HTBlockContent = HTContent.ofBlock("extended_processor")

    @JvmField
    val MANUAL_FORGE: HTBlockContent = HTContent.ofBlock("manual_forge")

    @JvmField
    val MANUAL_GRINDER: HTBlockContent = HTContent.ofBlock("manual_grinder")

    @JvmField
    val MANUAL_MIXER: HTBlockContent = HTContent.ofBlock("manual_mixer")

    @JvmField
    val NETWORK_INTERFACE: HTBlockContent = HTContent.ofBlock("network_interface")

    @JvmField
    val OPEN_CRATE: HTBlockContent = HTContent.ofBlock("open_crate")

    @JvmField
    val TELEPORT_ANCHOR: HTBlockContent = HTContent.ofBlock("teleport_anchor")

    @JvmField
    val TRASH_BOX: HTBlockContent = HTContent.ofBlock("trash_box")

    @JvmField
    val MECHANICS: List<HTBlockContent> = listOf(
        // colored
        EXTENDED_PROCESSOR, // red
        AUTO_ILLUMINATOR, // yellow
        OPEN_CRATE, // green
        TELEPORT_ANCHOR, // blue
        TRASH_BOX, // gray
        NETWORK_INTERFACE, // white
        // manual machines
        MANUAL_FORGE,
        MANUAL_GRINDER,
        MANUAL_MIXER,
    )

    //    Misc    //

    @JvmField
    val BACKPACK_INTERFACE: HTBlockContent = HTContent.ofBlock("backpack_interface")

    @JvmField
    val ENCHANTMENT_BOOKSHELF: HTBlockContent = HTContent.ofBlock("enchantment_bookshelf")

    @JvmField
    val ITEM_DISPLAY: HTBlockContent = HTContent.ofBlock("item_display")

    @JvmField
    val ROPE: HTBlockContent = HTContent.ofBlock("rope")

    @JvmField
    val SHAFT: HTBlockContent = HTContent.ofBlock("shaft")

    @JvmField
    val INFESTING: HTBlockContent = HTContent.ofBlock("infesting")

    @JvmField
    val MISC: List<HTBlockContent> = listOf(
        BACKPACK_INTERFACE,
        ENCHANTMENT_BOOKSHELF,
        ITEM_DISPLAY,
        ROPE,
        SHAFT,
    )
}
