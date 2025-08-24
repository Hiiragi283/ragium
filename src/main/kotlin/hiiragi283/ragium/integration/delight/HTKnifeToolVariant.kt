package hiiragi283.ragium.integration.delight

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.tool.HTToolVariant
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import vectorwing.farmersdelight.common.item.KnifeItem
import vectorwing.farmersdelight.common.registry.ModItems
import vectorwing.farmersdelight.common.tag.ModTags

object HTKnifeToolVariant : HTToolVariant {
    override fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<KnifeItem> =
        register.register("${material.serializedName}_knife") { _: ResourceLocation ->
            KnifeItem(
                tier,
                ModItems.knifeItem(tier),
            )
        }

    override fun getParentId(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(RagiumConst.FARMERS_DELIGHT, path)

    override val tagKey: TagKey<Item> = ModTags.KNIVES

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "%s Knife"
        HTLanguageType.JA_JP -> "%sのナイフ"
    }.replace("%s", value)

    override fun getSerializedName(): String = "knife"
}
