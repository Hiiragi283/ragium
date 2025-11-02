package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.text.HTTranslation
import net.minecraft.ChatFormatting
import net.minecraft.world.item.SmithingTemplateItem

class HTSmithingTemplateItem(
    appliesTo: HTTranslation,
    ingredients: HTTranslation,
    upgradeDescription: HTTranslation,
    baseSlotDescription: HTTranslation,
    additionsSlotDescription: HTTranslation,
) : SmithingTemplateItem(
        appliesTo.getColoredComponent(ChatFormatting.BLUE),
        ingredients.getColoredComponent(ChatFormatting.BLUE),
        upgradeDescription.getColoredComponent(ChatFormatting.GRAY),
        baseSlotDescription.getComponent(),
        additionsSlotDescription.getComponent(),
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
