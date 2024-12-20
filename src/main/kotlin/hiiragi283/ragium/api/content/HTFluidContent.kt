package hiiragi283.ragium.api.content

import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.fluid.Fluid
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

interface HTFluidContent : HTContent.Delegated<Fluid> {
    val tagKey: TagKey<Fluid>
        get() = TagKey.of(RegistryKeys.FLUID, Identifier.of(TagUtil.C_TAG_NAMESPACE, id.path))
}
