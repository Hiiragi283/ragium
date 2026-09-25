package hiiragi283.ragium.api.data.chemical

import hiiragi283.lib.registry.createKey
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.element.HTElement
import hiiragi283.ragium.api.data.element.RagiumElements
import net.minecraft.core.HolderGetter
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey

/**
 * Ragiumで追加される[化学物質][HTChemical]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
data object RagiumChemicals {
    //    Simple Substances    //

    // 1st Period
    @JvmField
    val HYDROGEN: ResourceKey<HTChemical> = create("hydrogen")

    // 2nd Period
    @JvmField
    val CARBON: ResourceKey<HTChemical> = create("carbon")

    @JvmField
    val DIAMOND: ResourceKey<HTChemical> = create("diamond")

    @JvmField
    val NITROGEN: ResourceKey<HTChemical> = create("nitrogen")

    @JvmField
    val OXYGEN: ResourceKey<HTChemical> = create("oxygen")

    // 3rd Period
    @JvmField
    val ALUMINUM: ResourceKey<HTChemical> = create("aluminum")

    @JvmField
    val SILICON: ResourceKey<HTChemical> = create("silicon")

    @JvmField
    val SULFUR: ResourceKey<HTChemical> = create("sulfur")

    @JvmField
    val CHLORINE: ResourceKey<HTChemical> = create("chlorine")

    // 4th Period

    //    Compounds    //

    // 3rd Period
    @JvmField
    val ALUMINA: ResourceKey<HTChemical> = create("alumina")

    //    Mixtures    //

    @JvmStatic
    private fun create(name: String): ResourceKey<HTChemical> =
        RagiumRegistries.Keys.CHEMICAL.createKey(RagiumAPI.id(name))

    @JvmStatic
    fun bootstrap(context: BootstrapContext<HTChemical>) {
        val getter: HolderGetter<HTElement> = context.lookup(RagiumRegistries.Keys.ELEMENT)
        // Simple Substances
        context.register(HYDROGEN, HTSimpleChemical(getter.getOrThrow(RagiumElements.HYDROGEN), 2))

        context.register(CARBON, HTSimpleChemical(getter.getOrThrow(RagiumElements.CARBON), 1))
        context.register(DIAMOND, HTSimpleChemical(getter.getOrThrow(RagiumElements.CARBON), 1))
        context.register(NITROGEN, HTSimpleChemical(getter.getOrThrow(RagiumElements.NITROGEN), 2))
        context.register(OXYGEN, HTSimpleChemical(getter.getOrThrow(RagiumElements.OXYGEN), 2))

        context.register(ALUMINUM, HTSimpleChemical(getter.getOrThrow(RagiumElements.ALUMINUM), 1))
        context.register(SILICON, HTSimpleChemical(getter.getOrThrow(RagiumElements.SILICON), 1))
        context.register(SULFUR, HTSimpleChemical(getter.getOrThrow(RagiumElements.SULFUR), 1))
        context.register(CHLORINE, HTSimpleChemical(getter.getOrThrow(RagiumElements.CHLORINE), 2))

        // Compounds
        context.register(
            ALUMINA,
            HTCompoundChemical.build(context) {
                add(RagiumElements.ALUMINUM, 2)
                add(RagiumElements.OXYGEN, 3)
            }
        )
    }
}
