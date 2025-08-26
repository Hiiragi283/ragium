package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.advancement.HTAdvancementKey
import hiiragi283.ragium.api.util.RagiumConst

object RagiumAdvancements {
    @JvmField
    val ROOT: HTAdvancementKey = create("root")

    @JvmField
    val CRAFTABLE_TEMPLATES: HTAdvancementKey = create("craftable_templates")

    //    Raginite    //

    @JvmField
    val RAGINITE: HTAdvancementKey = create(RagiumConst.RAGINITE)

    @JvmField
    val RAGI_CHERRY: HTAdvancementKey = create(RagiumConst.RAGI_CHERRY)

    @JvmField
    val RAGI_CHERRY_TOAST: HTAdvancementKey = create(RagiumConst.RAGI_CHERRY + "_toast")

    @JvmField
    val RAGI_ALLOY: HTAdvancementKey = create(RagiumConst.RAGI_ALLOY)

    // Advanced
    @JvmField
    val ADV_RAGI_ALLOY: HTAdvancementKey = create(RagiumConst.ADVANCED_RAGI_ALLOY)

    // Elite
    @JvmField
    val RAGI_CRYSTAL: HTAdvancementKey = create(RagiumConst.RAGI_CRYSTAL)

    @JvmField
    val RAGI_CRYSTAL_HAMMER: HTAdvancementKey = create(RagiumConst.RAGI_CRYSTAL + "_hammer")

    @JvmField
    val RAGI_TICKET: HTAdvancementKey = create("ragi_ticket")

    //    Azure    //

    @JvmField
    val AZURE_SHARD: HTAdvancementKey = create("azure_shard")

    @JvmField
    val AZURE_STEEL: HTAdvancementKey = create("azure_steel")

    @JvmField
    val AZURE_GEARS: HTAdvancementKey = create("azure_gears")

    //    Crimson    //

    @JvmField
    val CRIMSON_CRYSTAL: HTAdvancementKey = create(RagiumConst.CRIMSON_CRYSTAL)

    @JvmField
    val CRIMSON_SOIL: HTAdvancementKey = create("crimson_soil")

    //    Warped    //

    @JvmField
    val WARPED_CRYSTAL: HTAdvancementKey = create(RagiumConst.WARPED_CRYSTAL)

    @JvmField
    val DIM_ANCHOR: HTAdvancementKey = create("dimensional_anchor")

    @JvmField
    val TELEPORT_KEY: HTAdvancementKey = create("teleport_key")

    //    Eldritch    //

    @JvmField
    val ELDRITCH_PEARL: HTAdvancementKey = create(RagiumConst.ELDRITCH_PEARL)

    @JvmField
    val ELDRITCH_EGG: HTAdvancementKey = create("eldritch_egg")

    @JvmField
    val MYSTERIOUS_OBSIDIAN: HTAdvancementKey = create("mysterious_obsidian")

    //    Iridescentium    //

    @JvmField
    val IRIDESCENTIUM: HTAdvancementKey = create("iridescentium")

    @JvmField
    val ETERNAL_COMPONENT: HTAdvancementKey = create("eternal_component")

    @JvmStatic
    private fun create(path: String): HTAdvancementKey = HTAdvancementKey(RagiumAPI.id(path))
}
