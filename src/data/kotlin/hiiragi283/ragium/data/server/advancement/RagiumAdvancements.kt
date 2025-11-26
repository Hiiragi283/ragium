package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.advancement.HTAdvancementKey
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.core.registries.Registries

object RagiumAdvancements {
    val ROOT: HTAdvancementKey = create("root")

    val CRAFTABLE_TEMPLATES: HTAdvancementKey = create("craftable_templates")

    //    Raginite    //

    val RAGINITE: HTAdvancementKey = create("raginite")

    val RAGI_CHERRY: HTAdvancementKey = create("ragi_cherry")

    val RAGI_CHERRY_TOAST: HTAdvancementKey = create("ragi_cherry_toast")

    val RAGI_ALLOY: HTAdvancementKey = create("ragi_alloy")

    val ALLOY_SMELTER: HTAdvancementKey = create("alloy_smelter")

    val AMBROSIA: HTAdvancementKey = create("ambrosia")

    // Advanced
    val ADV_RAGI_ALLOY: HTAdvancementKey = create("advanced_ragi_alloy")

    val MELTER: HTAdvancementKey = create("melter")

    val REFINERY: HTAdvancementKey = create("refinery")

    val PLASTIC: HTAdvancementKey = create("plastic")

    val POTION_BUNDLE: HTAdvancementKey = create("potion_bundle")

    // Elite
    val RAGI_CRYSTAL: HTAdvancementKey = create("ragi_crystal")

    val RAGI_CRYSTAL_HAMMER: HTAdvancementKey = create("ragi_crystal_hammer")

    val RAGI_TICKET: HTAdvancementKey = create("ragi_ticket")

    //    Azure    //

    val AZURE_SHARD: HTAdvancementKey = create("azure_shard")

    val AZURE_STEEL: HTAdvancementKey = create("azure_steel")

    val AZURE_GEARS: HTAdvancementKey = create("azure_gears")

    val MIXER: HTAdvancementKey = create("mixer")

    //    Deep    //

    val RESONANT_DEBRIS: HTAdvancementKey = create("resonant_debris")

    val DEEP_STEEL: HTAdvancementKey = create("deep_steel")

    val BEHEAD_MOB: HTAdvancementKey = create("behead_mob")

    val ECHO_STAR: HTAdvancementKey = create("echo_star")

    //    Night Metal    //

    val NIGHT_METAL: HTAdvancementKey = create("night_metal")

    val SIMULATOR: HTAdvancementKey = create("simulator")

    //    Crimson    //

    val CRIMSON_CRYSTAL: HTAdvancementKey = create("crimson_crystal")

    val CRIMSON_SOIL: HTAdvancementKey = create("crimson_soil")

    //    Warped    //

    val WARPED_CRYSTAL: HTAdvancementKey = create("warped_crystal")

    val DIM_ANCHOR: HTAdvancementKey = create("dimensional_anchor")

    val TELEPORT_KEY: HTAdvancementKey = create("teleport_key")

    val WARPED_WART: HTAdvancementKey = create("warped_wart")

    //    Eldritch    //

    val ELDRITCH_PEARL: HTAdvancementKey = create("eldritch_pearl")

    val ELDRITCH_EGG: HTAdvancementKey = create("eldritch_egg")

    val MYSTERIOUS_OBSIDIAN: HTAdvancementKey = create("mysterious_obsidian")

    //    Iridescentium    //

    val IRIDESCENT_POWDER: HTAdvancementKey = create("iridescent_powder")

    val ETERNAL_COMPONENT: HTAdvancementKey = create("eternal_component")

    @JvmStatic
    private fun create(path: String): HTAdvancementKey = Registries.ADVANCEMENT.createKey(RagiumAPI.id(path))
}
