package hiiragi283.ragium.integration.emi.data

import dev.emi.emi.api.stack.EmiIngredient
import hiiragi283.ragium.api.registry.HTHolderLike
import net.minecraft.resources.ResourceLocation

@JvmRecord
data class HTEmiFluidFuelData(
    private val id: ResourceLocation,
    val energyRate: Int,
    val itemInput: EmiIngredient,
    val fluidInput: EmiIngredient,
) : HTHolderLike {
    override fun getId(): ResourceLocation = id
}
