package hiiragi283.ragium.client

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import hiiragi283.ragium.api.RagiumConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screen.Screen

object RagiumModMenuPlugin : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = ConfigScreenFactory { screen: Screen ->
        AutoConfig.getConfigScreen(RagiumConfig::class.java, screen).get()
    }
}
