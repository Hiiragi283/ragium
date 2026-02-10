package hiiragi283.ragium.api.data.model

import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import java.util.Optional

/**
 * @see hiiragi283.core.api.data.model.HTModelTemplates
 */
object RagiumModelTemplates {
    @JvmStatic
    private fun block(modelId: ResourceLocation, vararg requiredSlots: TextureSlot): ModelTemplate =
        ModelTemplate(Optional.of(modelId), Optional.empty(), *requiredSlots)

    @JvmStatic
    private fun block(modelId: ResourceLocation, suffix: String, vararg requiredSlots: TextureSlot): ModelTemplate =
        ModelTemplate(Optional.of(modelId), Optional.of(suffix), *requiredSlots)
}
