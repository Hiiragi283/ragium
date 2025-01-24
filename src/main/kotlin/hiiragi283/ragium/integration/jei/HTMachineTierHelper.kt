package hiiragi283.ragium.integration.jei

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import mezz.jei.api.ingredients.IIngredientHelper
import mezz.jei.api.ingredients.IIngredientType
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.resources.ResourceLocation

object HTMachineTierHelper : IIngredientHelper<HTMachineTier> {
    override fun getIngredientType(): IIngredientType<HTMachineTier> = RagiumJEIPlugin.MACHINE_TIER_TYPE

    override fun getDisplayName(ingredient: HTMachineTier): String = ingredient.text.string

    @Suppress("removal")
    @Deprecated("Deprecated in Java")
    override fun getUniqueId(ingredient: HTMachineTier, context: UidContext): String = "machine_tier:${ingredient.serializedName}"

    override fun getResourceLocation(ingredient: HTMachineTier): ResourceLocation = RagiumAPI.id(ingredient.serializedName)

    override fun copyIngredient(ingredient: HTMachineTier): HTMachineTier = ingredient

    override fun getErrorInfo(ingredient: HTMachineTier?): String = ingredient?.text?.string ?: "Unknown Machine Tier"
}
