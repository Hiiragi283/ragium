package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.material.HTMaterialDefinitionEvent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.addDefaultPrefix
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber
object ModMaterialKeys {
    @SubscribeEvent
    fun gatherDefinition(event: HTMaterialDefinitionEvent) {
        event.modify(Metals.entries) { addDefaultPrefix(CommonMaterialPrefixes.INGOT) }
        event.modify(Alloys.entries) { addDefaultPrefix(CommonMaterialPrefixes.INGOT) }
        event.modify(Gems.entries) { addDefaultPrefix(CommonMaterialPrefixes.GEM) }
    }

    enum class Metals : HTMaterialLike {
        // Draconic Evolution
        DRACONIUM,
        DRACONIUM_AWAKENED,

        // Just Dire Things
        FERRICORE,
        BLAZEGOLD,
        ECLIPSEALLOY,

        // Occultism
        IESNIUM,

        // Replication
        REPLICA,

        // Twilight Forest
        IRONWOOD,
        WROUGHT_IRON,
        KNIGHTMETAL,
        ;

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    enum class Alloys : HTMaterialLike {
        // Ender IO
        CONDUCTIVE_ALLOY,
        COPPER_ALLOY,
        DARK_STEEL,
        END_STEEL,
        ENERGETIC_ALLOY,
        PULSATING_ALLOY,
        REDSTONE_ALLOY,
        SOULARIUM,
        VIBRANT_ALLOY,

        // Immersive Engineering
        HOP_GRAPHITE,

        // Mekanism
        REFINED_OBSIDIAN,
        REFINED_GLOWSTONE,

        // Oritech
        ADAMANT,
        DURATIUM,
        ENERGITE,
        PROMETHEUM,

        // Twilight Forest
        STEELEAF,
        FIERY,
        ;

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }

    enum class Gems : HTMaterialLike {
        // AA,
        BLACK_QUARTZ,

        // AE2
        CERTUS_QUARTZ,
        FLUIX,

        // Ars Nouveau
        SOURCE,

        // Ender IO
        ENDER_CRYSTAL,
        ENTICING_CRYSTAL,
        PRESCIENT_CRYSTAL,
        PULSATING_CRYSTAL,
        VIBRANT_CRYSTAL,
        WEATHER_CRYSTAL,

        // EvilCraft
        DARK_GEM,
        DARK_POWER,

        // Forbidden
        ARCANE_CRYSTAL,
        CORRUPTED_ARCANE_CRYSTAL,

        // Magitech,
        TOURMALINE,

        // Oritech
        FLUXITE,

        // Twilight Forest
        CARMINITE,
        ;

        override fun asMaterialKey(): HTMaterialKey = HTMaterialKey.of(name.lowercase())
    }
}
