package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.common.text.HTSmithingTranslation
import net.minecraft.ChatFormatting
import net.minecraft.world.item.SmithingTemplateItem

class HTSmithingTemplateItem(translation: HTSmithingTranslation) :
    SmithingTemplateItem(
        translation.appliesTo.translateColored(ChatFormatting.BLUE),
        translation.ingredients.translateColored(ChatFormatting.BLUE),
        translation.upgradeDescription.translateColored(ChatFormatting.GRAY),
        translation.baseSlotDescription.translate(),
        translation.additionsSlotDescription.translate(),
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
        listOf(vanillaId("item", "empty_slot_ingot")),
    )
