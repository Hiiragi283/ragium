package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialType
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item

enum class HTColorMaterial(val color: DyeColor, private val enName: String, private val jpName: String) : HTMaterialType.Translatable {
    WHITE(DyeColor.WHITE, "White", "白色"),
    ORANGE(DyeColor.ORANGE, "Orange", "橙色"),
    MAGENTA(DyeColor.MAGENTA, "Magenta", "赤紫色"),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE, "Light Blue", "空色"),
    YELLOW(DyeColor.YELLOW, "Yellow", "黄色"),
    LIME(DyeColor.LIME, "Lime", "黄緑色"),
    PINK(DyeColor.PINK, "Pink", "桃色"),
    GRAY(DyeColor.GRAY, "Gray", "灰色"),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY, "Light Gray", "薄灰色"),
    CYAN(DyeColor.CYAN, "Cyan", "青緑色"),
    PURPLE(DyeColor.PURPLE, "Purple", "紫色"),
    BLUE(DyeColor.BLUE, "Blue", "青色"),
    BROWN(DyeColor.BROWN, "Brown", "茶色"),
    GREEN(DyeColor.GREEN, "Green", "緑色"),
    RED(DyeColor.RED, "Red", "赤色"),
    BLACK(DyeColor.BLACK, "Black", "黒色"),
    ;

    val dyeTag: TagKey<Item> = color.tag
    val dyedTag: TagKey<Item> = color.dyedTag

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun getSerializedName(): String = color.serializedName
}
