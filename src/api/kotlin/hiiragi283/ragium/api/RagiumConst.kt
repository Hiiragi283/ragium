package hiiragi283.ragium.api

import net.minecraft.world.item.Item
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion

/**
 * Ragiumで使用する定数の一覧
 */
object RagiumConst {
    //    Fluid    //

    const val LOG_TO_SAP = 500
    const val SAP_TO_MOLTEN = 250
    const val MOLTEN_TO_GEM = 1000

    //    Item    //

    const val ABSOLUTE_MAX_STACK_SIZE: Int = Item.ABSOLUTE_MAX_STACK_SIZE

    //    Materials    //

    const val ADVANCED_RAGI_ALLOY = "advanced_ragi_alloy"
    const val AZURE = "azure"
    const val AZURE_STEEL = "azure_steel"
    const val CHOCOLATE = "chocolate"
    const val COAL_COKE = "coal_coke"
    const val COOKED_MEAT = "cooked_meat"
    const val CRIMSON_CRYSTAL = "crimson_crystal"
    const val DEEP_STEEL = "deep_steel"
    const val ELDRITCH_PEARL = "eldritch_pearl"
    const val IRIDESCENTIUM = "iridescentium"
    const val MEAT = "meat"
    const val OBSIDIAN = "obsidian"
    const val QUARTZ = "quartz"
    const val RAGI_ALLOY = "ragi_alloy"
    const val RAGI_CHERRY = "ragi_cherry"
    const val RAGI_CRYSTAL = "ragi_crystal"
    const val RAGINITE = "raginite"
    const val WARPED_CRYSTAL = "warped_crystal"

    //    Mod ID    //

    const val MINECRAFT = "minecraft"
    const val NEOFORGE: String = NeoForgeVersion.MOD_ID

    const val ACCESSORIES = "accessories"
    const val ACTUALLY = "actuallyadditions"
    const val AE2 = "ae2"
    const val ALMOST = "almostunified"
    const val ARS_NOUVEAU = "ars_nouveau"
    const val COMMON = "c"
    const val CREATE = "create"
    const val EIO = "enderio"
    const val EIO_BASE = "enderio_base"
    const val FARMERS_DELIGHT = "farmersdelight"
    const val FOREGOING = "industrialforegoing"
    const val IMMERSIVE = "immersiveengineering"
    const val KALEIDO_COOKERY = "kaleidoscope_cookery"
    const val MAGITECH = "magitech"
    const val MEKANISM = "mekanism"
    const val MODERN = "modern_industrialization"
    const val ORITECH = "oritech"
    const val PNEUMATIC = "pneumaticcraft"
    const val RAILCRAFT = "railcraft"
    const val REPLICATION = "replication"
    const val TWILIGHT = "twilightforest"

    @JvmField
    val BUILTIN_IDS: Set<String> = setOf(MINECRAFT, NEOFORGE, COMMON, RagiumAPI.MOD_ID)

    //    Nbt    //

    const val ACCESS_CONFIG = "access_config"
    const val LAST_RECIPE = "last_recipe"
    const val OWNER = "owner"

    const val ITEM = "item"
    const val SLOT = "slot"
    const val ITEMS = "items"

    const val FLUID = "fluid"
    const val TANK = "tank"
    const val FLUIDS = "fluids"

    const val AMOUNT = "amount"
    const val INDEX = "index"
    const val BATTERIES = "batteries"

    const val PREVENT_ITEM_MAGNET = "PreventRemoteMovement"

    //    Recipes    //

    const val ALLOYING = "alloying"
    const val BREWING = "brewing"
    const val COMPRESSING = "compressing"
    const val CRUSHING = "crushing"
    const val ENCHANTING = "enchanting"
    const val EXTRACTING = "extracting"
    const val FLUID_TRANSFORM = "fluid_transform"
    const val MELTING = "melting"
    const val MIXING = "mixing"
    const val PLANTING = "planting"
    const val REFINING = "refining"
    const val SIMULATING = "simulating"
    const val SOLIDIFYING = "solidifying"
    const val WASHING = "washing"

    const val SAWMILL = "sawmill"

    //    Tag Prefixes    //

    const val CHIPS = "chips"
    const val CIRCUITS = "circuits"
    const val DUSTS = "dusts"
    const val FUELS = "fuels"
    const val GEMS = "gems"
    const val GLASS_BLOCKS = "glass_blocks"
    const val GLASS_BLOCKS_TINTED = "glass_blocks/tinted"
    const val INGOTS = "ingots"
    const val MOLDS = "molds"
    const val NUGGETS = "nuggets"
    const val ORES = "ores"
    const val SCRAPS = "scraps"
    const val STORAGE_BLOCKS = "storage_blocks"
}
