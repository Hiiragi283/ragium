package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text

object RagiumItemGroup {
    @JvmField
    val ITEM: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ragium.id("item"))

    @JvmStatic
    fun init() {
        register(ITEM) {
            displayName(Text.translatable(RagiumTranslationKeys.ITEM_GROUP_ITEM))
            icon { RagiumItems.RAGI_ALLOY_INGOT.defaultStack }
            entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
                RagiumItems.REGISTER.forEach(entries::add)
            }
        }
    }

    private inline fun register(key: RegistryKey<ItemGroup>, action: ItemGroup.Builder.() -> Unit) {
        Registry.register(
            Registries.ITEM_GROUP,
            key,
            FabricItemGroup.builder().apply(action).build(),
        )
    }
}
