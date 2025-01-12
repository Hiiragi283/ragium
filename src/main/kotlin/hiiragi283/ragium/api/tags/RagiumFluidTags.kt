package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.fluidTagKey
import net.minecraft.fluid.Fluid
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumFluidTags {
    //    Custom    //
    /**
     * 原子炉の冷却材に使える液体のタグ
     */
    @JvmField
    val COOLANTS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "coolants")

    /**
     * 燃焼発電機の燃料に使える液体のタグ
     */
    @JvmField
    val FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels")

    /**
     * 燃焼発電機でニトロ燃料として使える液体のタグ
     */
    @JvmField
    val NITRO_FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels/nitro")

    /**
     * 燃焼発電機で非ニトロ燃料として使える液体のタグ
     */
    @JvmField
    val NON_NITRO_FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels/non_nitro")

    /**
     * 植物性，動物性の油として使える液体のタグ
     */
    @JvmField
    val ORGANIC_OILS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "organic_oils")

    /**
     * 地熱発電機で燃料として使える液体のタグ
     */
    @JvmField
    val THERMAL_FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels/thermal")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Fluid> = fluidTagKey(Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Fluid> = fluidTagKey(commonId(path))
}
