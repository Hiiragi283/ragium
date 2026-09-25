package hiiragi283.lib.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureMapping.cubeTop
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

    /**
     * @since 26.1.7
     */
    @JvmField
    val TANK_TEMPLATE: ExtendedModelTemplate = ExtendedModelTemplateBuilder.builder()
        .parent(RagiumAPI.id(HTConstants.BLOCK, "tank_template"))
        .requiredTextureSlot(TextureSlot.TOP)
        .requiredTextureSlot(TextureSlot.SIDE)
        .build()

    /**
     * Hiiragi Seriesで使用される[TexturedModel.Provider]をまとめたクラスです。
     *
     * 参照 : [Minecraft - TexturedModel.Provider][TexturedModel.Provider]
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    data object Providers {
        @JvmField
        val TANK_TEMPLATE: TexturedModel.Provider =
            TexturedModel.createDefault(TextureMapping::cubeTop, HTModelTemplates.TANK_TEMPLATE)
    }
}
