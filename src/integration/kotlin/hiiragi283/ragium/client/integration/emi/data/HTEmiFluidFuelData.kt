package hiiragi283.ragium.client.integration.emi.data

import dev.emi.emi.api.stack.EmiStack

@JvmRecord
data class HTEmiFluidFuelData(val input: EmiStack, val time: Int)
