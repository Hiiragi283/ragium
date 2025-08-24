package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.tool.HTToolVariant
import hiiragi283.ragium.common.item.HTHammerItem
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object HTHammerToolVariant : HTToolVariant {
    override fun registerItem(register: DeferredRegister.Items, material: HTMaterialType, tier: Tier): DeferredItem<*> =
        register.registerItem(
            "${material.serializedName}_hammer",
            { prop: Item.Properties -> HTHammerItem(tier, prop) },
            Item.Properties().attributes(DiggerItem.createAttributes(tier, 1f, -2.8f)),
        )

    override fun getParentId(path: String): ResourceLocation = RagiumAPI.id(path)

    override val tagKey: TagKey<Item> = RagiumModTags.Items.TOOLS_HAMMER

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "%s Hammer"
        HTLanguageType.JA_JP -> "%sのハンマー"
    }.replace("%s", value)

    override fun getSerializedName(): String = "hammer"
}
