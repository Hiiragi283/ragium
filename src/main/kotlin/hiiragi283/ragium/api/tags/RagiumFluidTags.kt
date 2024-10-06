package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.fluid.Fluid
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumFluidTags {
    //    Custom    //
    @JvmField
    val COMBUSTION_FUEL: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "combustion_fuel")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Fluid> =
        TagKey.of(RegistryKeys.FLUID, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Fluid> = create(TagUtil.C_TAG_NAMESPACE, path)
}
