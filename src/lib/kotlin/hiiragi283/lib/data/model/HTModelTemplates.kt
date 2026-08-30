package hiiragi283.lib.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.ragium.api.RagiumAPI
import java.util.Optional
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder

/**
 * Hiiragi Seriesで使用される[ExtendedModelTemplate]をまとめたクラスです。
 *
 * 参照 : [Minecraft - TexturedModel][TexturedModel]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTModelTemplates {
    @JvmField
    val FLUID_BLOCK: ExtendedModelTemplate = ExtendedModelTemplateBuilder.builder()
        .requiredTextureSlot(TextureSlot.PARTICLE)
        .build()

    /**
     * 二つのレイヤーを持つブロックのモデル
     */
    @JvmField
    val LAYERED: ModelTemplate = block(RagiumAPI.id(HTConstants.BLOCK, "layered"), TextureSlot.LAYER0, TextureSlot.LAYER1)

    @JvmStatic
    private fun block(modelId: Identifier, vararg requiredSlots: TextureSlot): ModelTemplate = ModelTemplate(Optional.of(modelId), Optional.empty(), *requiredSlots)

    @JvmStatic
    private fun block(modelId: Identifier, suffix: String, vararg requiredSlots: TextureSlot): ModelTemplate = ModelTemplate(Optional.of(modelId), Optional.of(suffix), *requiredSlots)
}
