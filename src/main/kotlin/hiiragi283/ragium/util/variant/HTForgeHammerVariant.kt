package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.common.item.HTForgeHammerItem
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers

enum class HTForgeHammerVariant(val tier: Tier, private val enUsPattern: String, private val jaJpPattern: String) : HTVariantKey {
    IRON(Tiers.IRON, "Iron %s", "鉄の%s"),
    DIAMOND(Tiers.DIAMOND, "Diamond %s", "ダイヤモンドの%s"),
    NETHERITE(Tiers.NETHERITE, "Netherite %s", "ネザライトの%s"),
    RAGI_ALLOY(RagiumToolTiers.RAGI_ALLOY, "Ragi-Alloy %s", "ラギ合金の%s"),
    AZURE_STEEL(RagiumToolTiers.AZURE_STEEL, "Azure Steel %s", "紺鉄の%s"),
    DEEP_STEEL(RagiumToolTiers.DEEP_STEEL, "Deep Steel %s", "深層鋼の%s"), ;

    val factory: (Item.Properties) -> Item = { prop: Item.Properties -> HTForgeHammerItem(tier, prop) }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
