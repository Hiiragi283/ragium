package hiiragi283.ragium.client.integration.emi.data

import dev.emi.emi.api.stack.EmiIngredient

@JvmRecord
data class HTEmiFluidFuelData(val input: EmiIngredient, val time: Int)
