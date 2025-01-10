package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.Recipe
import net.minecraft.registry.RegistryWrapper

/**
 * 機械レシピのベースとなるクラス
 */
abstract class HTMachineRecipe(val definition: HTMachineDefinition, val data: HTMachineRecipeData) : Recipe<HTMachineInput> {
    companion object {
        @JvmStatic
        fun <T : HTMachineRecipe> createPacketCodec(factory: Factory<T>): PacketCodec<RegistryByteBuf, T> = PacketCodec.tuple(
            HTMachineDefinition.PACKET_CODEC,
            HTMachineRecipe::definition,
            HTMachineRecipeData.PACKET_CODEC,
            HTMachineRecipe::data,
            factory::create,
        )
    }

    val key: HTMachineKey = definition.key
    val tier: HTMachineTier = definition.tier

    /**
     * 指定した[index]から[HTItemIngredient]を返します。
     */
    fun getItemIngredient(index: Int): HTItemIngredient? = data.itemIngredients.getOrNull(index)

    /**
     * 指定した[index]から[HTFluidIngredient]を返します。
     */
    fun getFluidIngredient(index: Int): HTFluidIngredient? = data.fluidIngredients.getOrNull(index)

    /**
     * 指定した[index]から[HTItemResult]を返します。
     */
    fun getItemResult(index: Int): HTItemResult? = data.itemResults.getOrNull(index)

    /**
     * 指定した[index]から[HTFluidResult]を返します。
     */
    fun getFluidResult(index: Int): HTFluidResult? = data.fluidResults.getOrNull(index)

    fun checkDefinition(input: HTMachineInput): Boolean {
        if (!data.isValidOutput(true)) return false
        if (input.key != this.key) return false
        if (input.tier < this.tier) return false
        return true
    }

    //    Recipe    //

    final override fun craft(input: HTMachineInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    final override fun fits(width: Int, height: Int): Boolean = true

    final override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = getItemResult(0)?.stack ?: ItemStack.EMPTY

    final override fun isIgnoredInRecipeBook(): Boolean = true

    final override fun showNotification(): Boolean = false

    final override fun createIcon(): ItemStack = definition.iconStack

    final override fun isEmpty(): Boolean = true

    //    Factory    //

    /**
     * 指定した値から[HTMachineRecipe]を返す関数型インターフェースです。
     */
    fun interface Factory<T : HTMachineRecipe> {
        /**
         * @param definition 機械の情報
         * @param data レシピのインプット/アウトプット
         */
        fun create(definition: HTMachineDefinition, data: HTMachineRecipeData): T
    }
}
