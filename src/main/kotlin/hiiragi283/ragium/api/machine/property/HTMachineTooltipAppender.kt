package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.text.Text

fun interface HTMachineTooltipAppender {
    companion object {
        @JvmField
        val DEFAULT_PROCESSOR =
            HTMachineTooltipAppender { consumer: (Text) -> Unit, machine: HTMachine, tier: HTMachineTier ->
            }
    }

    fun appendTooltip(consumer: (Text) -> Unit, machine: HTMachine, tier: HTMachineTier)
}
