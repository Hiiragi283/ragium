package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.extension.longText
import hiiragi283.ragium.api.machine.HTMachine
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.text.Text
import net.minecraft.util.Formatting

fun interface HTMachineTooltipAppender {
    companion object {
        @JvmField
        val DEFAULT_PROCESSOR =
            HTMachineTooltipAppender { consumer: (Text) -> Unit, machine: HTMachine, tier: HTMachineTier ->
                val sizeType: HTMachineType.Size =
                    machine.asProperties()[HTMachinePropertyKeys.RECIPE_SIZE] ?: return@HTMachineTooltipAppender
                consumer(
                    Text
                        .translatable(
                            RagiumTranslationKeys.MACHINE_SLOT_COUNTS,
                            intText(sizeType.invSize / 2).formatted(Formatting.YELLOW),
                        ).formatted(Formatting.GRAY),
                )
                consumer(
                    Text
                        .translatable(
                            RagiumTranslationKeys.MACHINE_TANK_CAPACITY,
                            longText(tier.bucketUnit).formatted(Formatting.YELLOW),
                        ).formatted(Formatting.GRAY),
                )
            }
    }

    fun appendTooltip(consumer: (Text) -> Unit, machine: HTMachine, tier: HTMachineTier)
}
