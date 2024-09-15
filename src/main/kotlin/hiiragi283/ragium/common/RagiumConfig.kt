package hiiragi283.ragium.common

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = Ragium.MOD_ID)
class RagiumConfig : ConfigData {
    @JvmField
    var isHardMode: Boolean = false
}
