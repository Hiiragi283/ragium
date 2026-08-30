package hiiragi283.lib.data.model

import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
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
}
