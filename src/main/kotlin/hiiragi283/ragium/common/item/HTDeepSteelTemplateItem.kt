package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.text.RagiumTranslation
import net.minecraft.ChatFormatting
import net.minecraft.world.item.SmithingTemplateItem

class HTDeepSteelTemplateItem :
    SmithingTemplateItem(
        RagiumTranslation.DEEP_STEEL_UPGRADE_APPLIES_TO.getColoredComponent(ChatFormatting.BLUE),
        RagiumTranslation.DEEP_STEEL_UPGRADE_INGREDIENTS.getColoredComponent(ChatFormatting.BLUE),
        RagiumTranslation.DEEP_STEEL_UPGRADE.getColoredComponent(ChatFormatting.GRAY),
        RagiumTranslation.DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION.getComponent(),
        RagiumTranslation.DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION.getComponent(),
        listOf(
            "item/empty_armor_slot_helmet",
            "item/empty_armor_slot_chestplate",
            "item/empty_armor_slot_leggings",
            "item/empty_armor_slot_boots",
            "item/empty_slot_hoe",
            "item/empty_slot_axe",
            "item/empty_slot_sword",
            "item/empty_slot_shovel",
            "item/empty_slot_pickaxe",
        ).map(::vanillaId),
        listOf(vanillaId("item/empty_slot_ingot")),
    )
