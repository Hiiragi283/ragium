package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.resource.toId
import net.minecraft.core.HolderLookup
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.context.ContextKey
import net.minecraft.util.context.ContextKeySet
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.RecipeMap
import net.minecraft.world.level.Level

/**
 * [HTRecipeLookup]で使用される[ContextKey]をまとめたクラスです。
 *
 * 参照 : [Minecraft - SlotDisplayContext][net.minecraft.world.item.crafting.display.SlotDisplayContext]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipeLookupContext {
    @JvmStatic
    private fun <T : Any> createKey(path: String): ContextKey<T> = ContextKey(HTConstants.MOD_ID.toId(path))

    /**
     * レシピの一覧に紐づく[ContextKey]
     */
    @JvmField
    val RECIPES: ContextKey<RecipeMap> = createKey("recipes")

    /**
     * レジストリへのアクセスに紐づく[ContextKey]
     */
    @JvmField
    val REGISTRIES: ContextKey<HolderLookup.Provider> = createKey("registries")

    /**
     * 醸造レシピの一覧に紐づく[ContextKey]
     */
    @JvmField
    val BREWING: ContextKey<PotionBrewing> = createKey("brewing")

    /**
     * [HTRecipeLookup]で使用される[ContextKey]の条件
     */
    @JvmField
    val CONTEXT: ContextKeySet = ContextKeySet.Builder().required(RECIPES).optional(REGISTRIES).optional(BREWING).build()

    /**
     * [CONTEXT]に基づいた新しい[ContextMap]のインスタンスを作成します。
     * @since 26.1.2
     */
    @JvmStatic
    fun createOnClient(level: Level): ContextMap = ContextMap.Builder()
        .withParameter(RECIPES, HTPhysicalSideHelper.cachedRecipes)
        .withParameter(REGISTRIES, level.registryAccess())
        .withParameter(BREWING, level.potionBrewing())
        .create(CONTEXT)

    /**
     * [CONTEXT]に基づいた新しい[ContextMap]のインスタンスを作成します。
     */
    @JvmStatic
    fun create(level: ServerLevel): ContextMap = create(level.server)

    /**
     * [CONTEXT]に基づいた新しい[ContextMap]のインスタンスを作成します。
     * @since 26.1.2
     */
    @JvmStatic
    fun create(server: MinecraftServer): ContextMap = ContextMap.Builder()
        .withParameter(RECIPES, server.recipeManager.recipeMap())
        .withParameter(REGISTRIES, server.registryAccess())
        .withParameter(BREWING, server.potionBrewing())
        .create(CONTEXT)
}
