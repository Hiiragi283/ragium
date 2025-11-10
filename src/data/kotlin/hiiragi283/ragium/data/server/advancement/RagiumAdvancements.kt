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

    // Advanced
    val ADV_RAGI_ALLOY: HTAdvancementKey = create("advanced_ragi_alloy")

    val MELTER: HTAdvancementKey = create("melter")

    // Elite
    val RAGI_CRYSTAL: HTAdvancementKey = create("ragi_crystal")

    val RAGI_CRYSTAL_HAMMER: HTAdvancementKey = create("ragi_crystal_hammer")

    val RAGI_TICKET: HTAdvancementKey = create("ragi_ticket")

    //    Azure    //

    val AZURE_SHARD: HTAdvancementKey = create("azure_shard")

    val AZURE_STEEL: HTAdvancementKey = create("azure_steel")

    val AZURE_GEARS: HTAdvancementKey = create("azure_gears")

    val SIMULATOR: HTAdvancementKey = create("simulator")

    //    Deep    //

    val RESONANT_DEBRIS: HTAdvancementKey = create("resonant_debris")

    val DEEP_STEEL: HTAdvancementKey = create("deep_steel")

    val DEEP_GEARS: HTAdvancementKey = create("deep_gears")

    val ECHO_STAR: HTAdvancementKey = create("echo_star")

    //    Crimson    //

    val CRIMSON_CRYSTAL: HTAdvancementKey = create("crimson_crystal")

    val CRIMSON_SOIL: HTAdvancementKey = create("crimson_soil")

    //    Warped    //

    val WARPED_CRYSTAL: HTAdvancementKey = create("warped_crystal")

    val DIM_ANCHOR: HTAdvancementKey = create("dimensional_anchor")

    val TELEPORT_KEY: HTAdvancementKey = create("teleport_key")

    //    Eldritch    //

    val ELDRITCH_PEARL: HTAdvancementKey = create("eldritch_pearl")

    val ELDRITCH_EGG: HTAdvancementKey = create("eldritch_egg")

    val MYSTERIOUS_OBSIDIAN: HTAdvancementKey = create("mysterious_obsidian")

    //    Iridescentium    //

    val IRIDESCENTIUM: HTAdvancementKey = create("iridescentium")

    val ETERNAL_COMPONENT: HTAdvancementKey = create("eternal_component")

    @JvmStatic
    private fun create(path: String): HTAdvancementKey = Registries.ADVANCEMENT.createKey(RagiumAPI.id(path))
}
