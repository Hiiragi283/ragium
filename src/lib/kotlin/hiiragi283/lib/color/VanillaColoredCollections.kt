package hiiragi283.lib.color

import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import net.minecraft.resources.Identifier

/**
 * バニラの色付きコンテンツ向けに[HTColoredCollection]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
data object VanillaColoredCollections {
    @JvmField
    val BANNER: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_banner")

    @JvmField
    val BED: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_bed")

    @JvmField
    val CANDLE: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_candle")

    @JvmField
    val CARPET: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_carpet")

    @JvmField
    val CONCRETE: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_concrete")

    @JvmField
    val CONCRETE_POWDER: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_concrete_powder")

    @JvmField
    val DYE: HTColoredCollection<HTSimpleDeferredItem> = item("%s_dye")

    @JvmField
    val GLAZED_TERRACOTTA: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_glazed_terracotta")

    @JvmField
    val SHULKER_BOX: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_shulker_box")

    @JvmField
    val STAINED_GLASS: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_stained_glass")

    @JvmField
    val STAINED_GLASS_PANE: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_stained_glass_pane")

    @JvmField
    val TERRACOTTA: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_terracotta")

    @JvmField
    val WOOL: HTColoredCollection<HTSimpleDeferredBlockAndItem> = block("%s_wool")

    @JvmStatic
    private fun block(path: String): HTColoredCollection<HTSimpleDeferredBlockAndItem> = create(path, ::HTSimpleDeferredBlockAndItem)

    @JvmStatic
    private fun item(path: String): HTColoredCollection<HTSimpleDeferredItem> = create(path, ::HTSimpleDeferredItem)

    @JvmStatic
    private inline fun <T> create(path: String, transform: (Identifier) -> T): HTColoredCollection<T> = HTColoredCollection { transform(vanillaId(path.replace("%s", it.serializedName))) }
}
