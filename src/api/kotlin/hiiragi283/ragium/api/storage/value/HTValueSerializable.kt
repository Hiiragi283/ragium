package hiiragi283.ragium.api.storage.value

/**
 * @see [net.neoforged.neoforge.common.util.INBTSerializable]
 */
interface HTValueSerializable {
    fun serialize(output: HTValueOutput)

    fun deserialize(input: HTValueInput)
}
