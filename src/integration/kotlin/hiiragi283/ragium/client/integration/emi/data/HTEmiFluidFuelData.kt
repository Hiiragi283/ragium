package hiiragi283.ragium.client.integration.emi.data

import dev.emi.emi.api.stack.EmiIngredient
import hiiragi283.ragium.api.data.map.HTFluidFuelData

@JvmRecord
data class HTEmiFluidFuelData(
    val energyRate: Int,
    val fuelData: HTFluidFuelData,
    val itemInput: EmiIngredient,
    val fluidInput: EmiIngredient,
)
