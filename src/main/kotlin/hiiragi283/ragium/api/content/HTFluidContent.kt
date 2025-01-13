package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid

interface HTFluidContent : HTContent<Fluid> {
    val tagKey: TagKey<Fluid>
        get() = fluidTagKey(commonId(id.path))
}
