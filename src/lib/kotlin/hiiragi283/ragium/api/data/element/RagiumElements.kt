package hiiragi283.ragium.api.data.element

import hiiragi283.lib.registry.createKey
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey

/**
 * Ragiumで追加される[元素][HTElement]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
data object RagiumElements {
    // 1st Period
    @JvmField
    val HYDROGEN: ResourceKey<HTElement> = create("hydrogen")

    // 2nd Period
    @JvmField
    val CARBON: ResourceKey<HTElement> = create("carbon")

    @JvmField
    val NITROGEN: ResourceKey<HTElement> = create("nitrogen")

    @JvmField
    val OXYGEN: ResourceKey<HTElement> = create("oxygen")

    @JvmField
    val FLUORINE: ResourceKey<HTElement> = create("fluorine")

    // 3rd Period
    @JvmField
    val SODIUM: ResourceKey<HTElement> = create("sodium")

    @JvmField
    val ALUMINUM: ResourceKey<HTElement> = create("aluminum")

    @JvmField
    val SILICON: ResourceKey<HTElement> = create("silicon")

    @JvmField
    val SULFUR: ResourceKey<HTElement> = create("sulfur")

    @JvmField
    val CHLORINE: ResourceKey<HTElement> = create("chlorine")

    // 4th Period
    @JvmField
    val POTASSIUM: ResourceKey<HTElement> = create("potassium")

    @JvmField
    val CALCIUM: ResourceKey<HTElement> = create("calcium")

    @JvmField
    val IRON: ResourceKey<HTElement> = create("iron")

    @JvmField
    val COPPER: ResourceKey<HTElement> = create("copper")

    // 6th Period
    @JvmField
    val GOLD: ResourceKey<HTElement> = create("gold")

    @JvmStatic
    private fun create(name: String): ResourceKey<HTElement> =
        RagiumRegistries.Keys.ELEMENT.createKey(RagiumAPI.id(name))

    @JvmStatic
    fun bootstrap(context: BootstrapContext<HTElement>) {
        fun register(key: ResourceKey<HTElement>, symbol: String) {
            context.register(key, HTElement(symbol))
        }

        register(HYDROGEN, "H")

        register(CARBON, "C")
        register(NITROGEN, "N")
        register(OXYGEN, "O")
        register(FLUORINE, "F")

        register(SODIUM, "Na")
        register(ALUMINUM, "Al")
        register(SILICON, "Si")
        register(SULFUR, "S")
        register(CHLORINE, "Cl")

        register(POTASSIUM, "K")
        register(CALCIUM, "Ca")
        register(IRON, "Fe")
        register(COPPER, "Cu")

        register(GOLD, "Au")
    }
}
