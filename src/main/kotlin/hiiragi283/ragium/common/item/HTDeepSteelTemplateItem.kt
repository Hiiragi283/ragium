package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.SmithingTemplateItem

class HTDeepSteelTemplateItem :
    SmithingTemplateItem(
        Component.translatable(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_APPLIES_TO).withStyle(ChatFormatting.BLUE),
        Component.translatable(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_INGREDIENTS).withStyle(ChatFormatting.BLUE),
        Component.translatable(RagiumTranslationKeys.DEEP_STEEL_UPGRADE).withStyle(ChatFormatting.GRAY),
        Component.translatable(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_BASE_SLOT_DESCRIPTION),
        Component.translatable(RagiumTranslationKeys.DEEP_STEEL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION),
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
