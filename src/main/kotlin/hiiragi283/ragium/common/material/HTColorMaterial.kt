package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslatedNameProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.common.variant.HTColoredVariant
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import java.awt.Color

enum class HTColorMaterial(val dyeColor: DyeColor, private val enName: String, private val jpName: String) :
    HTMaterialLike,
    HTTranslatedNameProvider {
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

    companion object {
        val VANILLA_TABLE: ImmutableTable<HTColoredVariant, HTColorMaterial, HTDeferredItem<*>> = buildTable {
            for (color: HTColorMaterial in HTColorMaterial.entries) {
                val id: ResourceLocation = vanillaId(color.asMaterialName())
                for (variant: HTColoredVariant in HTColoredVariant.entries) {
                    this[variant, color] = HTDeferredItem<Item>(id.withSuffix("_${variant.variantName()}"))
                }
            }
        }

        @JvmStatic
        fun getColoredItem(variant: HTColoredVariant, color: HTColorMaterial): HTDeferredItem<*> =
            VANILLA_TABLE[variant, color] ?: error("Unknown ${color.asMaterialName()} ${variant.variantName()}")
    }

    val dyeTag: TagKey<Item> = dyeColor.tag
    val dyedTag: TagKey<Item> = dyeColor.dyedTag

    val color: Color = Color(dyeColor.textureDiffuseColor)

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(dyeColor.serializedName)
}
