package hiiragi283.ragium.common.recipe.custom

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.itemLookup
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.tag.HTTagUtil
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTMaterialCrushingRecipe(val inputPrefix: HTTagPrefix, val inputCount: Int, val outputCount: Int) : HTMachineRecipe() {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMaterialCrushingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTTagPrefix.CODEC.fieldOf("input_prefix").forGetter(HTMaterialCrushingRecipe::inputPrefix),
                    ExtraCodecs.POSITIVE_INT
                        .optionalFieldOf("input_count", 1)
                        .forGetter(HTMaterialCrushingRecipe::inputCount),
                    ExtraCodecs.POSITIVE_INT
                        .optionalFieldOf("output_count", 1)
                        .forGetter(HTMaterialCrushingRecipe::outputCount),
                ).apply(instance, ::HTMaterialCrushingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMaterialCrushingRecipe> = StreamCodec
            .composite(
                HTTagPrefix.STREAM_CODEC,
                HTMaterialCrushingRecipe::inputPrefix,
                ByteBufCodecs.VAR_INT,
                HTMaterialCrushingRecipe::inputCount,
                ByteBufCodecs.VAR_INT,
                HTMaterialCrushingRecipe::outputCount,
                ::HTMaterialCrushingRecipe,
            ).cast<RegistryFriendlyByteBuf>()
    }

    override fun matches(input: HTMachineInput): Boolean {
        for (key: HTMaterialKey in RagiumAPI.getInstance().getMaterialRegistry().keys) {
            val tagKey: TagKey<Item> = inputPrefix.createItemTag(key)
            val stackIn: ItemStack = input.getItemStack(HTStorageIO.INPUT, 0)
            if (stackIn.`is`(tagKey) && stackIn.count >= inputCount) {
                return true
            }
        }
        return false
    }

    override fun canProcess(input: HTMachineInput): Boolean {
        var targetMaterial: HTMaterialKey? = null
        for (key: HTMaterialKey in RagiumAPI.getInstance().getMaterialRegistry().keys) {
            val tagKey: TagKey<Item> = inputPrefix.createItemTag(key)
            val stackIn: ItemStack = input.getItemStack(HTStorageIO.INPUT, 0)
            if (stackIn.`is`(tagKey)) {
                targetMaterial = key
                break
            }
        }
        if (targetMaterial == null) return false

        val lookup: HolderLookup.RegistryLookup<Item> =
            RagiumAPI.getInstance().getRegistryAccess()?.itemLookup() ?: return false
        val firstItem: Item = HTTagUtil.getFirstItem(lookup, HTTagPrefixes.DUST, targetMaterial) ?: return false
        val output = ItemStack(firstItem, outputCount)

        // Item output
        val outputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.OUTPUT, 0) ?: return false
        if (!outputSlot.canInsert(output)) return false
        // Item input
        val inputSlot: HTItemSlot = input.getSlotOrNull(HTStorageIO.INPUT, 0) ?: return false
        return inputSlot.canExtract(inputCount)
    }

    override fun process(input: HTMachineInput) {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MATERIAL_CRUSHING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
