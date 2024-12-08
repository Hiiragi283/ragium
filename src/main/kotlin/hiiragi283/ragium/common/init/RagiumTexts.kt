package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.extension.asText
import hiiragi283.ragium.api.extension.name
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.text.MutableText
import net.minecraft.text.Text

object RagiumTexts {
    @JvmStatic
    fun fluidFilter(entryList: RegistryEntryList<Fluid>): MutableText = Text.translatable(
        RagiumTranslationKeys.EXPORTER_FLUID_FILTER,
        entryList.asText(Fluid::name),
    )

    @JvmStatic
    fun itemFilter(entryList: RegistryEntryList<Item>): MutableText = Text.translatable(
        RagiumTranslationKeys.EXPORTER_ITEM_FILTER,
        entryList.asText(Item::getName),
    )
}
