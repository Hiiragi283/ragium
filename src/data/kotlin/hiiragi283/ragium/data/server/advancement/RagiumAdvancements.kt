package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.advancement.HTAdvancementKey
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.core.registries.Registries

object RagiumAdvancements {
    val ROOT: HTAdvancementKey = create("root")

    val CRAFTABLE_TEMPLATES: HTAdvancementKey = create("craftable_templates")

    //    Raginite    //

    val RAGINITE: HTAdvancementKey = create(RagiumConst.RAGINITE)

    val RAGI_CHERRY: HTAdvancementKey = create(RagiumConst.RAGI_CHERRY)

    val RAGI_CHERRY_TOAST: HTAdvancementKey = create(RagiumConst.RAGI_CHERRY + "_toast")

    val RAGI_ALLOY: HTAdvancementKey = create(RagiumConst.RAGI_ALLOY)

    val ALLOY_SMELTER: HTAdvancementKey = create("alloy_smelter")

    // Advanced
    val ADV_RAGI_ALLOY: HTAdvancementKey = create(RagiumConst.ADVANCED_RAGI_ALLOY)

    val MELTER: HTAdvancementKey = create("melter")

    // Elite
    val RAGI_CRYSTAL: HTAdvancementKey = create(RagiumConst.RAGI_CRYSTAL)

    val RAGI_CRYSTAL_HAMMER: HTAdvancementKey = create(RagiumConst.RAGI_CRYSTAL + "_hammer")

    val RAGI_TICKET: HTAdvancementKey = create("ragi_ticket")

    //    Azure    //

    val AZURE_SHARD: HTAdvancementKey = create("azure_shard")

    val AZURE_STEEL: HTAdvancementKey = create(RagiumConst.AZURE_STEEL)

    val AZURE_GEARS: HTAdvancementKey = create("azure_gears")

    val SIMULATOR: HTAdvancementKey = create("simulator")

    //    Deep    //

    val RESONANT_DEBRIS: HTAdvancementKey = create("resonant_debris")

    val DEEP_STEEL: HTAdvancementKey = create(RagiumConst.DEEP_STEEL)

    val DEEP_GEARS: HTAdvancementKey = create("deep_gears")

    val ECHO_STAR: HTAdvancementKey = create("echo_star")

    //    Crimson    //

    val CRIMSON_CRYSTAL: HTAdvancementKey = create(RagiumConst.CRIMSON_CRYSTAL)

    val CRIMSON_SOIL: HTAdvancementKey = create("crimson_soil")

    //    Warped    //

    val WARPED_CRYSTAL: HTAdvancementKey = create(RagiumConst.WARPED_CRYSTAL)

    val DIM_ANCHOR: HTAdvancementKey = create("dimensional_anchor")

    val TELEPORT_KEY: HTAdvancementKey = create("teleport_key")

    //    Eldritch    //

    val ELDRITCH_PEARL: HTAdvancementKey = create(RagiumConst.ELDRITCH_PEARL)

    val ELDRITCH_EGG: HTAdvancementKey = create("eldritch_egg")

    val MYSTERIOUS_OBSIDIAN: HTAdvancementKey = create("mysterious_obsidian")

    //    Iridescentium    //

    val IRIDESCENTIUM: HTAdvancementKey = create(RagiumConst.IRIDESCENTIUM)

    val ETERNAL_COMPONENT: HTAdvancementKey = create("eternal_component")

    @JvmStatic
    private fun create(path: String): HTAdvancementKey = Registries.ADVANCEMENT.createKey(RagiumAPI.id(path))
}
