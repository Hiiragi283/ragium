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
    @JvmField
    val IRON: ResourceKey<HTChemical> = create("iron")

    @JvmField
    val COPPER: ResourceKey<HTChemical> = create("copper")

    // 6th Period
    @JvmField
    val GOLD: ResourceKey<HTChemical> = create("gold")

    //    Compounds    //

    // 1st Period
    @JvmField
    val HYDROXIDE: ResourceKey<HTChemical> = create("hydroxide")

    @JvmField
    val WATER: ResourceKey<HTChemical> = create("water")

    // 2nd Period
    @JvmField
    val NITRATE: ResourceKey<HTChemical> = create("nitrate")

    @JvmField
    val NITRIC_ACID: ResourceKey<HTChemical> = create("nitric_acid")

    @JvmField
    val HYDROGEN_FLUORIDE: ResourceKey<HTChemical> = create("hydrogen_fluoride")

    @JvmField
    val FLUORITE: ResourceKey<HTChemical> = create("fluorite")

    @JvmField
    val CRYOLITE: ResourceKey<HTChemical> = create("cryolite")

    // 3rd Period
    @JvmField
    val SODIUM_HYDROXIDE: ResourceKey<HTChemical> = create("sodium_hydroxide")

    @JvmField
    val SODIUM_CHLORIDE: ResourceKey<HTChemical> = create("sodium_chloride")

    @JvmField
    val ALUMINA: ResourceKey<HTChemical> = create("alumina")

    @JvmField
    val SILICON_DIOXIDE: ResourceKey<HTChemical> = create("silicon_dioxide")

    @JvmField
    val SULFUR_DIOXIDE: ResourceKey<HTChemical> = create("sulfur_dioxide")

    @JvmField
    val SULFUR_TRIOXIDE: ResourceKey<HTChemical> = create("sulfur_trioxide")

    @JvmField
    val SULFATE: ResourceKey<HTChemical> = create("sulfate")

    @JvmField
    val SULFURIC_ACID: ResourceKey<HTChemical> = create("sulfuric_acid")

    @JvmField
    val HYDROGEN_CHLORIDE: ResourceKey<HTChemical> = create("hydrogen_chloride")

    // 4th Period
    @JvmField
    val POTASSIUM_NITRATE: ResourceKey<HTChemical> = create("potassium_nitrate")

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

        context.register(IRON, HTSimpleChemical(getter.getOrThrow(RagiumElements.IRON), 1))
        context.register(COPPER, HTSimpleChemical(getter.getOrThrow(RagiumElements.COPPER), 1))

        context.register(GOLD, HTSimpleChemical(getter.getOrThrow(RagiumElements.GOLD), 1))

        // Compounds
        context.register(
            HYDROXIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.OXYGEN, 1)
                add(RagiumElements.HYDROGEN, 1)
            }
        )
        context.register(
            WATER,
            HTCompoundChemical.build(context) {
                add(RagiumElements.HYDROGEN, 2)
                add(RagiumElements.OXYGEN, 1)
            }
        )

        context.register(
            NITRATE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.NITROGEN, 1)
                add(RagiumElements.OXYGEN, 3)
            }
        )
        context.register(
            NITRIC_ACID,
            HTCompoundChemical.build(context) {
                add(RagiumElements.NITROGEN, 1)
                add(NITRATE, 1)
            }
        )
        context.register(
            HYDROGEN_FLUORIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.HYDROGEN, 1)
                add(RagiumElements.FLUORINE, 1)
            }
        )
        context.register(
            FLUORITE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.CALCIUM, 1)
                add(RagiumElements.FLUORINE, 2)
            }
        )
        context.register(
            CRYOLITE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.SODIUM, 3)
                add(RagiumElements.ALUMINUM, 1)
                add(RagiumElements.FLUORINE, 6)
            }
        )

        context.register(
            SODIUM_HYDROXIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.SODIUM, 1)
                add(HYDROXIDE, 1)
            }
        )
        context.register(
            SODIUM_CHLORIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.SODIUM, 1)
                add(RagiumElements.CHLORINE, 1)
            }
        )
        context.register(
            ALUMINA,
            HTCompoundChemical.build(context) {
                add(RagiumElements.ALUMINUM, 2)
                add(RagiumElements.OXYGEN, 3)
            }
        )
        context.register(
            SILICON_DIOXIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.SILICON, 1)
                add(RagiumElements.OXYGEN, 2)
            }
        )
        context.register(
            SULFUR_DIOXIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.SULFUR, 1)
                add(RagiumElements.OXYGEN, 2)
            }
        )
        context.register(
            SULFUR_TRIOXIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.SULFUR, 1)
                add(RagiumElements.OXYGEN, 3)
            }
        )
        context.register(
            SULFATE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.SULFUR, 1)
                add(RagiumElements.OXYGEN, 4)
            }
        )
        context.register(
            SULFURIC_ACID,
            HTCompoundChemical.build(context) {
                add(RagiumElements.HYDROGEN, 2)
                add(SULFATE, 1)
            }
        )
        context.register(
            HYDROGEN_CHLORIDE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.HYDROGEN, 1)
                add(RagiumElements.CHLORINE, 1)
            }
        )

        context.register(
            POTASSIUM_NITRATE,
            HTCompoundChemical.build(context) {
                add(RagiumElements.POTASSIUM, 1)
                add(NITRATE, 1)
            }
        )
    }
}
