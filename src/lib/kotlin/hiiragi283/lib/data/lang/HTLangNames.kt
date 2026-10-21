package hiiragi283.lib.data.lang

import net.minecraft.world.level.block.ColorCollection

/**
 * @author Hiiragi Tsubasa
 * @since 26.3.0
 */
data object HTLangNames {
    @JvmField
    val COLOR: ColorCollection<HTLangName> = ColorCollection(
        HTLangName("White", "白色"),
        HTLangName("Orange", "橙色"),
        HTLangName("Magenta", "赤紫色"),
        HTLangName("Light Blue", "空色"),
        HTLangName("Yellow", "黄色"),
        HTLangName("Lime", "黄緑色"),
        HTLangName("Pink", "桃色"),
        HTLangName("Gray", "灰色"),
        HTLangName("Light Gray", "薄灰色"),
        HTLangName("Cyan", "青緑色"),
        HTLangName("Purple", "紫色"),
        HTLangName("Blue", "青色"),
        HTLangName("Brown", "茶色"),
        HTLangName("Green", "緑色"),
        HTLangName("Red", "赤色"),
        HTLangName("Black", "黒色")
    )
}
