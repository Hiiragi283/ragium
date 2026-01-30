package hiiragi283.ragium.data.client

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.texture.HTTextureProvider
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer

class RagiumTextureProvider(context: HTDataGenContext) : HTTextureProvider(RagiumAPI.MOD_ID, context) {
    override fun gather(output: BiConsumer<ResourceLocation, NativeImage>) {
        material(output)
    }
}
