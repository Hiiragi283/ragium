package hiiragi283.ragium.client.integration.emi.data

import dev.emi.emi.api.stack.EmiIngredient

@JvmRecord
data class HTEmiFluidFuelData(val energyRate: Int, val itemInput: EmiIngredient, val fluidInput: EmiIngredient)
