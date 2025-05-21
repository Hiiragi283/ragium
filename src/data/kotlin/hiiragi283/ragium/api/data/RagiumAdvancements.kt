package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.advancements.Advancement
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey

object RagiumAdvancements {
    @JvmField
    val ROOT: ResourceKey<Advancement> = create("root")

    @JvmField
    val ETERNAL_TICKET: ResourceKey<Advancement> = create("eternal_ticket")

    //    Raginite    //

    @JvmField
    val RAGI_TICKET: ResourceKey<Advancement> = create("ragi_ticket")

    @JvmField
    val RAGINITE_DUST: ResourceKey<Advancement> = create("raginite_dust")

    //    Azure    //

    @JvmField
    val AZURE_TICKET: ResourceKey<Advancement> = create("azure_ticket")

    @JvmField
    val AZURE_SHARD: ResourceKey<Advancement> = create("azure_shard")

    @JvmField
    val AZURE_GEARS: ResourceKey<Advancement> = create("azure_gears")

    //    Crimson    //

    @JvmField
    val CRIMSON_CRYSTAL: ResourceKey<Advancement> = create("crimson_crystal")

    @JvmField
    val CRIMSON_SOIL: ResourceKey<Advancement> = create("crimson_soil")

    //    Warped    //

    @JvmField
    val WARPED_CRYSTAL: ResourceKey<Advancement> = create("warped_crystal")

    @JvmField
    val TELEPORT_TICKET: ResourceKey<Advancement> = create("teleport_ticket")
    
    //    Eldritch    //

    @JvmField
    val ELDRITCH_PEARL: ResourceKey<Advancement> = create("eldritch_pearl")

    @JvmStatic
    private fun create(path: String): ResourceKey<Advancement> = ResourceKey.create(Registries.ADVANCEMENT, RagiumAPI.id(path))
}
