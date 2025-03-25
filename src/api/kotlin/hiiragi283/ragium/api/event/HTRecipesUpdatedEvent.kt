package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.itemLookup
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.bus.api.Event
import java.util.function.Function
import kotlin.jvm.optionals.getOrNull

/**
 * レシピが再読み込みされたときに呼び出されるイベント
 * @see [HTMachineRecipeType.reloadCache]
 */
class HTRecipesUpdatedEvent(
    provider: HolderLookup.Provider,
    private val currentType: HTMachineRecipeType,
    private val consumer: (RecipeHolder<*>) -> Unit,
) : Event(),
    HolderLookup.Provider by provider {
    /**
     * レシピを登録します
     * @param R 登録するレシピのクラス
     * @param recipeType 登録するレシピの種類
     * @param recipeId 登録するレシピのID
     * @param function 登録するレシピのビルダーを返すブロック
     */
    fun <R : Recipe<*>> register(recipeType: RecipeType<R>, recipeId: ResourceLocation, function: Function<HolderGetter<Item>, R?>) {
        if (currentType == recipeType) {
            val recipe: R = function.apply(itemLookup()) ?: return
            consumer(RecipeHolder(recipeId.withPrefix("/"), recipe))
        }
    }

    //    Helper    //

    /**
     * 指定した[prefix]と[key]に含まれる[Holder]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    fun getFirstHolder(prefix: HTTagPrefix, key: HTMaterialKey): Holder<Item>? = getFirstHolder(prefix.createTag(key))

    /**
     * 指定した[prefix]と[key]に含まれる[Item]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    fun getFirstItem(prefix: HTTagPrefix, key: HTMaterialKey): Item? = getFirstHolder(prefix, key)?.value()

    /**
     * 指定した[tagKey]に含まれる[Holder]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    fun getFirstHolder(tagKey: TagKey<Item>): Holder<Item>? {
        val holderSet: HolderSet.Named<Item> = itemLookup().get(tagKey).getOrNull() ?: return null
        // Find item from Ragium
        var firstHolder: Holder<Item>? =
            holderSet.firstOrNull { holder: Holder<Item> -> holder.idOrThrow.namespace == RagiumAPI.MOD_ID }
        // Find item from Vanilla
        if (firstHolder == null) {
            firstHolder = holderSet.firstOrNull { holder: Holder<Item> -> holder.idOrThrow.namespace == "minecraft" }
        }
        // Return found item or first item
        return firstHolder ?: holderSet.firstOrNull()
    }

    /**
     * 指定した[tagKey]に含まれる[Item]を返します。
     * @return 名前空間が`ragium`, `minecraft`の順に検索し，見つからない場合は最初の値を返す
     */
    fun getFirstItem(tagKey: TagKey<Item>): Item? = getFirstHolder(tagKey)?.value()
}
