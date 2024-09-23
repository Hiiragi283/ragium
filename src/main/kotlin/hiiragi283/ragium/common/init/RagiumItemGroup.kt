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
    val ITEM_KEY: RegistryKey<ItemGroup> = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ragium.id("item"))

    @JvmField
    val ITEM: ItemGroup = register(ITEM_KEY) {
        displayName(Text.translatable("itemGroup.ragium.item"))
        icon {
            RagiumItems.Ingots.RAGI_STEEL
                .asItem()
                .defaultStack
        }
        entries { _: ItemGroup.DisplayContext, entries: ItemGroup.Entries ->
            RagiumItems.REGISTER.forEach(entries::add)
        }
    }

    private inline fun register(key: RegistryKey<ItemGroup>, action: ItemGroup.Builder.() -> Unit): ItemGroup =
        Registry.register(Registries.ITEM_GROUP, key, FabricItemGroup.builder().apply(action).build())
}
