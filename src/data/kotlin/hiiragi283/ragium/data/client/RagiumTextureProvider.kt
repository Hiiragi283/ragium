package hiiragi283.ragium.data.client

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.texture.HTTextureProvider
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer

class RagiumTextureProvider(context: HTDataGenContext) : HTTextureProvider(context) {
    override fun gather(output: BiConsumer<ResourceLocation, NativeImage>) {
        material(output, HTConst.ITEM) { RagiumItems.MATERIALS.column(it).keys }
    }

    private fun material(
        output: BiConsumer<ResourceLocation, NativeImage>,
        pathPrefix: String,
        transform: (RagiumMaterial) -> Set<HTMaterialPrefix>,
    ) {
        for (material: RagiumMaterial in RagiumMaterial.entries) {
            for (prefix: HTMaterialPrefix in transform(material)) {
                val templateImage: NativeImage = material.getTemplateId(prefix)?.let(::getTexture) ?: continue
                val image: NativeImage = copyFrom(templateImage)

                for ((index: Int, pixels: Collection<Pair<Int, Int>>) in createTemplate(templateImage).map) {
                    for ((x: Int, y: Int) in pixels) {
                        image.setPixelRGBA(x, y, argbToFromABGR(material.colorPalette[index].rgb))
                    }
                }
                output.accept(RagiumAPI.id(pathPrefix, prefix.asPrefixName(), material.asMaterialName()), image)
            }
        }
    }
}
