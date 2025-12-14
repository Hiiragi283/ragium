package hiiragi283.ragium.client.integration.emi.data

import dev.emi.emi.api.stack.EmiStack

@JvmRecord
data class HTRockGenerationEmiData(val water: EmiStack, val lava: EmiStack, val output: EmiStack)
