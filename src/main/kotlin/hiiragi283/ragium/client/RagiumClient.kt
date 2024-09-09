package hiiragi283.ragium.client

import hiiragi283.ragium.client.gui.HTMachineScreen
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.screen.ingame.HandledScreens

object RagiumClient : ClientModInitializer {
    override fun onInitializeClient() {
        HandledScreens.register(HTMachineType.SCREEN_HANDLER_TYPE, ::HTMachineScreen)

        Ragium.log { info("Ragium-Client initialized!") }
    }
}