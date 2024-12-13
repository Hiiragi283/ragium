package hiiragi283.ragium.api.tags

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil
import net.minecraft.fluid.Fluid
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object RagiumFluidTags {
    //    Custom    //
    /**
     * Include coolant fluids
     *
     * Fluid consumption is 81,000 Units/process
     * @see [hiiragi283.ragium.common.block.machine.generator.HTNuclearReactorBlockEntity]
     */
    @JvmField
    val COOLANTS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "coolants")

    /**
     * Include fuel fluids
     * @see [hiiragi283.ragium.common.block.machine.generator.HTCombustionGeneratorBlockEntity]
     */
    @JvmField
    val FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels")

    /**
     * Include nitro fuel fluids
     *
     * Fluid consumption is 1,000 Units/process
     * @see [hiiragi283.ragium.common.block.machine.generator.HTCombustionGeneratorBlockEntity]
     */
    @JvmField
    val NITRO_FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels/nitro")

    /**
     * Include non-nitro fuel fluids
     *
     *  Fluid consumption is 9,000 Units/process
     * @see [hiiragi283.ragium.common.block.machine.generator.HTCombustionGeneratorBlockEntity]
     */
    @JvmField
    val NON_NITRO_FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels/non_nitro")

    /**
     * Include organic oils, like [hiiragi283.ragium.common.init.RagiumFluids.SEED_OIL] or [hiiragi283.ragium.common.init.RagiumFluids.TALLOW]
     */
    @JvmField
    val ORGANIC_OILS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "organic_oils")

    /**
     * Include heat fluids
     *
     *  Fluid consumption is 9000 Units/process
     * @see [hiiragi283.ragium.common.block.machine.generator.HTThermalGeneratorBlockEntity]
     */
    @JvmField
    val THERMAL_FUELS: TagKey<Fluid> = create(RagiumAPI.MOD_ID, "fuels/thermal")

    @JvmStatic
    fun create(namespace: String, path: String): TagKey<Fluid> = TagKey.of(RegistryKeys.FLUID, Identifier.of(namespace, path))

    @JvmStatic
    fun create(path: String): TagKey<Fluid> = create(TagUtil.C_TAG_NAMESPACE, path)
}
