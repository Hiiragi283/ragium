package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.text.Text

fun interface HTMachineTooltipAppender {
    companion object {
        @JvmField
        val DEFAULT_PROCESSOR =
            HTMachineTooltipAppender { consumer: (Text) -> Unit, key: HTMachineKey, tier: HTMachineTier ->
            }
    }

    fun appendTooltip(consumer: (Text) -> Unit, key: HTMachineKey, tier: HTMachineTier)
}
