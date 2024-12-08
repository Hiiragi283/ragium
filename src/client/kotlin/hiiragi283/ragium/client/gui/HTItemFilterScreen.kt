package hiiragi283.ragium.client.gui

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryOps
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class HTItemFilterScreen(val stack: ItemStack) : HTFilterScreen(stack.name) {
    companion object {
        @JvmStatic
        fun openScreen(stack: ItemStack, world: World): Boolean {
            if (stack.isIn(RagiumItemTags.ITEM_EXPORTER_FILTERS)) {
                val screen = HTItemFilterScreen(stack)
                MinecraftClient.getInstance().setScreen(screen)
                val filter: RegistryEntryList<Item> =
                    stack.getOrDefault(RagiumComponentTypes.ITEM_FILTER, RegistryEntryList.empty())
                val registryOps: RegistryOps<NbtElement> = world.registryManager.getOps(NbtOps.INSTANCE)
                ITEM_CODEC
                    .encodeStart(registryOps, filter)
                    .ifSuccess { nbt: NbtElement ->
                        screen.textField.text = nbt.asString()
                    }
                return true
            }
            return false
        }
    }

    override fun applyChange() {
        val client: MinecraftClient = client ?: return
        val registryOps: RegistryOps<NbtElement> = client.world?.registryManager?.getOps(NbtOps.INSTANCE) ?: return
        ITEM_CODEC
            .parse(registryOps, convertText())
            .ifSuccess { entryList: RegistryEntryList<Item> ->
                stack.set(RagiumComponentTypes.ITEM_FILTER, entryList)
                client.player?.sendMessage(Text.literal("Applied Change!").formatted(Formatting.GREEN))
            }.ifError { error: DataResult.Error<RegistryEntryList<Item>> ->
                client.player?.sendMessage(Text.literal(error.message()).formatted(Formatting.RED))
            }
    }
}
