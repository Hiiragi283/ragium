package hiiragi283.ragium.data.server.advancement

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.advancements.Advancement
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey

object RagiumAdvancements {
    @JvmField
    val ROOT: ResourceKey<Advancement> = create("root")

    @JvmField
    val CRAFTABLE_TEMPLATES: ResourceKey<Advancement> = create("craftable_templates")

    @JvmField
    val ETERNAL_TICKET: ResourceKey<Advancement> = create("eternal_ticket")

    //    Raginite    //

    @JvmField
    val RAGINITE: ResourceKey<Advancement> = create(RagiumConst.RAGINITE)

    @JvmField
    val RAGI_ALLOY: ResourceKey<Advancement> = create(RagiumConst.RAGI_ALLOY)

    @JvmField
    val ADV_RAGI_ALLOY: ResourceKey<Advancement> = create(RagiumConst.ADVANCED_RAGI_ALLOY)

    @JvmField
    val RAGI_CRYSTAL: ResourceKey<Advancement> = create(RagiumConst.RAGI_CRYSTAL)

    @JvmField
    val RAGI_CRYSTAL_HAMMER: ResourceKey<Advancement> = create(RagiumConst.RAGI_CRYSTAL + "_hammer")

    @JvmField
    val RAGI_TICKET: ResourceKey<Advancement> = create("ragi_ticket")

    //    Azure    //

    @JvmField
    val AZURE_SHARD: ResourceKey<Advancement> = create("azure_shard")

    @JvmField
    val AZURE_STEEL: ResourceKey<Advancement> = create("azure_steel")

    @JvmField
    val AZURE_GEARS: ResourceKey<Advancement> = create("azure_gears")

    //    Crimson    //

    @JvmField
    val CRIMSON_CRYSTAL: ResourceKey<Advancement> = create(RagiumConst.CRIMSON_CRYSTAL)

    @JvmField
    val CRIMSON_SOIL: ResourceKey<Advancement> = create("crimson_soil")

    //    Warped    //

    @JvmField
    val WARPED_CRYSTAL: ResourceKey<Advancement> = create(RagiumConst.WARPED_CRYSTAL)

    @JvmField
    val DIM_ANCHOR: ResourceKey<Advancement> = create("dimensional_anchor")

    @JvmField
    val TELEPORT_TICKET: ResourceKey<Advancement> = create("teleport_ticket")

    //    Eldritch    //

    @JvmField
    val ELDRITCH_PEARL: ResourceKey<Advancement> = create(RagiumConst.ELDRITCH_PEARL)

    @JvmField
    val ELDRITCH_EGG: ResourceKey<Advancement> = create("eldritch_egg")

    @JvmField
    val MYSTERIOUS_OBSIDIAN: ResourceKey<Advancement> = create("mysterious_obsidian")

    @JvmStatic
    private fun create(path: String): ResourceKey<Advancement> = ResourceKey.create(Registries.ADVANCEMENT, RagiumAPI.id(path))
}
