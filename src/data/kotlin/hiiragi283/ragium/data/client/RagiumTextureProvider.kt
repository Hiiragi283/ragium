package hiiragi283.ragium.data.client

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.texture.HTTextureProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer

class RagiumTextureProvider(context: HTDataGenContext) : HTTextureProvider(context) {
    override fun gather(output: BiConsumer<ResourceLocation, NativeImage>) {
        material(output, RagiumAPI.MOD_ID, HTConst.BLOCK) { RagiumBlocks.MATERIALS.column(it).keys }
        material(output, RagiumAPI.MOD_ID, HTConst.ITEM) { RagiumItems.MATERIALS.column(it).keys }
    }
}
