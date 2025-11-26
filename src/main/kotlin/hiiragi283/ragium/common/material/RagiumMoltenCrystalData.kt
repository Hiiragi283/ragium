package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTBasicFluidContent
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import java.awt.Color

enum class RagiumMoltenCrystalData(val color: Color, private val enName: String, private val jaName: String) :
    HTMaterialLike.Translatable {
    CRIMSON(Color(0x660000), "Crimson Blood", "深紅の血液"),
    WARPED(Color(0x006666), "Dew of the Warp", "歪みの雫"),
    ELDRITCH(Color(0x660066), "Eldritch Flux", "異質な流動体"),
    ;

    val base: TagKey<Item>?
        get() = when (this) {
            CRIMSON -> ItemTags.CRIMSON_STEMS
            WARPED -> ItemTags.WARPED_STEMS
            ELDRITCH -> null
        }

    val sap: HTBasicFluidContent?
        get() = when (this) {
            CRIMSON -> RagiumFluidContents.CRIMSON_SAP
            WARPED -> RagiumFluidContents.WARPED_SAP
            ELDRITCH -> null
        }

    val molten: HTBasicFluidContent
        get() = when (this) {
            CRIMSON -> RagiumFluidContents.CRIMSON_BLOOD
            WARPED -> RagiumFluidContents.DEW_OF_THE_WARP
            ELDRITCH -> RagiumFluidContents.ELDRITCH_FLUX
        }

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }

    override fun asMaterialKey(): HTMaterialKey = when (this) {
        CRIMSON -> RagiumMaterialKeys.CRIMSON_CRYSTAL
        WARPED -> RagiumMaterialKeys.WARPED_CRYSTAL
        ELDRITCH -> RagiumMaterialKeys.ELDRITCH_PEARL
    }
}
