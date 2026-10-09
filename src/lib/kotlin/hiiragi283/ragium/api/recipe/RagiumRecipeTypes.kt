package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.OnDatapackSyncEvent

/**
 * Ragiumで使用される[RecipeType]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@EventBusSubscriber
data object RagiumRecipeTypes {
    @JvmStatic
    val allTypes: Map<Identifier, RecipeType<*>>
        field: MutableMap<Identifier, RecipeType<*>> = Object2ObjectLinkedOpenHashMap()

    @JvmStatic
    private fun <T : Recipe<*>> create(name: String): RecipeType<T> {
        val id: Identifier = RagiumAPI.id(name)
        val recipeType: RecipeType<T> = RecipeType.simple<T>(id)
        allTypes[id] = recipeType
        return recipeType
    }

    @SubscribeEvent
    fun onDatapackSync(event: OnDatapackSyncEvent) {
        event.sendRecipes(allTypes.values)
    }

    // Mechanical
    @JvmField
    val ASSEMBLING: RecipeType<RTAssemblingRecipe> = create(RagiumConstants.ASSEMBLING)

    @JvmField
    val COMPRESSING: RecipeType<RTCompressingRecipe> = create(RagiumConstants.COMPRESSING)

    @JvmField
    val CRUSHING: RecipeType<RTCrushingRecipe> = create(RagiumConstants.CRUSHING)

    @JvmField
    val CUTTING: RecipeType<RTCuttingRecipe> = create(RagiumConstants.CUTTING)

    @JvmField
    val DRAINING: RecipeType<RTDrainingRecipe> = create(RagiumConstants.DRAINING)

    @JvmField
    val FILLING: RecipeType<RTFillingRecipe> = create(RagiumConstants.FILLING)

    // Heat
    @JvmField
    val ALLOYING: RecipeType<RTAlloyingRecipe> = create(RagiumConstants.ALLOYING)

    @JvmField
    val FREEZING: RecipeType<RTFreezingRecipe> = create(RagiumConstants.FREEZING)

    @JvmField
    val MELTING: RecipeType<RTMeltingRecipe> = create(RagiumConstants.MELTING)

    @JvmField
    val PYROLYZING: RecipeType<RTPyrolyzingRecipe> = create(RagiumConstants.PYROLYZING)

    @JvmField
    val REFINING: RecipeType<RTRefiningRecipe> = create(RagiumConstants.REFINING)

    // Chemical
    @JvmField
    val BATHING: RecipeType<RTBathingRecipe> = create(RagiumConstants.BATHING)

    @JvmField
    val CENTRIFUGING: RecipeType<RTCentrifugingRecipe> = create(RagiumConstants.CENTRIFUGING)

    @JvmField
    val EXCHANGING: RecipeType<RTExchangingRecipe> = create(RagiumConstants.EXCHANGING)

    @JvmField
    val MIXING: RecipeType<RTMixingRecipe> = create(RagiumConstants.MIXING)

    @JvmField
    val REACTING: RecipeType<RTReactingRecipe> = create(RagiumConstants.REACTING)

    @JvmField
    val WASHING: RecipeType<RTWashingRecipe> = create(RagiumConstants.WASHING)

    // Resource
    @JvmField
    val PLANTING: RecipeType<RTPlantingRecipe> = create(RagiumConstants.PLANTING)

    // Electronics
    @JvmField
    val ELECTROLYZING: RecipeType<RTElectrolyzingRecipe> = create(RagiumConstants.ELECTROLYZING)

    @JvmField
    val RESOURCE_EXTRACTING: RecipeType<RTResourceExtractingRecipe> = create(RagiumConstants.RESOURCE_EXTRACTING)

    // Arcane
    @JvmField
    val ENCHANTING: RecipeType<RTEnchantingRecipe> = create(RagiumConstants.ENCHANTING)
}
