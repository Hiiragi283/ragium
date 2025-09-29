package hiiragi283.ragium.api.extension

import hiiragi283.ragium.common.variant.HTDecorationVariant
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ModelFile

//    ModelFile    //

fun modelFile(id: ResourceLocation): ModelFile = ModelFile.UncheckedModelFile(id)

val HTDecorationVariant.textureId: ResourceLocation get() = base.blockId
