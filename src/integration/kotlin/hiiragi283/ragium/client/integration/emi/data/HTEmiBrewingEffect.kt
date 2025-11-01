package hiiragi283.ragium.client.integration.emi.data

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack

@JvmRecord
data class HTEmiBrewingEffect(val input: EmiIngredient, val potion: EmiStack)
