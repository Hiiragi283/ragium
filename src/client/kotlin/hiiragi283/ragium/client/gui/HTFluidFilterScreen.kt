package hiiragi283.ragium.client.gui

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.mojang.serialization.DataResult
import com.mojang.serialization.JsonOps
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.client.MinecraftClient
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.registry.RegistryOps
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class HTFluidFilterScreen(val stack: ItemStack) : HTFilterScreen(stack.name) {
    companion object {
        @JvmStatic
        fun openScreen(stack: ItemStack, world: World): Boolean {
            if (stack.isIn(RagiumItemTags.FLUID_EXPORTER_FILTERS)) {
                val screen = HTFluidFilterScreen(stack)
                MinecraftClient.getInstance().setScreen(screen)
                val filter: RegistryEntryList<Fluid> =
                    stack.getOrDefault(RagiumComponentTypes.FLUID_FILTER, RegistryEntryList.empty())
                val registryOps: RegistryOps<JsonElement> = world.registryManager.getOps(JsonOps.INSTANCE)
                FLUID_CODEC
                    .encodeStart(registryOps, filter)
                    .ifSuccess { json: JsonElement -> screen.textField.text = json.toString() }
                return true
            }
            return false
        }
    }

    override fun applyChange() {
        val client: MinecraftClient = client ?: return
        val registryOps: RegistryOps<JsonElement> = client.world?.registryManager?.getOps(JsonOps.INSTANCE) ?: return
        FLUID_CODEC
            .parse(registryOps, JsonParser.parseString(textField.text))
            .ifSuccess { entryList: RegistryEntryList<Fluid> ->
                stack.set(RagiumComponentTypes.FLUID_FILTER, entryList)
                client.player?.sendMessage(Text.literal("Applied Change!").formatted(Formatting.GREEN))
            }.ifError { error: DataResult.Error<RegistryEntryList<Fluid>> ->
                client.player?.sendMessage(Text.literal(error.message()).formatted(Formatting.RED))
            }
    }
}
