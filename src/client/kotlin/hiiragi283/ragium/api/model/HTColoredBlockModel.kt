package hiiragi283.ragium.api.model

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite

@Environment(EnvType.CLIENT)
abstract class HTColoredBlockModel : HTBakedModel {
    final override lateinit var sprite: Sprite

    final override fun getTransformation(): ModelTransformation = ModelHelper.MODEL_TRANSFORM_BLOCK
}
