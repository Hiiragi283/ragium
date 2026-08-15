package hiiragi283.lib

import hiiragi283.lib.resource.vanillaId
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.common.NeoForgeMod

/**
 * Ragiumで使用される定数を集めたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTConstants {
    const val ZERO_CELSIUS = 273
    const val STANDARD_TEMP = 300

    const val EXPERIMENTAL = "experimental"
    const val MATERIAL = "material"

    //    Fluid    //

    //    GUI    //

    const val TEXTURES = "textures"
    const val GUI = "gui"

    //    Item    //

    //    Mod ID    //

    /**
     * MinecraftのMOD ID
     */
    const val MINECRAFT = "minecraft"

    /**
     * NeoForgeのMOD ID
     */
    const val NEOFORGE: String = NeoForgeMod.MOD_ID

    /**
     * 共通タグで使用されるID
     */
    const val COMMON = "c"

    @JvmField
    val BUILT_IN_IDS: Set<String> = setOf(MINECRAFT, NEOFORGE, COMMON)

    @JvmStatic
    fun getBuiltInIdSet(modId: String): Set<String> = BUILT_IN_IDS + modId

    //    Serialization    //

    const val OWNER = "owner"
    const val BLOCK = "block"

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

    //    Recipes    //

    // Vanilla
    const val BLASTING = "blasting"
    const val SHAPED = "shaped"
    const val SHAPELESS = "shapeless"
    const val SMELTING = "smelting"
    const val SMITHING = "smithing"
    const val SMOKING = "smoking"
    const val TRANSMUTE = "transmute"

    // Serialization
    const val ENERGY = "energy"
    const val TIME = "time"

    const val INGREDIENT = "ingredient"
    const val INGREDIENTS = "ingredients"
    const val PRIMARY_INGREDIENT = "primary_ingredient"
    const val SECONDARY_INGREDIENT = "secondary_ingredient"
    const val ITEM_INGREDIENT = "item_ingredient"
    const val FLUID_INGREDIENT = "fluid_ingredient"

    const val CATALYST = "catalyst"

    const val RESULT = "result"
    const val RESULTS = "results"
    const val PRIMARY_RESULT = "primary_result"
    const val SECONDARY_RESULT = "secondary_result"
    const val ITEM_RESULT = "item_result"
    const val FLUID_RESULT = "fluid_result"

    //    TagKey    //

    const val ELEMENTS = "elements"
    const val MINERALS = "minerals"

    //    Text    //

    const val CONSTANTS = "constants"
    const val DESCRIPTION = "description"
    const val ERROR = "error"
    const val TOOLTIP = "tooltip"

    const val ITEM_GROUP = "itemGroup"
    const val UPGRADE = "upgrade"

    data object Keys {
        @JvmField
        val AIR: Identifier = vanillaId("air")

        @JvmField
        val EMPTY: Identifier = vanillaId("empty")
    }
}
