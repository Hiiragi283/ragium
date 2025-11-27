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
    const val EIO_MACHINES = "enderio_machines"
    const val FARMERS_DELIGHT = "farmersdelight"
    const val FOREGOING = "industrialforegoing"
    const val HOSTILE_NETWORKS = "hostilenetworks"
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

    //    Serialization    //

    const val ACCESS_CONFIG = "access_config"
    const val ENCHANTMENT = "enchantment"
    const val OWNER = "owner"

    const val ITEM = "item"
    const val SLOT = "slot"
    const val ITEMS = "items"

    const val FLUID = "fluid"
    const val TANK = "tank"
    const val FLUIDS = "fluids"

    const val AMOUNT = "amount"
    const val CAPACITY = "capacity"
    const val BATTERIES = "batteries"

    const val ID = "id"
    const val TAG = "tag"
    const val COUNT = "count"
    const val COMPONENTS = "components"
    const val CHANCE = "chance"

    const val PREVENT_ITEM_MAGNET = "PreventRemoteMovement"

    //    Recipes    //

    const val ALLOYING = "alloying"
    const val BREWING = "brewing"
    const val COMPRESSING = "compressing"
    const val CRUSHING = "crushing"
    const val CUTTING = "cutting"
    const val ENCHANTING = "enchanting"
    const val EXTRACTING = "extracting"
    const val MELTING = "melting"
    const val MIXING = "mixing"
    const val PLANTING = "planting"
    const val REFINING = "refining"
    const val SIMULATING = "simulating"
    const val SOLIDIFYING = "solidifying"
    const val WASHING = "washing"

    const val INGREDIENT = "ingredient"

    const val RESULT = "result"
    const val RESULTS = "results"
    const val ITEM_RESULT = "item_result"
    const val FLUID_RESULT = "fluid_result"

    const val LUBRICANT_CONSUME = 25
}
